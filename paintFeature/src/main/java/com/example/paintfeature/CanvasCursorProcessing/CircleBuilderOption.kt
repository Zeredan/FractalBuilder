package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.set
import com.example.paintfeature.ViewModels.PaintViewModel
import kotlin.math.roundToInt
import kotlin.math.sqrt

class CircleBuilderOption(vm: PaintViewModel) : PaintViewModel.ControlOption(vm) {
    private var positionA: Offset = Offset.Zero
    private var positionB: Offset = Offset.Zero
    private lateinit var initialBitmap: Bitmap

    private fun drawBrezenhamCircle(x1: Int, y1: Int, radius: Int, result: Bitmap){
        var x = 0
        var y = radius
        var delta = 1 - 2 * radius
        var error = 0
        while(y >= x){

                listOf(
                    x1 + x to y1 + y,
                    x1 + x to y1 - y,
                    x1 - x to y1 + y,
                    x1 - x to y1 - y,
                    x1 + y to y1 + x,
                    x1 + y to y1 - x,
                    x1 - y to y1 + x,
                    x1 - y to y1 - x
                ).forEach { (ix, iy) ->
                    try {
                        result[ix, iy] = Color.MAGENTA
                    }
                    catch (e: Exception){

                    }
                }


            error = 2 * (delta + y) - 1
            if ((delta < 0) && (error <= 0)){
                delta += 2 * ++x + 1
            }
            else if((delta > 0) && (error > 0)){
                delta -= 2 * --y + 1
            }
            else delta += 2 * (++x - --y)
        }
    }
    fun Bitmap.withCircle(center: Offset, radius: Float) : Bitmap{
        val result = this.copy(this.config, true)
        drawBrezenhamCircle(center.x.roundToInt(), center.y.roundToInt(), radius.toInt(), result)
        return result
    }

    override suspend fun onDown(offset: Offset) {
        initialBitmap = vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset]
        vm.addWithMemory(initialBitmap)
        positionA = offset
        positionB = offset
    }

    override suspend fun onMove(offset: Offset) {
        positionB += offset
        vm.bitmapStack[vm.bitmapStack.lastIndex] = initialBitmap.withCircle(
            positionA,
            sqrt((positionA.x - positionB.x).let{it*it} + (positionA.y - positionB.y).let{it*it})
        )
    }

    override suspend fun onUp() {

    }

}