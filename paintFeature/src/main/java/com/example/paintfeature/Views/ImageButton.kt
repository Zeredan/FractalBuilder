package com.example.paintfeature.Views

import androidx.annotation.IdRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cg_lab2.R
import com.example.paintfeature.ViewModels.PaintViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageButton(imageResId: Int, changeSelection: Boolean = true, vm: PaintViewModel, snackbarHostState: SnackbarHostState, message: String, onClick: () -> Unit){
    var coroutineScope = rememberCoroutineScope()
    val isActive = vm.selectedOption == imageResId
    Image(
        modifier = Modifier
            .border(
                if (isActive) 3.dp else 1.dp,
                color = animateColorAsState(if (isActive) Color.Green else Color(255, 255, 200), tween(600)).value,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
            .size(45.dp)
            .combinedClickable(
                onClick = {
                    if (changeSelection) vm.selectedOption = imageResId
                    onClick()
                },
                onLongClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            ),
        painter = painterResource(id = imageResId),
        contentDescription = null
    )
}