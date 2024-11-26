package com.example.projecting_feature.model

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.times
import kotlin.time.times

data class ProjectingSettingsState(
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val offsetZ: Float = 0f,
    val rotationX: Float = 0f,
    val rotationY: Float = 0f,
    val rotationZ: Float = 0f,
    val scale: Float = 1f,
    val R: Float = 100f,
    val H: Float = 100f,
    val alphaRange: Pair<Float, Float> = 0f to 2*Math.PI.toFloat(),
    val kRange: Pair<Float, Float> = -0.5f to 0.5f,
    val alphaN: Int = 10,
    val kN: Int = 10,
    val cameraZ: Float = 500f
)
