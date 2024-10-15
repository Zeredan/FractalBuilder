package com.example.paintfeature.ViewModels

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cg_lab2.R
import com.example.paintfeature.CanvasCursorProcessing.BesierBuilderOption
import com.example.paintfeature.CanvasCursorProcessing.CircleBuilderOption
import com.example.paintfeature.CanvasCursorProcessing.FillColorBarkBeetle
import com.example.paintfeature.CanvasCursorProcessing.FillImageAmplified
import com.example.paintfeature.CanvasCursorProcessing.LineBuilderOption
import com.example.paintfeature.CanvasCursorProcessing.PixelDrawerOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PaintViewModel : ViewModel() {
    public abstract class ControlOption(var vm: PaintViewModel) {
        abstract suspend fun onDown(offset: Offset)
        abstract suspend fun onMove(offset: Offset)
        abstract suspend fun onUp()

        open suspend fun onDisableControl(){

        }
        open suspend fun onEnableControl(){

        }
    }
    var canvasScale by mutableStateOf(1f)
    var canvasOffset by mutableStateOf(Offset.Zero)
    var selectedOption by mutableStateOf<Int?>(null)
        private set
    var memoryIndexOffset by mutableStateOf(0)
    var bitmapStack = mutableStateListOf<Bitmap>()
    val reqDeactivate = listOf(R.drawable.besie)

    lateinit var fillBitmap: Bitmap
    var canvasSizeStateFlow : MutableStateFlow<IntSize> = MutableStateFlow(IntSize.Zero)

    private val options = mapOf<Int?, ControlOption>(
        R.drawable.pen to PixelDrawerOption(this),
        R.drawable.line to LineBuilderOption(this),
        R.drawable.circle to CircleBuilderOption(this),
        R.drawable.besie to BesierBuilderOption(this),
        R.drawable.rows to FillColorBarkBeetle(this),
        R.drawable.polygon to FillImageAmplified(this)
    )

    init{
        viewModelScope.launch {
            var seenFirstValue = false
            canvasSizeStateFlow.collect{
                if (seenFirstValue) {
                    initiateBitmapStack()
                    this.cancel("initiated")
                }
                else{
                    seenFirstValue = true
                }
            }
        }
    }

    fun _setSelectedOption(img: Int?){
        if (selectedOption != img){
            viewModelScope.launch(Dispatchers.Main){
                val prev = selectedOption
                selectedOption = img
                options[prev]?.onDisableControl()
                options[img]?.onEnableControl()
            }
        }
    }
    fun backStack(){
        if (-memoryIndexOffset < bitmapStack.size - 1) memoryIndexOffset--
    }
    fun forwardStack(){
        if (memoryIndexOffset < 0) memoryIndexOffset++
    }
    fun addWithMemory(bm: Bitmap){
        val result = bitmapStack.slice(0..bitmapStack.lastIndex + memoryIndexOffset) + bm
        memoryIndexOffset = 0
        bitmapStack.clear()
        bitmapStack.addAll(result)
    }
    fun initiateBitmapStack(){
        bitmapStack = mutableStateListOf(
            Bitmap.createBitmap(
                Array(canvasSizeStateFlow.value.height){
                    Array(canvasSizeStateFlow.value.width){
                        Color.BLACK
                    }
                }.flatten().toIntArray(),
                canvasSizeStateFlow.value.width,
                canvasSizeStateFlow.value.width,
                Bitmap.Config.ARGB_8888
            )
        )
    }

    fun canvasCursorDown(offset: Offset){
        viewModelScope.launch(Dispatchers.Main) {
            options[selectedOption]?.onDown(offset)
        }
    }
    fun canvasCursorMove(offset: Offset){
        viewModelScope.launch(Dispatchers.Main) {
            options[selectedOption]?.onMove(offset)
        }
    }
    fun canvasCursorUp(){
        viewModelScope.launch(Dispatchers.Main) {
            options[selectedOption]?.onUp()
        }
    }
}