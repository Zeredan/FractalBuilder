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
suspend fun Bitmap.toGray(): Bitmap {
    var result = this.copy(this.config, true)
    repeat(result.width){ x ->
        repeat(result.height){ y ->
            val (r, g, b) = this[x, y].toColor()
            val fV = 0.299f * r + 0.587f * g + 0.114f * b
            result[x, y] = Color.rgb(fV, fV, fV)
        }
    }
    return result
}