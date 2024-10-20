package com.example.imageprocessing.Views

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cg_lab2.R
import com.example.imageprocessing.ViewModels.ImageProcessingViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ColumnScope.TextWithPicture(vm: ImageProcessingViewModel, text: String, bitmap: Bitmap?, onClick: (() -> Unit)? = null){
    Text(
        modifier = Modifier
            .align(Alignment.Start),
        text = text,
        fontSize = 24.sp,
        color = Color.Green
    )
    Image(
        modifier = Modifier
            .run {
                if (bitmap == null)
                    this.border(
                        width = 10.dp,
                        brush = Brush.horizontalGradient(listOf(Color.White, Color.LightGray)),
                        shape = RoundedCornerShape(10.dp)
                    )
                else this
            }
            .fillMaxWidth()
            .padding(20.dp)
            .aspectRatio(1f)
            .run {
                if (onClick != null) this.clickable { onClick() } else this
            },
        painter =
        if (bitmap != null)
            BitmapPainter(bitmap.asImageBitmap())
        else
            if (onClick == null)
                painterResource(R.drawable.img_not_selected)
            else painterResource(R.drawable.image_req_selection),
        contentDescription = null
    )
}