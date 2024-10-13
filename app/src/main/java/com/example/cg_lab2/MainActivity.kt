package com.example.cg_lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.cg_lab2.Views.NextNavigationButton
import com.example.cg_lab2.Views.PrevNavigationButton
import com.example.cg_lab2.ui.theme.CG_lab2Theme
import com.example.fractalbuilderfeature.Views.FractalBuilderRoot
import com.example.paintfeature.Views.PaintRoot
import kotlinx.coroutines.launch

fun Modifier.coloredGradient(colors: List<Color>) : Modifier{
    return this.background(object : ShaderBrush(){
        override fun createShader(size: Size): Shader {
            return LinearGradientShader(Offset.Zero, size.center * 2f, colors)
        }
    })
}

class MainActivity : ComponentActivity() {

    val contentScreens = linkedMapOf<String, @Composable () -> Unit>(
        "Task1" to { FractalBuilderRoot() },
        "Task2" to { PaintRoot() },
    )

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun RootComposable(){
        val width = LocalConfiguration.current.screenWidthDp
        val density = LocalDensity.current.density
        val coroutineScope = rememberCoroutineScope()

        var anchoredDraggableState = remember{
            AnchoredDraggableState<String>(
                initialValue = "Task1",
                anchors = DraggableAnchors<String> {
                    contentScreens.entries.forEachIndexed{ ind, (name) ->
                        name at ind * width * density
                    }
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

        Row(
            modifier = Modifier
                .coloredGradient(listOf(Color.Gray, Color.DarkGray))
                .fillMaxSize()
                .horizontalScroll(
                    ScrollState(anchoredDraggableState.offset.toInt()),
                    enabled = false
                )
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true
                )
        )
        {
            contentScreens.entries.forEachIndexed { ind, data ->
                Column(
                    modifier = Modifier
                        .width(width.dp)
                        .fillMaxHeight()
                )
                {
                    Row(
                        modifier = Modifier
                            .coloredGradient(listOf(Color.Green, Color.Yellow))
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        if (ind > 0){
                            PrevNavigationButton(
                                modifier = Modifier
                                    .coloredGradient(listOf(Color.Blue, Color.Cyan))
                                    .size(100.dp),
                                onClick = {
                                    coroutineScope.launch {
                                        anchoredDraggableState.animateTo(contentScreens.entries.toList()[ind - 1].key)
                                    }
                                }
                            )
                        }
                        else {
                            Spacer(Modifier.width(100.dp))
                        }
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "${data.key}",
                            fontSize = 26.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        if (ind < contentScreens.size - 1){
                            NextNavigationButton(
                                modifier = Modifier
                                    .coloredGradient(listOf(Color.Blue, Color.Cyan))
                                    .size(100.dp),
                                onClick = {
                                    coroutineScope.launch {
                                        anchoredDraggableState.animateTo(contentScreens.entries.toList()[ind + 1].key)
                                    }
                                }
                            )
                        }
                        else {
                            Spacer(Modifier.width(100.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    {
                        data.value()
                    }
                }
            }
        }
    }

    class VM1 : ViewModel(){
        var strst by mutableStateOf("")
    }
    class VM2: ViewModel(){
        var intstr by mutableStateOf(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CG_lab2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    RootComposable()
                }
            }
        }
    }
}
