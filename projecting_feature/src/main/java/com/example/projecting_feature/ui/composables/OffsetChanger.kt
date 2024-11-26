package com.example.projecting_feature.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cg_lab2.R

@Composable
internal fun OffsetChanger(
    modifier: Modifier = Modifier,
    text: String,
    value: Float,
    deltaValue: Float,
    onChanged: (Float) -> Unit
)
{
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 2.dp,
                color = Color(0, 160, 0),
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.Green)
            .padding(10.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    )
    {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable { onChanged(value - deltaValue) }
                .padding(10.dp)
                .fillMaxHeight()
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.decrease),
            contentDescription = null
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(text)
            Text(value.toString())
        }
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable { onChanged(value + deltaValue) }
                .padding(10.dp)
                .fillMaxHeight()
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.increase),
            contentDescription = null
        )
    }
}