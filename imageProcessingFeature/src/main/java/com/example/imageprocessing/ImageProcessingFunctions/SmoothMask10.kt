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
fun Bitmap.toSmooth10() : Bitmap{
    var result = this.copy(this.config, true)
    val matrix = listOf(listOf(1,1,1), listOf(1,2,1), listOf(1,1,1))
    repeat(result.width){ x ->
        repeat(result.height){ y ->
            var counter = 0
            var sum = 0f
            (-1..1).forEach { j ->
                (-1..1).forEach { i ->
                    try{
                        sum += this[x + j, y + i].toColor().red() * matrix[i + 1][j + 1]
                        counter += matrix[i + 1][j + 1]
                    }
                    catch (e: Exception){

                    }
                }
            }
            val br = sum / counter
            result[x, y] = Color.rgb(br, br, br)
        }
    }
    return result
}