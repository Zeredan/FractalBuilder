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
fun Bitmap.toMonochromatic(brightnessTreshold: Int) : Bitmap{
    var result = this.copy(this.config, true)
    repeat(result.width){ x ->
        repeat(result.height){ y ->
            val (r, g, b) = this[x, y].toColor()
            var light = if (r < brightnessTreshold / 255f) 0f else 1f
            result[x, y] = Color.rgb(light, light, light)
        }
    }
    return result
}