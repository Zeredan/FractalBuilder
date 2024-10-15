package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.get
import androidx.core.graphics.set
import com.example.paintfeature.ViewModels.PaintViewModel
import java.util.Stack
import kotlin.math.roundToInt

class FillColorBarkBeetle(vm: PaintViewModel) : PaintViewModel.ControlOption(vm) {

    fun fillColor(center: Offset, result: Bitmap){
        var s = Stack<Offset>()
        s.push(center)
        while(s.isNotEmpty()) {
            val cur = s.pop()
            try {
                result[cur.x.roundToInt(), cur.y.roundToInt()] = Color.CYAN
            } catch (e: Exception) {

            }
            listOf(
                cur.copy(x = cur.x - 1),
                cur.copy(x = cur.x + 1),
                cur.copy(y = cur.y - 1),
                cur.copy(y = cur.y + 1),
            ).filter {
                result[it.x.roundToInt(), it.y.roundToInt()] == Color.BLACK
            }.forEach {
                s.push(it)
            }
        }
    }

    fun Bitmap.withFillColor(center: Offset) : Bitmap {
        var result = this.copy(this.config, true)
        fillColor(center, result)
        return result
    }
    override suspend fun onDown(offset: Offset) {
        try {
            vm.addWithMemory(
                vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset].withFillColor(
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