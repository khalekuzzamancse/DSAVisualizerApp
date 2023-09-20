package com.khalekuzzamanjustcse.graph_visualization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.DraggableGraphNode
import com.khalekuzzamanjustcse.graph_visualization.graph_input.Graph
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphBuilderScreenTopAppbar
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GraphPreivew() {
    var addNode by remember {
        mutableIntStateOf(-1)
    }
    var addEdge by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            GraphBuilderScreenTopAppbar(
                title = "Graph",
                onNodeAdded = {
                    try {
                        addNode = it.toInt()
                    } catch (_: Exception) {

                    }
                },
                onAddEdgeClick = {
                    addEdge = true
                })
        }
    ) {
        Column(
            modifier =
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            GraphBuilder(
                addNode = addNode,
                onNodeAdded = { addNode = -1 },
                addEdgeRecentlyTwoLongClickedNode = addEdge,
                onEdgeAdded = {
                    addEdge = false
                },
            )
        }
    }


}


@Composable
fun GraphBuilder(
    addNode: Int = -1,
    onNodeAdded: () -> Unit,
    addEdgeRecentlyTwoLongClickedNode: Boolean = false,
    onEdgeAdded: () -> Unit = {},
) {
    val nodeSize = 64.dp
    val sizePx = nodeSize.value * LocalDensity.current.density
    val graph by remember {
        mutableStateOf(
            Graph(
                DraggableGraphNode(value = 5, sizePx = sizePx),
                DraggableGraphNode(value = 10, sizePx = sizePx)
            )
        )
    }
    var edges = remember { graph.edges }
    val updateEdges: () -> Unit = {
        edges = graph.edges
    }


    if (addNode != -1) {
        graph.addNode(DraggableGraphNode(value = addNode, sizePx = sizePx))
        onNodeAdded()
    }
    if (addEdgeRecentlyTwoLongClickedNode) {
        if (graph.getLastClickedPair().size == 2) {
            graph.addEdge(
                graph.getLastClickedPair().first(),
                graph.getLastClickedPair().last()
            )
            updateEdges()
        }
        onEdgeAdded()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                edges.forEach { (u, v) ->
                    drawLine(
                        color = Color.Black,
                        start = u.getCenter(),
                        end = v.getCenter(),
                        strokeWidth = 4f
                    )
                }

            }

    ) {
        graph.nodes.forEach { node ->
            GraphNodeComposable(
                label = "${node.value}",
                size = nodeSize,
                currentOffset = node.offset.value,
                onDrag = node::onDrag,
                onLongClick = {
                    graph.onNodeLongClick(node)
                }
            )
        }

    }
}