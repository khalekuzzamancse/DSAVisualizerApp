package com.khalekuzzamanjustcse.tree_visualization

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun SwappableElementPreview() {
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    Column(modifier=Modifier.fillMaxSize()) {
        Button(onClick = {
            offset += Offset(30f, 20f)
        }) {
            Text(text = "Move")
        }
        MoveableTreeNode(
            currentOffset=offset,
            label = "10")

    }

}

@Composable
fun MoveableTreeNode(
    label:String,
    size: Dp = 100.dp,
    currentOffset: Offset= Offset.Zero,
) {
    val offsetAnimation by animateOffsetAsState(currentOffset, label = "")
    val padding = 8.dp
    Box(
        modifier = Modifier
            .size(size)
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            }
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White),
            modifier = Modifier
                .padding(padding)
                .clip(CircleShape)
                .background(Color.Red)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

