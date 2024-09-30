package com.example.cg_lab2.ui.theme.Screens

import android.graphics.Bitmap
import android.icu.util.MeasureUnit.Complexity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cg_lab2.ViewModels.Task1ViewModel
import com.example.cg_lab2.ui.theme.CG_lab2Theme
import com.example.cg_lab2.ui.theme.CustomComposables.NextNavigationButton
import com.example.cg_lab2.ui.theme.CustomComposables.PrevNavigationButton
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Complex(
    var x: Float,
    var y: Float
)
{
    operator fun plus(b: Complex) : Complex{
        return Complex(this.x + b.x, this.y + b.y)
    }
    operator fun minus(b: Complex) : Complex{
        return Complex(this.x - b.x, this.y - b.y)
    }
    operator fun times(b: Complex) : Complex{
        return Complex(this.x * b.x - this.y * b.y, this.x * b.y + this.y * b.x)
    }
    operator fun div(b: Complex) : Complex{
        return (b.x * b.x + b.y * b.y).run denom@{
            Complex(
                (this@Complex.x * b.x + this@Complex.y * b.y) / this,
                (b.x * this@Complex.y - this@Complex.x * b.y) / this
            )
        }
    }
    val modulo : Float
        get(){
            return sqrt(x*x.toDouble() + y*y.toDouble()).toFloat()
        }
}

fun formula(Zk: Complex, Z0 : Complex) : Complex
{
    return Zk*Zk*Zk*Zk + Z0
    //return Zk*Zk*Zk + Complex(-0.7f, 0.27015f)
}

fun getPointIterations(Z0: Complex) : Int{
    if (Z0.modulo > 2f) return 0
    var Zk = Z0
    var iterationCount = 0
    while(iterationCount < 50 && Zk.modulo < 2f)
    {
        Zk = formula(Zk, Z0)
        ++iterationCount
    }
    return iterationCount
}

fun <T> createBitmap(
    colors: Array<Array<T>>,
    transform: (T) -> Int,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888,
): Bitmap
{
    val intColors = colors
        .flatten()
        .map(transform)
        .toIntArray()
    val width = colors.firstOrNull()?.size ?: 0
    val height = colors.size

    return Bitmap.createBitmap(
        intColors,
        width,
        height,
        config,
    )
}

@Composable
fun Task1Screen(){
    val vm: Task1ViewModel = viewModel<Task1ViewModel>()
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
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green, disabledContainerColor = Color.Yellow)
        )
        {
            Text("Вычислить", color = Color.Magenta, fontSize = 24.sp)
        }
    }
}