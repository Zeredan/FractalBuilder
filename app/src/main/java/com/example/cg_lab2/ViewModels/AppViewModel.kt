package com.example.cg_lab2.ViewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorizedDecayAnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fractalbuilderfeature.Views.FractalBuilderRoot
import com.example.imageprocessing.Views.ImageProcessingRoot
import com.example.paintfeature.Views.PaintRoot
import com.example.paintfeature.Views.ProjectingRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class AppViewModel : ViewModel() {
    var coroutineScope: CoroutineScope? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalFoundationApi::class)
    val contentScreens = linkedMapOf<String, @Composable () -> Unit>(
        "Фракталы" to { FractalBuilderRoot() },
        "Рисование" to { PaintRoot() },
        "Обработка изображений" to {
            ImageProcessingRoot{
                coroutineScope?.launch { anchoredDraggableState.animateTo("Обработка изображений") }
            }
        },
        "3D" to { ProjectingRoot() }
    )

    @OptIn(ExperimentalFoundationApi::class)
    var anchoredDraggableState = AnchoredDraggableState<String>(
        initialValue = "3D",
        anchors = DraggableAnchors<String> {
        },
        positionalThreshold = { it1: Float ->
            it1 * 0.5f
        },
        velocityThreshold = {
            100f
        },
        animationSpec = tween(200, 0, EaseInQuart),
    )
//    @OptIn(ExperimentalFoundationApi::class)
//    var anchoredDraggableState = AnchoredDraggableState<String>(
//        initialValue = "3D",
//        anchors = DraggableAnchors<String> {
//        },
//        positionalThreshold = { it1: Float ->
//            it1 * 0.5f
//        },
//        velocityThreshold = {
//            100f
//        },
//        snapAnimationSpec = tween(200, 0, EaseInQuart),
//        decayAnimationSpec = exponentialDecay()
//    )
}