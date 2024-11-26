package com.example.paintfeature.Views

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.projecting_feature.ui.composables.OffsetChanger
import com.example.projecting_feature.ui.viewmodels.ProjectingViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProjectingRoot() {
    val context = LocalContext.current as ComponentActivity
    val vm by context.viewModels<ProjectingViewModel>()
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
                        vm.updateSize(it)
                    },
                contentAlignment = Alignment.Center
            )
            {
                val image by vm.resultImageStateFlow.collectAsState()
                val textMeasurer = rememberTextMeasurer()
                Canvas(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxSize()
                )
                {
                    drawLine(
                        Brush.linearGradient(listOf(Color(180, 20, 0), Color.Red)),
                        strokeWidth = 4f,
                        start = center,
                        end = center.copy(y = center.y * 2),
                    )
                    drawPath(
                        Path().apply {
                            this.moveTo(center.x - 10, center.y * 2f - 30)
                            this.lineTo(center.x + 10, center.y * 2f - 30)
                            this.lineTo(center.x, center.y * 2f)
                            this.lineTo(center.x - 10, center.y * 2f - 30)
                        },
                        brush = Brush.linearGradient(listOf(Color(180, 20, 0), Color.Red))
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "Y",
                        topLeft = center.copy(x = center.x - 60, y = center.y * 2f - 60),
                        style = TextStyle(color = Color.Red)
                    )
                    drawLine(
                        Brush.linearGradient(listOf(Color(20, 140, 200), Color.Blue)),
                        strokeWidth = 4f,
                        start = center,
                        end = center.copy(x = center.x * 2),
                    )
                    drawPath(
                        Path().apply {
                            this.moveTo(center.x * 2f - 30, center.y - 10)
                            this.lineTo(center.x * 2f - 30, center.y + 10)
                            this.lineTo(center.x * 2f, center.y)
                            this.lineTo(center.x * 2f - 30, center.y - 10)
                        },
                        brush = Brush.linearGradient(listOf(Color(20, 140, 200), Color.Blue)),
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "X",
                        topLeft = center.copy(x = center.x * 2f - 60, y = center.y - 60),
                        style = TextStyle(color = Color.Blue)
                    )
                    drawCircle(Color.Green, radius = 10f, center = center)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "Z",
                        topLeft = center.copy(x = center.x - 40, y = center.y - 40),
                        style = TextStyle(color = Color(0, 200, 0))
                    )
                    if (image != null){
                        drawImage(image!!.asImageBitmap())
                    }
                }
            }
            Divider()
            val projectingState by vm.projectingSettingsStateFlow.collectAsState()
            LazyColumn(
                modifier = Modifier
                    .border(
                        color = Color.LightGray,
                        width = 3.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(10.dp)
            )
            {
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        OffsetChanger(
                            modifier = Modifier.height(60.dp),
                            text = "X offset",
                            value = projectingState.offsetX,
                            deltaValue = 50f,
                            onChanged = vm::updateOffsetX
                        )
                    }
                }
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        OffsetChanger(
                            modifier = Modifier.height(60.dp),
                            text = "Y offset",
                            value = projectingState.offsetY,
                            deltaValue = 50f,
                            onChanged = vm::updateOffsetY
                        )
                    }
                }
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        OffsetChanger(
                            modifier = Modifier.height(60.dp),
                            text = "Z offset",
                            value = projectingState.offsetZ,
                            deltaValue = 50f,
                            onChanged = vm::updateOffsetZ
                        )
                    }
                }
                item{
                    Text("X rotation")
                }
                item{
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = projectingState.rotationX,
                        onValueChange = vm::updateRotationX,
                        valueRange = -180f..180f,
                        steps = 36
                    )
                }
                item{
                    Text("Y rotation")
                }
                item{
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = projectingState.rotationY,
                        onValueChange = vm::updateRotationY,
                        valueRange = -180f..180f,
                        steps = 36
                    )
                }
                item{
                    Text("Z rotation")
                }
                item{
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = projectingState.rotationZ,
                        onValueChange = vm::updateRotationZ,
                        valueRange = -180f..180f,
                        steps = 36
                    )
                }
                item{
                    Text("N*N points")
                }
                item{
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = projectingState.kN.toFloat(),
                        onValueChange = vm::updateQuality,
                        valueRange = 10f..100f,
                        steps = 9
                    )
                }
                item{
                    Text("H value")
                }
                item{
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = projectingState.H.toFloat(),
                        onValueChange = vm::updateHeight,
                        valueRange = 10f..1000f,
                        steps = 9
                    )
                }
                item{
                    Text("R value")
                }
                item{
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = projectingState.R.toFloat(),
                        onValueChange = vm::updateRadius,
                        valueRange = 10f..1000f,
                        steps = 9
                    )
                }
            }
        }
    }
}