package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import com.example.paintfeature.DrawFunctions.drawBresenhamLine
import com.example.paintfeature.ViewModels.PaintViewModel
import kotlin.math.roundToInt

class PixelDrawerOption(vm: PaintViewModel) : PaintViewModel.ControlOption(vm) {
    var prevPoint = Offset.Zero
    var currentPoint = Offset.Zero


    private fun Bitmap.withPixel(x: Int, y: Int, x1: Int, y1: Int) : Bitmap{
        var result = this.copy(this.config, true)
        try{
            drawBresenhamLine(x, y, x1, y1, Color.GREEN, result)
        }
        catch (e: Exception){
        }
        return result
    }
    override suspend fun onDown(offset: Offset) {
        currentPoint = offset
        vm.addWithMemory(vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset])
    }

    override suspend fun onMove(offset: Offset) {
        prevPoint = currentPoint
        currentPoint += offset
        vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset] =
            vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset].withPixel(
                prevPoint.x.roundToInt(),
                prevPoint.y.roundToInt(),
                currentPoint.x.roundToInt(),
                currentPoint.y.roundToInt(),
            )
    }

    override suspend fun onUp() {

    }
}