package com.example.imageprocessing.ImageProcessingFunctions


import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.core.graphics.toColor
/*val c2 = 0.run res@{
    object {
        var a = 0
    }.apply accum@{
        listOf(p2, p3, p4, p5, p6, p7, p8, p9, p2).run {
            this.forEachIndexed { ind, a ->
                try {
                    if (a == 0 && this[ind + 1] == 1) this@accum.a++
                    if (this@accum.a > 1) return@res false
                } catch (e: Exception) {

                }
            }
        }
    }.a == 1
}*/
@RequiresApi(Build.VERSION_CODES.O)
fun Bitmap.toSkeletized() : Bitmap{
    var result = this.copy(this.config, true)
    var stepResult = result.copy(result.config, true)

    var isChanged = true
    while(isChanged) {
        isChanged = false
        (1 until this.width - 1).forEach { x ->
            (1 until this.height - 1).forEach { y ->
                if (result[x, y] == Color.BLACK) {
                    val p2 = if (result[x, y - 1] == Color.BLACK) 1 else 0
                    val p3 = if (result[x + 1, y - 1] == Color.BLACK) 1 else 0
                    val p4 = if (result[x + 1, y] == Color.BLACK) 1 else 0
                    val p5 = if (result[x + 1, y + 1] == Color.BLACK) 1 else 0
                    val p6 = if (result[x, y + 1] == Color.BLACK) 1 else 0
                    val p7 = if (result[x - 1, y + 1] == Color.BLACK) 1 else 0
                    val p8 = if (result[x - 1, y] == Color.BLACK) 1 else 0
                    val p9 = if (result[x - 1, y - 1] == Color.BLACK) 1 else 0

                    val c1 = (p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9) in (2..6)
                    var s = 0
                    listOf(p2, p3, p4, p5, p6, p7, p8, p9, p2).run {
                        this.forEachIndexed { ind, a ->
                            try {
                                if (a == 0 && this[ind + 1] == 1) s++
                            } catch (e: Exception) {

                            }
                        }
                    }
                    val c2 = s == 1
                    val c3 = p2 * p4 * p6 == 0
                    val c4 = p4 * p6 * p8 == 0
                    if (c1 && c2 && c3 && c4){
                        isChanged = true
                        stepResult[x, y] = Color.WHITE
                    }
                }
            }
        }
        //result = stepResult.copy(result.config, true)
        (1 until this.width - 1).forEach { x ->
            (1 until this.height - 1).forEach { y ->
                if (result[x, y] == Color.BLACK) {
                    val p2 = if (result[x, y - 1] == Color.BLACK) 1 else 0
                    val p3 = if (result[x + 1, y - 1] == Color.BLACK) 1 else 0
                    val p4 = if (result[x + 1, y] == Color.BLACK) 1 else 0
                    val p5 = if (result[x + 1, y + 1] == Color.BLACK) 1 else 0
                    val p6 = if (result[x, y + 1] == Color.BLACK) 1 else 0
                    val p7 = if (result[x - 1, y + 1] == Color.BLACK) 1 else 0
                    val p8 = if (result[x - 1, y] == Color.BLACK) 1 else 0
                    val p9 = if (result[x - 1, y - 1] == Color.BLACK) 1 else 0

                    val c1 = (p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9) in (2..6)
                    var s = 0
                    listOf(p2, p3, p4, p5, p6, p7, p8, p9, p2).run {
                        this.forEachIndexed { ind, a ->
                            try {
                                if (a == 0 && this[ind + 1] == 1) s++
                            } catch (e: Exception) {

                            }
                        }
                    }
                    val c2 = s == 1
                    val c5 = p2 * p4 * p8 == 0
                    val c6 = p2 * p6 * p8 == 0
                    if (c1 && c2 && c5 && c6){
                        isChanged = true
                        stepResult[x, y] = Color.WHITE
                    }
                }
            }
        }
        result = stepResult.copy(result.config, true)
    }
    (1 until this.width - 1).forEach { x ->
        (1 until this.height - 1).forEach { y ->
            if (result[x, y] == Color.BLACK) {
                val p2 = if (result[x, y - 1] == Color.BLACK) 1 else 0
                val p3 = if (result[x + 1, y - 1] == Color.BLACK) 1 else 0
                val p4 = if (result[x + 1, y] == Color.BLACK) 1 else 0
                val p5 = if (result[x + 1, y + 1] == Color.BLACK) 1 else 0
                val p6 = if (result[x, y + 1] == Color.BLACK) 1 else 0
                val p7 = if (result[x - 1, y + 1] == Color.BLACK) 1 else 0
                val p8 = if (result[x - 1, y] == Color.BLACK) 1 else 0
                val p9 = if (result[x - 1, y - 1] == Color.BLACK) 1 else 0

                val d1 = (1 - p9) * p4 * p6 == 1
                val d2 = (1 - p5) * p8 * p2 == 1
                val d3 = (1 - p3) * p6 * p8 == 1
                val d4 = (1 - p7) * p2 * p4 == 1

                if (d1 && d2 && d3 && d4) stepResult[x, y] = Color.WHITE
            }
        }
    }

    return stepResult
}