package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import kotlin.concurrent.timer
import kotlin.random.Random

/*
Dragging is an costly operation it execute  frequently
so used the drag amount internally and when drag end then notify the user
that drag is ended

 */
@Preview
@Composable
private fun NodeCompose2Preview() {

    val size = 64.dp
    val sizePx = LocalDensity.current.density * size.value
    var state by remember {
        mutableStateOf(NodeComposableState(size = size, sizePx = sizePx, label = "20"))
    }
    val randomColor: () -> Color = {
        val random = Random.Default
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        Color(red, green, blue)
    }
    val blink: () -> Unit = {
        timer(period = 300) {
            state = state.copy(color = randomColor(), backgroundColor = randomColor())
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            MyButton(label = "ChangeState") {
                state = state.copy(
                    offset = Offset(200f, 200f),
                    backgroundColor = Color.Magenta,
                    clickable = false,
                    draggable = false
                )
            }
            MyButton(label = "Blink", onClick = blink)
        }
        NodeComposable(
            state = state,
            onDragStart = {},
            onClick = { },
            onDragEnd = { state = state.copy(offset = it)}
        )
    }


}

@Composable
fun NodeComposable(
    state: NodeComposableState,
    onDragStart: (Offset) -> Unit,
    onDragEnd: (Offset) -> Unit,
    onClick: () -> Unit
) {
    var offsetWithDragged by remember {
        mutableStateOf(Offset.Zero + state.offset)
    }
    // val offsetAnimation by animateOffsetAsState(state.offset, label = "")
//    val offsetAnimation by animateOffsetAsState(state.offset, label = "")
    val nodeColor by animateColorAsState(targetValue = state.color, label = "")
    val backgroundColor by animateColorAsState(targetValue = state.backgroundColor, label = "")


    val modifier = Modifier
        .size(state.size)
        .offset {
//            IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            IntOffset(offsetWithDragged.x.toInt(), offsetWithDragged.y.toInt())
        }
        .then(
            if (state.draggable) {
                Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = onDragStart,
                        onDrag = { change, dragAmount ->
                            offsetWithDragged += dragAmount
                            change.consume()
                        },
                        onDragEnd = {
                            onDragEnd(offsetWithDragged)
                        }
                    )
                }
            } else {
                Modifier
            }
        )
        .then(
            if (state.clickable) {
                Modifier.clickable { onClick() }
            } else {
                Modifier
            }
        )
        .background(backgroundColor)


    Box(modifier = modifier) {
        Text(
            text = state.label,
            color = state.textColor,
            modifier = Modifier
                //  .padding(padding)
                .clip(CircleShape)
                .background(nodeColor)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}



