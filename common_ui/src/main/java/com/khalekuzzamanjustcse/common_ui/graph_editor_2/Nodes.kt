package com.khalekuzzamanjustcse.common_ui.graph_editor_2


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphBasicNode
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorVisualNode
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import kotlin.math.cos
import kotlin.math.sin


/*
Since we need to move it ,so to easily find the coordinates use the Box
layout because in case of box layout all composable default Offset(0,0)
so we can easily ...
 */
@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun GraphEditorNodeComposablePreview2() {
    val size = 64.dp
    val sizePx = size.value * LocalDensity.current.density

    var clickedNodes by remember {
        mutableStateOf(setOf<Int>())

    }
    val onClick: (GraphEditorVisualNode) -> Unit = { node ->
        clickedNodes = clickedNodes + node.id
    }


    val node = remember {
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
            showAnchor = false,
            onClick = onClick
        )
    }

    var nodes by remember {
        mutableStateOf(
            listOf(
                node,
                node.copy(id = 2, label = "20"),
                node.copy(id = 3, label = "30")
            )
        )
    }


    val onDragEnd: (GraphEditorVisualNode) -> Unit = { dragged ->
        Log.i("NodeEventOccurred:dragged-> ", "$dragged")
    }


    var twoPoints by remember {
        mutableStateOf(setOf<Pair<Int, Offset>>())
    }
    var edges by remember {
        mutableStateOf(listOf<Pair<Offset, Offset>>())
    }


    var edgeDrawMode by remember {
        mutableStateOf(true)
    }
    //show the anchor points of clicked nodes
    if (edgeDrawMode) {
        nodes = nodes.map { _node ->
            if (clickedNodes.contains(_node.id)) _node.copy(showAnchor = true)
            else _node
        }
    }



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            MyButton(label = "EdgeDrawMode") {
                edgeDrawMode = !edgeDrawMode
            }
            MyButton(label = "HideAnchor") {
                nodes = nodes.map {
                    it.copy(showAnchor = false)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    edges.forEach {
                        drawEdge(it.first, it.second, hasDirection = false)
                    }
                }
        ) {
            nodes.forEach {
                GraphEditorNodeComposable(
                    it, enableDrag = edgeDrawMode,
                    onAnchorPointClick = { node, offset ->
                        Log.i("AnchorPointClick: ", "$offset")
                        twoPoints = twoPoints + Pair(node.id, offset)
                        if (twoPoints.size > 2)
                            twoPoints = twoPoints.toMutableSet().drop(1).toSet()
                        if (twoPoints.size == 2) {
                            edges = edges + Pair(twoPoints.first().second, twoPoints.last().second)
                            twoPoints = emptySet()
                        }

                    }
                )
            }
        }
    }


}

@Composable
fun GraphEditorNodeComposable(
    node: GraphEditorVisualNode,
    enableDrag: Boolean = true,
    onAnchorPointClick: (GraphEditorVisualNode, Offset) -> Unit = { _, _ -> }
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
                            },
                            onDragEnd = {
                                node.onDragEnd(node.copy(position = offsetWithDragged))
                            }
                        )

                    }
            else
                Modifier
                    .clickable {
                        node.onClick(node)
                    }
        )
    Box(
        modifier = modifier
            .size(node.size)
    ) {
        Text(
            text = node.label,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Cyan)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )

        if (node.showAnchor) {
            AnchorPoints(node.size) {
                onAnchorPointClick(node, it + offsetWithDragged)
            }
        }


    }


}


@Composable
private fun AnchorPoints(
    nodeSize: Dp,
    onAnchorPointClick: (Offset) -> Unit,
) {
    val density = LocalDensity.current.density
    val nodeSizePx = nodeSize.value * density
    val anchorSize = nodeSize / 10
    val anchorSizePx = anchorSize.value * density
    val radius = nodeSizePx / 2
    val center = Offset(nodeSizePx / 2, nodeSizePx / 2)
    val anchorCenter = Offset(anchorSizePx / 2, anchorSizePx / 2)

    for (i in 1..10) {
        val angleDegree = 36.0f * i
        val offset = calculateCoordinates(angleDegree, radius, center)
        AnchorPoint(
            offset = offset - anchorCenter,
            size = anchorSize
        ) {
            onAnchorPointClick(it + anchorCenter)
        }
    }
}

@Composable
private fun AnchorPoint(
    offset: Offset,
    size: Dp,
    onTapped: (Offset) -> Unit = {},
) {


    Box(
        modifier = Modifier
            .size(size)
            .offset {
                val (x, y) = offset
                IntOffset(x.toInt(), y.toInt())
            }
            .clip(CircleShape)
            .background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTapped(offset) }
                )

            }


    )


}
fun DrawScope.drawEdge(
    start: Offset,
    end: Offset,
    controlPoint: Offset? = null,
    hasDirection: Boolean,
    onPathCreated:(Path)->Unit ={}
) {

    var midpoint = Offset((start.x + end.x) / 2, (start.y + end.y) / 2)
    if (controlPoint != null)
        midpoint = controlPoint
    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticBezierTo(midpoint.x, midpoint.y, end.x, end.y)
        onPathCreated(this)
    }
    val pathMeasured = PathMeasure().apply {
        setPath(path, false)
    }
    val headStart = pathMeasured.getPosition(pathMeasured.length - 25)


    drawPath(
        path = path, color = Color.Black, style = Stroke(3f)
    )
    if (hasDirection) {
        rotate(30f, end) {
            drawLine(
                color = Color.Black,
                start = headStart,
                end = end,
                strokeWidth = 3f
            )
        }
        rotate(-30f, end) {
            drawLine(
                color = Color.Black,
                start = headStart,
                end = end,
                strokeWidth = 3f
            )

        }
    }


}


fun calculateCoordinates(
    angleDegree: Float, radius: Float,
    center: Offset
): Offset {
    val angleRadian = Math.toRadians(angleDegree.toDouble())
    val x = center.x + radius * cos(angleRadian).toFloat()
    val y = center.y + radius * sin(angleRadian).toFloat()
    return Offset(x, y)
}