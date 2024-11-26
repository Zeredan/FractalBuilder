package com.example.projecting_feature.domain

import android.R.attr
import com.example.projecting_feature.model.Point3D
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


infix fun Point3D.rotated(rotation: List<Float>) : Point3D{
    val (rX, rY, rZ) = rotation

// Поворот вокруг X
    val x1 = x
    val y1 = y * cos(rX) - z * sin(rX);
    val z1 = y * sin(rX) + z * cos(rX);

    // Поворот вокруг Y
    val x2 = x1 * cos(rY) + z1 * sin(rY);
    val y2 = y1
    val z2 = -x1 * sin(rY) + z1 * cos(rY);

    // Поворот вокруг Z
    val x3 = x2 * cos(rZ) - y2 * sin(rZ);
    val y3 = x2 * sin(rZ) + y2 * cos(rZ);
    val z3 = z2
    return Point3D(
        x3,
        y3,
        z3
    )
}
fun getPointFromAlphaAndK(
    alpha: Float,
    k: Float,
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    offsetZ: Float = 0f,
    rotationX: Float = 0f,
    rotationY: Float = 0f,
    rotationZ: Float = 0f,
    scale: Float = 1f,
    R: Float = 10f,
    H: Float = 10f,
) : Point3D{
    val initialPoint = Point3D(
        (R * (1 + abs(sin(2f*k*Math.PI + 0.5f*alpha))) * sin(alpha)).toFloat(),
        (R * (1 + abs(sin(2f*k*Math.PI + 0.5f*alpha))) * cos(alpha)).toFloat(),
        H * k
    )
    println("curK: ${k}")
    val scaledPoint = initialPoint.copy(
        x = initialPoint.x * scale,
        y = initialPoint.y * scale,
        z = initialPoint.z * scale,
    )
    val rotatedPoint = scaledPoint rotated listOf(rotationX, rotationY, rotationZ)
    return rotatedPoint.copy(
        x = rotatedPoint.x + offsetX,
        y = rotatedPoint.y + offsetY,
        z = rotatedPoint.z + offsetZ
    )
}