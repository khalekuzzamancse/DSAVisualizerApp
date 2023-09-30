package com.khalekuzzamanjustcse.common_ui.graph_editor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


/*
Since we need to move it ,so to easily find the coordinates use the Box
layout because in case of box layout all composable default Offset(0,0)
so we can easily ...
 */
@Preview
@Composable
private fun GraphEditorNodeComposablePreview() {
    val onClick: (Int) -> Unit = { id ->
        Log.i("NodeEventOccurred:Clicked-> ","$id")

    }
    val onDragEnd: (Int, Offset) -> Unit = { id, offset ->
        Log.i("NodeEventOccurred:dragged-> ","($id ,$offset)")
    }
    val node1 = remember {
        GraphEditorNodeState(
            id = 1, size = 50.dp, label = "10",
            position = Offset.Zero,
            onClick = onClick, onDragEnd = onDragEnd
        )
    }
    val node2 = remember {
        GraphEditorNodeState(
            id = 2, size = 50.dp, label = "20",
            position = Offset(100f,100f),
            onClick = onClick, onDragEnd = onDragEnd
        )
    }


    Box(modifier = Modifier.fillMaxSize()) {
        GraphEditorNodeComposable(node1)
        GraphEditorNodeComposable(node2)
    }

}

@Composable
fun GraphEditorNodeComposable(
    node: GraphEditorNodeState,
) {
    var offsetWithDragged by remember {
        mutableStateOf(Offset.Zero + node.position)
    }
    val modifier = Modifier
        .size(node.size)
        .offset {
            IntOffset(offsetWithDragged.x.toInt(), offsetWithDragged.y.toInt())
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, dragAmount ->
                    offsetWithDragged += dragAmount
                    change.consume()
                },
                onDragEnd = {
                    node.onDragEnd(node.id, offsetWithDragged)
                }
            )

        }
        .clickable { node.onClick(node.id) }
    Text(
        text = node.label,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Cyan)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}


