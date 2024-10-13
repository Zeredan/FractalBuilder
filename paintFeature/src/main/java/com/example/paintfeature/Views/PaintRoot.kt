package com.example.paintfeature.Views

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cg_lab2.R
import com.example.paintfeature.ViewModels.PaintViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PaintRoot() {
    val vm = viewModel<PaintViewModel>()
    var snackbarHostState = remember{ SnackbarHostState() }
    var coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = Color.Transparent
    )
    {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        )
        {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .onSizeChanged {
                        vm.canvasSizeStateFlow.value = it
                    }
            )
            {
                Canvas(
                    modifier = Modifier
                        .background(Color.Green)
                        .fillMaxSize()
                        .pointerInput(1) {
                            awaitPointerEventScope {
                                var prevPos = Offset.Zero
                                while (true) {
                                    val event = awaitPointerEvent()
                                    when (event.type) {
                                        PointerEventType.Press -> vm.canvasCursorDown(event.changes.first().position)
                                        PointerEventType.Move -> vm.canvasCursorMove(event.changes.first().position - prevPos)
                                        PointerEventType.Release -> vm.canvasCursorUp()
                                    }
                                    prevPos = event.changes.first().position
                                }
                            }
                        }
                        .pointerInput(0) {
                            detectTransformGestures { centroid, pan, zoom, rotation ->
                            }
                        }
                )
                {
                    try{
                        drawImage(vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset].asImageBitmap())
                    }
                    catch (e: Exception){

                    }
                }
            }
            Divider(thickness = 2.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                ImageButton(
                    imageResId = R.drawable.arrow_back,
                    changeSelection = false,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Назад"
                )
                {
                    vm.backStack()
                }
                ImageButton(
                    imageResId = R.drawable.arrow_forward,
                    changeSelection = false,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Вперед"
                )
                {
                    vm.forwardStack()
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                ImageButton(
                    imageResId = R.drawable.line,
                    changeSelection = true,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Прямая с помощью алгоритма Брезенхама"
                )
                {
                }
                ImageButton(
                    imageResId = R.drawable.circle,
                    changeSelection = true,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Окружность с помощью алгоритма Брезенхама"
                )
                {
                }
                ImageButton(
                    imageResId = R.drawable.besie,
                    changeSelection = true,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Кривая Безье(параметрический способ)"
                )
                {
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                ImageButton(
                    imageResId = R.drawable.polygon,
                    changeSelection = true,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Закрашивание полигонов"
                )
                {
                }
                ImageButton(
                    imageResId = R.drawable.rows,
                    changeSelection = true,
                    vm,
                    snackbarHostState = snackbarHostState,
                    message = "Закрашивание с затравкой(модифицированное)"
                )
                {
                }
            }
        }
    }
}