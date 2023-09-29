package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton


@Preview
@Composable
fun GraphDrawerPreview() {
    val size = 64.dp
    val sizePx = LocalDensity.current.density * size.value
    val recentlyClickedNodes = Array(2) { 0 }
    var clickCount by remember {
        mutableIntStateOf(0)
    }
    var lastTappedLocation by remember {
        mutableStateOf(Offset.Zero)
    }
    var nodes by remember {
        mutableStateOf(
            listOf(
                NodeComposableState(
                    size = size, sizePx = sizePx, label = "10",
                    offset = Offset(10f, 10f)
                ),
                NodeComposableState(
                    size = size, sizePx = sizePx, label = "20",
                    offset = Offset(2 * sizePx + 10f, 10f)
                ),
                NodeComposableState(
                    size = size, sizePx = sizePx, label = "30",
                    offset = Offset(10f, sizePx + 180f)
                ),
                NodeComposableState(
                    size = size, sizePx = sizePx, label = "30",
                    offset = Offset(3 * sizePx + 10f, sizePx + 180f)
                )
            )
        )
    }
    var edges by remember {
        mutableStateOf(
            listOf(
                EdgeComposableState(
                    startPoint = nodes[0].center,
                    endPoint = nodes[3].center,
                ),
                EdgeComposableState(
                    startPoint = nodes[1].center,
                    endPoint = nodes[2].center - Offset(0f, sizePx / 2 - 10f),
                )
            )
        )
    }

    val onDrag: (Int, Offset) -> Unit = { nodeIndex, offset ->
        val updatedNodes = nodes.toMutableList()
        val oldState = nodes[nodeIndex]
        updatedNodes[nodeIndex] = oldState.copy(offset = offset + oldState.offset)
        nodes = updatedNodes
    }
    val onNodeClick: (Int) -> Unit = { nodeIndex ->
        recentlyClickedNodes[clickCount % 2] = nodeIndex
        clickCount++

    }
    val addNode: () -> Unit = {
        val updatedNodes = nodes.toMutableList()
        updatedNodes.add(
            NodeComposableState(
                size = size, sizePx = sizePx,
                label = "33", offset = lastTappedLocation
            )
        )
        nodes = updatedNodes

    }
    val onCanvasTapped: (Offset) -> Unit = {
        lastTappedLocation = it
    }

    val addEdge: () -> Unit = {
        val newEdges = edges.toMutableList()
        newEdges.add(
            EdgeComposableState(
                startPoint = nodes[recentlyClickedNodes[0]].center,
                endPoint = nodes[recentlyClickedNodes[1]].center,
            )
        )
        edges = newEdges
    }
    //Canvas has no initial size so if we make sure you give some initial size
    //other wise tapping and other gestures will nor work
    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            MyButton(label = "AddEdge", onClick = addEdge)
            MyButton(label = "AddNode", onClick = addNode)
        }
        GraphDrawer(
            nodes = nodes,
            edges = edges,
            onClick = onNodeClick,
            onDrag = onDrag,
            onCanvasTapped = onCanvasTapped,
            canvasSize = Pair(500.dp, 500.dp)
        )
    }


}

/*
This composable will know given list of nodes and edges
it just draw them,
it state it:
List<NodeState> represents nodes in graph
List<EdgeState> represents edges
 */

@Composable
fun GraphDrawer(
    modifier: Modifier = Modifier,
    canvasSize: Pair<Dp, Dp>,
    nodes: List<NodeComposableState>,
    edges: List<EdgeComposableState>,
    onDragStart: (Int, Offset) -> Unit = { _, _ -> },
    onDragEnd: (Int) -> Unit = {},
    onClick: (Int) -> Unit = {},
    onDrag: (Int, Offset) -> Unit = { _, _ -> },
    onCanvasTapped: (Offset) -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(canvasSize.first, canvasSize.second)
            .drawBehind {
                edges.forEach { edgeState ->
                    drawEdge(edgeState)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    onCanvasTapped(it)
                }
            }
    ) {
        nodes.forEachIndexed { nodeIndex, node ->
            NodeComposable(
                state = node,
                onDragStart = { offset -> onDragStart(node.id, offset) },
                onDragEnd = { onDragEnd(node.id) },
                onClick = { onClick(node.id) },
                onDrag = { dragAmount -> onDrag(node.id, dragAmount) }
            )
        }

    }
}

