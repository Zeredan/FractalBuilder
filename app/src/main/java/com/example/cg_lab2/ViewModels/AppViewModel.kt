package com.example.cg_lab2.ViewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInQuart
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
                coroutineScope?.launch { anchoredDraggableState.animateTo("Обработка изображений", 1f) }
            }
        }
    )

    @OptIn(ExperimentalFoundationApi::class)
    var anchoredDraggableState = AnchoredDraggableState<String>(
            initialValue = "Фракталы",
            anchors = DraggableAnchors<String> {
            },
            positionalThreshold = { it1 ->
                it1 * 0.5f
            },
            velocityThreshold = {
                100f
            },
            animationSpec = tween(200, 0, EaseInQuart)
    )

}