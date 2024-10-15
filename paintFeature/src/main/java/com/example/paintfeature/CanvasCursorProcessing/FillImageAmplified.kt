package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.get
import androidx.core.graphics.set
import com.example.paintfeature.ViewModels.PaintViewModel
import java.util.Stack
import kotlin.math.roundToInt

class FillImageAmplified(vm: PaintViewModel) : PaintViewModel.ControlOption(vm) {

    fun fillColor(center: Offset, result: Bitmap){
        var s = Stack<Offset>()
        s.push(center)
        while(s.isNotEmpty()) {
            var cur = s.pop()
            var left = cur.x.roundToInt()
            var right = cur.x.roundToInt()
            var curY = cur.y.roundToInt()
            if (result[left, curY] != Color.BLACK) continue
            while (result[left - 1, curY] == Color.BLACK) --left
            while (result[right + 1, curY] == Color.BLACK) ++right

            (left..right).forEach {
                result[it, curY] =
                    vm.fillBitmap[it % vm.fillBitmap.width, curY % vm.fillBitmap.height]
            }
            left += 2
            right -= 2
            var i = left
            while (i < right) {
                while (result[i, curY + 1] == Color.BLACK && i < right) ++i
                s.push(Offset(i - 1f, curY + 1f))
                while (result[i, curY + 1] != Color.BLACK && i < right) ++i
            }

            i = left
            while (i < right) {
                while (result[i, curY - 1] == Color.BLACK && i < right) ++i
                s.push(Offset(i - 1f, curY - 1f))
                while (result[i, curY - 1] != Color.BLACK && i < right) ++i
            }
        }
    }

    fun Bitmap.withFillImage(center: Offset) : Bitmap {
        var result = this.copy(this.config, true)
        fillColor(center, result)
        return result
    }
    override suspend fun onDown(offset: Offset) {
        try {
            vm.addWithMemory(
                vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset].withFillImage(
                    offset
                )
            )
        }
        catch (e: Exception){

        }
    }

    override suspend fun onMove(offset: Offset) {
    }

    override suspend fun onUp() {
    }

}