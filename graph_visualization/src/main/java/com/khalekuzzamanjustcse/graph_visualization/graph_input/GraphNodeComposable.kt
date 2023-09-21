package com.khalekuzzamanjustcse.graph_visualization.graph_input

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun GraphNodeComposablePreview() {
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    var position by remember {
        mutableStateOf(Offset.Zero)
    }
    val onDrag: (Offset) -> Unit = { dragAmount ->
        offset += dragAmount
    }
    val onPositionChanged: (LayoutCoordinates) -> Unit = {
        position = it.positionInParent()
    }


    //
    //
    //
    Column(modifier = Modifier.fillMaxSize()) {

        GraphNodeComposable(
            label = "5",
            size = 64.dp,
            currentOffset = offset,
            onDrag = onDrag,
            onPositionChanged = onPositionChanged
        )
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GraphNodeComposable(
    label: String,
    size: Dp,
    color: Color = Color.Red,
    currentOffset: Offset = Offset.Zero,
    onDragEnd: () -> Unit = {},
    onDragStart: () -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    onPositionChanged: (LayoutCoordinates) -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val offsetAnimation by animateOffsetAsState(currentOffset, label = "")
    val padding = 8.dp
    Box(
        modifier = Modifier
            .size(size)
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        onDragStart()
                    },
                    onDrag = { change, dragAmount ->
                        onDrag(dragAmount)
                        change.consume()
                    },
                    onDragEnd = {
                        onDragEnd()
                    }
                )
            }
            .onGloballyPositioned {
                onPositionChanged(it)
            }
            .combinedClickable(
                onDoubleClick = onLongClick

            ) {

            }

    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier
                .padding(padding)
                .clip(CircleShape)
                .background(color)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}