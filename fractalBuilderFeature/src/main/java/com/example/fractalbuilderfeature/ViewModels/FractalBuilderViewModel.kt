package com.example.fractalbuilderfeature.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fractalbuilderfeature.BusinessLogic.Complex
import com.example.fractalbuilderfeature.BusinessLogic.createBitmap
import com.example.fractalbuilderfeature.BusinessLogic.getPointIterations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FractalBuilderViewModel : ViewModel() {

    var leftBound by mutableStateOf(-2.2f)
    var rightBound by mutableStateOf(1f)
    var topBound by mutableStateOf(1.2f)
    var bottomBound by mutableStateOf(-1.2f)

    var scale by mutableStateOf(1f)
    var offsetDp by mutableStateOf(Offset(0f, 0f))

    var fractureBitmap by mutableStateOf<ImageBitmap?>(null)

    var isCalculating by mutableStateOf(false)
    private set


    fun processImage(width: Int, height: Int, density: Float){
        viewModelScope.launch {
            isCalculating = true
            val partPercent = (scale - 1f) / 2 / scale
            var leftBound1 = leftBound + (rightBound - leftBound) * partPercent
            var rightBound1 = rightBound - (rightBound - leftBound) * partPercent
            var bottomBound1 = bottomBound + (topBound - bottomBound) * partPercent
            var topBound1 = topBound -  (topBound - bottomBound) * partPercent

            val width1 = rightBound1 - leftBound1
            val height1 = topBound1 - bottomBound1

            leftBound1 -= offsetDp.x * density / width * width1
            rightBound1 -= offsetDp.x * density / width * width1
            topBound1 += offsetDp.y * density / height * height1
            bottomBound1 += offsetDp.y * density / height * height1

            leftBound = leftBound1
            rightBound = rightBound1
            bottomBound = bottomBound1
            topBound = topBound1
            
            fractureBitmap = withContext(Dispatchers.Default) {
                val colorsArray = Array(height) { i ->
                    Array(width) { j ->
                        val x =
                            leftBound + (j.toFloat() / width.toFloat()) * (rightBound - leftBound)
                        val y =
                            topBound + (i.toFloat() / height.toFloat()) * (bottomBound - topBound)
                        val color = when (getPointIterations(Complex(x, y)) % 6) {
                            0 -> Color.White
                            1 -> Color.Black
                            2 -> Color.Blue
                            3 -> Color.Green
                            4 -> Color.Yellow
                            5 -> Color.Red
                            else -> Color.Magenta
                        }
                        color
                    }
                }
                createBitmap(colorsArray, Color::toArgb).asImageBitmap()
            }
            scale = 1f
            offsetDp = Offset.Zero
            isCalculating = false
        }
    }
}