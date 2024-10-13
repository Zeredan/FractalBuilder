package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.set
import com.example.paintfeature.ViewModels.PaintViewModel
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BesierBuilderOption(vm: PaintViewModel) : PaintViewModel.ControlOption(vm) {
    private var points = mutableListOf<Offset>()


    private infix fun Pair<Offset, Offset>.percent(frac: Float) : Offset{
        return Offset(
            this.first.x + (this.second.x - this.first.x) * frac,
            this.first.y + (this.second.y - this.first.y) * frac,
        )
    }

    fun drawBesier(result: Bitmap){
        Array(1000){ step ->
            val frac = step.toFloat() / 1000
            var wavePoints = points
            while(wavePoints.size > 1){
                wavePoints = Array(wavePoints.size - 1){ ind ->
                    (wavePoints[ind] to wavePoints[ind + 1]) percent frac
                }.toMutableList()
            }
            wavePoints[0]
        }.run{
            this.indices.forEach { ind ->
                if (ind < this.size - 1){
                    drawBresenhamLine(
                        this[ind].x.roundToInt(),
                        this[ind].y.roundToInt(),
                        this[ind + 1].x.roundToInt(),
                        this[ind + 1].y.roundToInt(),
                        Color.RED,
                        result
                    )
                }
            }
        }
    }

    private fun Bitmap.withBesier() : Bitmap{
        var result = this.copy(this.config, true)
        drawBesier(result)
        return result
    }

    override suspend fun onDown(offset: Offset) {
        points += offset
        println(points.joinToString("|||"))
    }

    override suspend fun onMove(offset: Offset) {

    }

    override suspend fun onUp() {

    }

    override suspend fun onDisableControl() {
        if (points.isNotEmpty()){
            vm.addWithMemory(vm.bitmapStack[vm.bitmapStack.lastIndex + vm.memoryIndexOffset].withBesier())
        }
    }

    override suspend fun onEnableControl() {
        points.clear()
    }
}