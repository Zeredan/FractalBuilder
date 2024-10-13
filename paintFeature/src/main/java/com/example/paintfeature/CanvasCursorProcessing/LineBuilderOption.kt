package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.get
import androidx.core.graphics.set
import com.example.paintfeature.ViewModels.PaintViewModel
import kotlin.math.roundToInt

class LineBuilderOption(vm: PaintViewModel) : PaintViewModel.ControlOption(vm) {
    private var positionA: Offset = Offset.Zero
    private var positionB: Offset = Offset.Zero
    private lateinit var initialBitmap: Bitmap


        // Этот код "рисует" все 9 видов отрезков. Наклонные (из начала в конец и из конца в начало каждый), вертикальный и горизонтальный - тоже из начала в конец и из конца в начало, и точку.



    fun Bitmap.withLine(A: Offset, B: Offset) : Bitmap{
        val result = this.copy(this.config, true)
        drawBresenhamLine(A.x.roundToInt(), A.y.roundToInt(), B.x.roundToInt(), B.y.roundToInt(), Color.YELLOW, result)
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
        vm.bitmapStack[vm.bitmapStack.lastIndex] = initialBitmap.withLine(positionA, positionB)
    }

    override suspend fun onUp() {

    }
}