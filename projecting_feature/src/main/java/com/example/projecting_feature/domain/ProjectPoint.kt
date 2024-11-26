package com.example.projecting_feature.domain

import com.example.projecting_feature.model.Point2D
import com.example.projecting_feature.model.Point3D

fun Point3D.projectPoint(cameraZ: Float) : Point2D{
    val scaleXY = cameraZ / (cameraZ - z)
    return Point2D(
        x * scaleXY,
        y * scaleXY
    )
}