package com.khalekuzzamanjustcse.common_ui.graph_editor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import kotlin.math.sqrt


/*
Since we need to move it ,so to easily find the coordinates use the Box
layout because in case of box layout all composable default Offset(0,0)
so we can easily ...
 */
@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun GraphEditorNodeComposablePreview() {
    val onClick: (GraphEditorVisualNode) -> Unit = { id ->
        Log.i("NodeEventOccurred:Clicked-> ", "$id")
    }
    val onDragEnd: (GraphEditorVisualNode) -> Unit = { node ->
        Log.i("NodeEventOccurred:dragged-> ", "$node")
    }
    val size = 64.dp
    val sizePx = size.value * LocalDensity.current.density

    var startPoint by remember {
        mutableStateOf(Offset.Unspecified)
    }
    var endPoint by remember {
        mutableStateOf(Offset.Unspecified)
    }

    val onCanvasDragStart: (Offset) -> Unit = {
        startPoint = it
    }
    val onCanvasDragging: (Offset) -> Unit = { fingerAt ->
        endPoint = fingerAt
    }
    var edgeDrawMode by remember {
        mutableStateOf(true)
    }
    val afterLineDrawn: () -> Unit = {
        Log.i(
            "AfterLineDrawn", "\n" +
                    "startPos:$startPoint\n" +
                    "endPos:$endPoint"
        )

    }


    val node1 = remember {
        GraphEditorVisualNode(
            basicNode = object : GraphBasicNode {
                override val id: Int
                    get() = 1
                override val label: String
                    get() = "10"
                override val position: Offset
                    get() = Offset.Zero
                override val sizePx: Float
                    get() = sizePx
                override val size: Dp
                    get() = size
            },
            size = size,
            showAnchor = false
        )
    }
    val node2 = remember {
        GraphEditorVisualNode(
            basicNode = object : GraphBasicNode {
                override val id: Int
                    get() = 1
                override val label: String
                    get() = "10"
                override val position: Offset
                    get() = Offset(100f, 100f)
                override val sizePx: Float
                    get() = sizePx
                override val size: Dp
                    get() = size
            },
            size = size,
            showAnchor = false
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            MyButton(label = "EdgeDrawMode") {
                edgeDrawMode = !edgeDrawMode
                startPoint = Offset.Unspecified
                endPoint = Offset.Unspecified
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = onCanvasDragStart,
                        onDrag = { change, _ ->
                            onCanvasDragging(change.position)
                        },
                        onDragEnd = afterLineDrawn
                    )
                }
                .drawBehind {
                    if (
                        !edgeDrawMode &&
                        startPoint != Offset.Unspecified
                        && endPoint != Offset.Unspecified
                    ) {
//                        drawLine(
//                            color = Color.Black,
//                            start = startPoint,
//                            end = endPoint,
//                            strokeWidth = 4f
//                        )
                        drawArrow(startPoint, endPoint)
                    }
                }
        ) {
            GraphEditorNodeComposable(node1, enableDrag = edgeDrawMode)
            GraphEditorNodeComposable(node2, enableDrag = edgeDrawMode)
        }

    }


}

@Composable
fun GraphEditorNodeComposable(
    node: GraphEditorVisualNode,
    enableDrag: Boolean = true,
) {
    var offsetWithDragged by remember {
        mutableStateOf(Offset.Zero + node.position)
    }
    val modifier = Modifier
        .size(node.size)
        .offset {
            IntOffset(offsetWithDragged.x.toInt(), offsetWithDragged.y.toInt())
        }
        .then(
            if (enableDrag)
                Modifier
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                offsetWithDragged += dragAmount
                                change.consume()
                                Log.i("Dragging:Node", "")
                            },
                            onDragEnd = {
                                node.onDragEnd(node.copy(position = offsetWithDragged))
                            }
                        )
                    }
                    .clickable { node.onClick(node) }
            else
                Modifier
        )

    Text(
        text = node.label,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Cyan)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

fun DrawScope.drawArrow(
    start: Offset,
    end: Offset,
) {
    val headStart= moveAlongLine(end,start,20f)
    drawLine(
        color = Color.Black,
        start = start,
        end = end,
        strokeWidth = 4f
    )
    rotate(30f, end) {
        drawLine(
            color = Color.Black,
            start = headStart,
            end = end,
            strokeWidth = 2f
        )
    }
    rotate(-30f, end) {
        drawLine(
            color = Color.Black,
            start = headStart,
            end = end,
            strokeWidth = 2f
        )

    }
}

private fun moveAlongLine(
    point1: Offset, point2: Offset, distance:Float,
):Offset{
    val dx=point2.x - point1.x
    val dy=point2.y - point1.y
    val length=sqrt(dx*dx + dy*dy)
    if(length<=0) return point1
    val scale=distance/length
    return Offset((dx*scale)+point1.x,(dy*scale)+point1.y)
}
