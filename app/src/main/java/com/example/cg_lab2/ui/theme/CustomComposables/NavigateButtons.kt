package com.example.cg_lab2.ui.theme.CustomComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun PrevNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
)
{
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onClick()
            }
            .clip(
                object : Shape{
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density
                    ): Outline
                    {
                        val width = size.width
                        val height = size.height

                        return Outline.Generic(
                            Path().apply{
                                this.moveTo(0f, height / 2)
                                this.lineTo(40f, 0f)
                                this.lineTo(80f, 0f)
                                this.lineTo(40f, height / 2)
                                this.lineTo(80f, height)
                                this.lineTo(40f, height)
                                this.lineTo(0f, height / 2)
                            }.run{
                                Path().apply{
                                    repeat(3){
                                        this.addPath(
                                            this@run,
                                            Offset(it * 80f, 0f)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            )
            .then(modifier)
    )
}

@Composable
fun NextNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
)
{
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onClick()
            }
            .clip(
                object : Shape{
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density
                    ): Outline
                    {
                        val width = size.width
                        val height = size.height

                        return Outline.Generic(
                            Path().apply{
                                this.moveTo(width, height / 2)
                                this.lineTo(width - 40f, 0f)
                                this.lineTo(width - 80f, 0f)
                                this.lineTo(width - 40f, height / 2)
                                this.lineTo(width - 80f, height)
                                this.lineTo(width - 40f, height)
                                this.lineTo(width, height / 2)
                            }.run{
                                Path().apply{
                                    repeat(3){
                                        this.addPath(
                                            this@run,
                                            Offset(it * -80f, 0f)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            )
            .then(modifier)
    )
}