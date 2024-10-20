package com.example.imageprocessing.ViewModels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.decodeBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageprocessing.ImageProcessingFunctions.toGray
import com.example.imageprocessing.ImageProcessingFunctions.toHeights
import com.example.imageprocessing.ImageProcessingFunctions.toMonochromatic
import com.example.imageprocessing.ImageProcessingFunctions.toSkeletized
import com.example.imageprocessing.ImageProcessingFunctions.toSmooth10
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class ImageProcessingViewModel(var context: Context, val scrollToMe: () -> Unit, intent: Intent) : ViewModel() {
    private val uriSharedFlow = MutableSharedFlow<Uri?>(replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.SUSPEND)
    private val skeletizationUriSharedFlow = MutableSharedFlow<Uri?>(replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.SUSPEND)

    var autoProcessImages by mutableStateOf(false)
    var startingImageBitmap by mutableStateOf<Bitmap?>(null)
    private set

    var grayImageBitmap by mutableStateOf<Bitmap?>(null)
    private set

    var monoChromaticImageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var monochromaticTreshold by mutableStateOf<Int>(160)
    var isMonochromaticStateChanged by mutableStateOf(true)
    var isMonochromaticCalculated by mutableStateOf(true)

    var smooth10ImageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var heights1ImageBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var heights2ImageBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var heights3ImageBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var heights4ImageBitmap by mutableStateOf<Bitmap?>(null)
        private set


    var skeletizationStartingBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var skeletizationMonochromaticBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var skeletizationProcessedBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var skeletizationMonochromaticTreshold by mutableStateOf(128)

    init{
        viewModelScope.launch {
            delay(1000)
            val uri = intent.extras?.getParcelable(Intent.EXTRA_STREAM, Uri::class.java)
            uriSharedFlow.tryEmit(uri)
            skeletizationUriSharedFlow.tryEmit(uri)
            uri?.let {
                scrollToMe()
            }
            startProcessingImages()
            startProcessingSkeletizationImages()
        }
    }

    fun processStartingImageBitmap(uri: Uri){
        viewModelScope.launch{
            startingImageBitmap = withContext(Dispatchers.Default) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(context.contentResolver, uri),
                ).copy(Bitmap.Config.ARGB_8888, false)
            }
        }
    }
    fun processGrayImageBitmap(){
        viewModelScope.launch{
            grayImageBitmap = withContext(Dispatchers.Default) {
                startingImageBitmap?.toGray()
            }
        }
    }
    fun processMonochromaticImageBitmap(){
        isMonochromaticCalculated = false
        isMonochromaticStateChanged = false
        viewModelScope.launch{
            monoChromaticImageBitmap = withContext(Dispatchers.Default) {
                grayImageBitmap?.toMonochromatic(monochromaticTreshold)
            }
            isMonochromaticCalculated = true
        }
    }
    fun processSmooth10ImageBitmap(){
        viewModelScope.launch{
            smooth10ImageBitmap = withContext(Dispatchers.Default) {
                grayImageBitmap?.toSmooth10()
            }
        }
    }
    fun processHeightsImageBitmap(){
        viewModelScope.launch{
            heights1ImageBitmap = withContext(Dispatchers.Default) {
                grayImageBitmap?.toHeights(0)
            }
        }
        viewModelScope.launch{
            heights2ImageBitmap = withContext(Dispatchers.Default) {
                grayImageBitmap?.toHeights(1)
            }
        }
        viewModelScope.launch{
            heights3ImageBitmap = withContext(Dispatchers.Default) {
                grayImageBitmap?.toHeights(2)
            }
        }
        viewModelScope.launch{
            heights4ImageBitmap = withContext(Dispatchers.Default) {
                grayImageBitmap?.toHeights(3)
            }
        }
    }

    fun processStartingSkeletizationImageBitmap(uri: Uri){
        viewModelScope.launch {
            skeletizationStartingBitmap = withContext(Dispatchers.Default) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(context.contentResolver, uri),
                ).copy(Bitmap.Config.ARGB_8888, false)
            }
        }
    }
    fun processMonochromaticSkeletizationImageBitmap(treshold: Int){
        viewModelScope.launch {
            skeletizationMonochromaticBitmap = withContext(Dispatchers.Default) {
                skeletizationStartingBitmap?.toMonochromatic(treshold)
            }
        }
    }
    fun processSkeletizationImageBitmap(){
        viewModelScope.launch {
            skeletizationProcessedBitmap = withContext(Dispatchers.Default){
                skeletizationMonochromaticBitmap?.toSkeletized()
            }
        }
    }
    private fun startProcessingImages(){
        viewModelScope.launch{
            uriSharedFlow
                .filter { it != null }
                .collect { uri ->
                    processStartingImageBitmap(uri!!)
                    if (autoProcessImages) {
                        processGrayImageBitmap()
                        processMonochromaticImageBitmap()
                        processSmooth10ImageBitmap()
                        processHeightsImageBitmap()
                    }
                }
        }
    }
    private fun startProcessingSkeletizationImages(){
        viewModelScope.launch{
            skeletizationUriSharedFlow
                .filter { it != null }
                .collect { uri ->
                    processStartingSkeletizationImageBitmap(uri!!)
                    processSkeletizationImageBitmap()
                }
        }
    }
    fun emitUri(uri: Uri?){
        viewModelScope.launch {
            uriSharedFlow.emit(uri)
        }
    }
    fun emitSkeletizationUri(uri: Uri?){
        viewModelScope.launch {
            skeletizationUriSharedFlow.emit(uri)
        }
    }
}