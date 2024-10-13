package com.example.fractalbuilderfeature.Views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fractalbuilderfeature.ViewModels.FractalBuilderViewModel
import kotlin.math.roundToInt

@Composable
fun FractalBuilderRoot(){
    val vm: FractalBuilderViewModel = viewModel<FractalBuilderViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        var width by remember {
            mutableStateOf(0)
        }
        val density = LocalDensity.current.density
        Text(
            "Z[k+1] = Z[k]^4 + Z[0]"
        )
        Text(
            "x: (${vm.leftBound}; ${vm.rightBound})"
        )
        Text(
            "y: (${vm.bottomBound}; ${vm.topBound})"
        )
        Text("Условие остановки: |Z[k] > 2|")

        Box(
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        {
            Box(
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
                    .onSizeChanged { width = it.width }
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->
                            println("$centroid|||$pan|||$zoom|||$rotation")
                            vm.offsetDp += pan / density
                            vm.scale *= zoom
                            vm.offsetDp = vm.offsetDp * zoom
                        }
                    }
            )
            {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(vm.offsetDp.x.roundToInt().dp, vm.offsetDp.y.roundToInt().dp)
                        .scale(vm.scale)
                )
                {
                    if (vm.fractureBitmap != null) drawImage(vm.fractureBitmap!!)
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        OutlinedButton(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .height(30.dp),
            onClick = {
                vm.processImage((width), (width), density)
            },
            enabled = !vm.isCalculating,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green, disabledContainerColor = Color.Yellow)
        )
        {
            Text("Вычислить", color = Color.Magenta, fontSize = 24.sp)
        }
    }
}