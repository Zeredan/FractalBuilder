package com.example.projecting_feature.ui.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecting_feature.domain.projectPoint
import com.example.projecting_feature.domain.resolvePoints3D
import com.example.projecting_feature.model.Point2D
import com.example.projecting_feature.model.Point3D
import com.example.projecting_feature.model.ProjectingSettingsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ProjectingViewModel : ViewModel() {
    val projectingSettingsStateFlow = MutableStateFlow(ProjectingSettingsState())
    val canvasSizeStateFlow = MutableStateFlow(IntSize(100, 100))

    val surfacePointsStateFlow = projectingSettingsStateFlow.mapLatest{
        resolvePoints3D(
            it.offsetX,
            it.offsetY,
            it.offsetZ,
            Math.toRadians(it.rotationX.toDouble()).toFloat(),
            Math.toRadians(it.rotationY.toDouble()).toFloat(),
            Math.toRadians(it.rotationZ.toDouble()).toFloat(),
            it.scale,
            it.R,
            it.H,
            it.alphaRange,
            it.kRange,
            it.alphaN,
            it.kN
        )
    }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, emptyList())

    val surfaceProjectedPointsStateFlow = surfacePointsStateFlow.mapLatest {
        it.map {
            it.map {
                it.projectPoint(projectingSettingsStateFlow.value.cameraZ)
            }
        }
    }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, emptyList())

    val resultImageStateFlow = MutableStateFlow<Bitmap?>(null)

    private val lockForUpdatingImage = Mutex(false)
    init{
        val cs = CoroutineScope(Dispatchers.Default)
        cs.launch {
            surfaceProjectedPointsStateFlow.collectLatest { points ->
                updateResultingImage(points, canvasSizeStateFlow.value)
            }
        }
        cs.launch {
            canvasSizeStateFlow.collectLatest{ size ->
                updateResultingImage(surfaceProjectedPointsStateFlow.value, size)
            }
        }
    }

    suspend fun updateResultingImage(points: List<List<Point2D>>, size: IntSize){
        lockForUpdatingImage.withLock {
            try {
                val bm = Bitmap.createBitmap(
                    size.width,
                    size.height,
                    Bitmap.Config.ARGB_8888
                )
                Canvas(bm).apply {
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f
                    val N = points.size
                    val M = points[0].size
                    points.forEachIndexed { i, row ->
                        row.forEachIndexed { j, p ->
                            val nextI = (i + 1) % N
                            val nextJ = (j + 1) % M
                            drawLine(
                                centerX + p.x,
                                centerY + p.y,
                                centerX + points[i][nextJ].x,
                                centerY + points[i][nextJ].y,
                                Paint().apply {
                                    shader = LinearGradientShader(
                                        Offset.Zero,
                                        canvasSizeStateFlow.value.center.toOffset() * 2f,
                                        listOf(Color.Green, Color.Blue)
                                    )
                                }
                            )
                            drawLine(
                                centerX + p.x,
                                centerY + p.y,
                                centerX + points[nextI][j].x,
                                centerY + points[nextI][j].y,
                                Paint().apply {
                                    shader = LinearGradientShader(
                                        Offset.Zero,
                                        canvasSizeStateFlow.value.center.toOffset() * 2f,
                                        listOf(Color.Cyan, Color.Magenta)
                                    )
                                }
                            )
                        }
                    }
                }
                resultImageStateFlow.value = bm
            } catch (e: Exception) {

            }
        }
    }

    fun updateSize(size: IntSize){
        viewModelScope.launch {
            canvasSizeStateFlow.emit(size)
        }
    }
    fun updateOffsetX(offset: Float){
        viewModelScope.launch {
            projectingSettingsStateFlow.emit(projectingSettingsStateFlow.value.copy(offsetX = offset))
        }
    }
    fun updateOffsetY(offset: Float){
        viewModelScope.launch {
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(offsetY = offset)
        }
    }
    fun updateOffsetZ(offset: Float){
        viewModelScope.launch {
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(offsetZ = offset)
        }
    }

    fun updateRotationX(rX: Float){
        viewModelScope.launch {
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(rotationX = rX)
        }
    }
    fun updateRotationY(rY: Float){
        viewModelScope.launch {
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(rotationY = rY)
        }
    }
    fun updateRotationZ(rZ: Float){
        viewModelScope.launch {
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(rotationZ = rZ)
        }
    }

    fun updateQuality(newN: Float){
        viewModelScope.launch{
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(
                alphaN = newN.toInt(),
                kN = newN.toInt()
            )
        }
    }
    fun updateHeight(newH: Float){
        viewModelScope.launch{
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(
                H = newH
            )
        }
    }
    fun updateRadius(newR: Float){
        viewModelScope.launch{
            projectingSettingsStateFlow.value = projectingSettingsStateFlow.value.copy(
                R = newR
            )
        }
    }
}