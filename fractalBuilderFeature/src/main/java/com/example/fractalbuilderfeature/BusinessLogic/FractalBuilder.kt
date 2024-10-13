package com.example.fractalbuilderfeature.BusinessLogic

import android.graphics.Bitmap
import kotlin.math.sqrt

data class Complex(
    var x: Float,
    var y: Float
)
{
    operator fun plus(b: Complex) : Complex {
        return Complex(this.x + b.x, this.y + b.y)
    }
    operator fun minus(b: Complex) : Complex {
        return Complex(this.x - b.x, this.y - b.y)
    }
    operator fun times(b: Complex) : Complex {
        return Complex(this.x * b.x - this.y * b.y, this.x * b.y + this.y * b.x)
    }
    operator fun div(b: Complex) : Complex {
        return (b.x * b.x + b.y * b.y).run denom@{
            Complex(
                (this@Complex.x * b.x + this@Complex.y * b.y) / this,
                (b.x * this@Complex.y - this@Complex.x * b.y) / this
            )
        }
    }
    val modulo : Float
        get(){
            return sqrt(x*x.toDouble() + y*y.toDouble()).toFloat()
        }
}

fun formula(Zk: Complex, Z0 : Complex) : Complex
{
    return Zk*Zk*Zk*Zk + Z0
    //return Zk*Zk*Zk + Complex(-0.7f, 0.27015f)
}

fun getPointIterations(Z0: Complex) : Int{
    if (Z0.modulo > 2f) return 0
    var Zk = Z0
    var iterationCount = 0
    while(iterationCount < 200 && Zk.modulo < 2f)
    {
        Zk = formula(Zk, Z0)
        ++iterationCount
    }
    return iterationCount
}

fun <T> createBitmap(
    colors: Array<Array<T>>,
    transform: (T) -> Int,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888,
): Bitmap
{
    val intColors = colors
        .flatten()
        .map(transform)
        .toIntArray()
    val width = colors.firstOrNull()?.size ?: 0
    val height = colors.size

    return Bitmap.createBitmap(
        intColors,
        width,
        height,
        config,
    )
}