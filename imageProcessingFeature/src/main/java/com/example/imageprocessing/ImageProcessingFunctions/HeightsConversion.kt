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

@RequiresApi(Build.VERSION_CODES.O)
fun Bitmap.toHeights(type: Int) : Bitmap{
    var result = this.copy(this.config, true)
    val matrix = when(type) {
        0 -> listOf(listOf(-1, 0, 0), listOf(0, 0, 0), listOf(0, 0, 1))
        1 -> listOf(listOf(1, 0, 0), listOf(0, 0, 0), listOf(0, 0, -1))
        2 -> listOf(listOf(0, 0, 1), listOf(0, 0, 0), listOf(-1, 0, 0))
        else -> listOf(listOf(0, 0, -1), listOf(0, 0, 0), listOf(1, 0, 0))
    }
    repeat(result.width){ x ->
        repeat(result.height){ y ->
            var sum = 0f
            (-1..1).forEach { j ->
                (-1..1).forEach { i ->
                    try{
                        sum += this[x + j, y + i].toColor().red() * matrix[i + 1][j + 1]
                    }
                    catch (e: Exception){

                    }
                }
            }
            val br = 0.5f + sum / 2
            result[x, y] = Color.rgb(br, br, br)
        }
    }
    return result
}