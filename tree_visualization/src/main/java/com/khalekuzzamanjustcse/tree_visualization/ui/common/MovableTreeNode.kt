package com.khalekuzzamanjustcse.tree_visualization.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            offset += Offset(30f, 20f)
        }) {
            Text(text = "Move")
        }
        MovableTreeNode(
            currentOffset = offset,
            label = "10"
        )

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovableTreeNode(
    label: String,
    size: Dp = 100.dp,
    currentOffset: Offset = Offset.Zero,
    color: Color = Color.Red,
    onLongPress: () -> Unit = {}
) {
    val offsetAnimation by animateOffsetAsState(currentOffset, label = "")
    val backgroundColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = 500), label = ""
    )
    val padding = 8.dp
    Box(
        modifier = Modifier
            .size(size)
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            }
            .combinedClickable(
                onLongClick = onLongPress
            ) {

            }
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White),
            modifier = Modifier
                .padding(padding)
                .clip(CircleShape)
//                .background(color)
                .background(backgroundColor)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

