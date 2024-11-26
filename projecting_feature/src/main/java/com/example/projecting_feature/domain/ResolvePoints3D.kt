package com.example.projecting_feature.domain

import com.example.projecting_feature.model.Point3D


internal val Pair<Float, Float>.distance
    get() = this.second - this.first
internal fun resolvePoints3D(
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    offsetZ: Float = 0f,
    rotationX: Float = 0f,
    rotationY: Float = 0f,
    rotationZ: Float = 0f,
    scale: Float = 1f,
    R: Float = 10f,
    H: Float = 10f,
    alphaRange: Pair<Float, Float> = 0f to 2*Math.PI.toFloat(),
    kRange: Pair<Float, Float> = -0.5f to 0.5f,
    alphaN: Int,
    kN: Int
) : List<List<Point3D>> {
    return List(alphaN){ alphaInd ->
        List(kN){ kInd ->
            val (alpha, k) = (alphaRange.first + alphaRange.distance * alphaInd.toFloat() / (alphaN - 1)) to
                    (kRange.first + kRange.distance * kInd.toFloat() / (kN - 1))
            getPointFromAlphaAndK(
                alpha, k, offsetX, offsetY, offsetZ, rotationX, rotationY, rotationZ, scale, R, H
            )
        }
    }
}