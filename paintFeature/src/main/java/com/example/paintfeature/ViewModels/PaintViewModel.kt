package com.example.paintfeature.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.cg_lab2.R

class PaintViewModel : ViewModel() {
    private inner class ControlOption(
        var onDown: (Offset) -> Unit,
        var onMove: (Offset) -> Unit,
        var onUp: () -> Unit,
    )

    var selectedOption by mutableStateOf<Int?>(null)

    private val options = mapOf(
        R.drawable.line to ControlOption(
            onDown = {

            },
            onMove = {

            },
            onUp = {

            }
        )
    )

    fun canvasCursorDown(offset: Offset){

    }
    fun canvasCursorMove(offset: Offset){

    }
    fun canvasCursorUp(){

    }
}