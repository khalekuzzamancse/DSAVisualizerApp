package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.DraggableGraphNode
import com.khalekuzzamanjustcse.graph_visualization.graph_input.Graph
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphBuilderScreenTopAppbar
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNode
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GraphPreview() {

    val nodeSize = 64.dp
    val sizePx = nodeSize.value * LocalDensity.current.density
    var onInputMode by remember { mutableStateOf(true) }
    val graph by remember {
        mutableStateOf(
            Graph(
                DraggableGraphNode(value = 5, sizePx = sizePx),
                DraggableGraphNode(value = 10, sizePx = sizePx)
            )
        )
    }
    var edges by remember { mutableStateOf(graph.getAllEdges()) }
    var nodes by remember { mutableStateOf(graph.getAllNodes()) }

    val updateEdges: () -> Unit = {
        edges = graph.getAllEdges()
    }


    Scaffold(
        topBar = {
            GraphBuilderScreenTopAppbar(
                title = "Graph",
                onNodeAdded = {
                    try {
                        val value = it.toInt()
                        val node = DraggableGraphNode(value = value, sizePx = sizePx)
                        graph.addNode(node)
                        //updated nodes
                        nodes = graph.getAllNodes()

                    } catch (_: Exception) {

                    }
                },
                onAddEdgeClick = {
                    if (graph.getLastClickedPair().size == 2) {
                        graph.addEdge(
                            graph.getLastClickedPair().first(),
                            graph.getLastClickedPair().last()
                        )
                        Log.i("GraphJDJDKJKD", "${graph.getLastClickedPair().map { it.value }}")
                        updateEdges()
                    }
                },
                isOnInputMode = onInputMode,
                onInputComplete = {
                    onInputMode = false

                }
            )
        }
    ) {
        Column(
            modifier =
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            GraphBuilder(
                nodeSize=nodeSize,
                edges = edges,
                nodes = nodes,
                onNodeClick ={node->
                    graph.onNodeLongClick(node)
                    updateEdges()
                }
            )
        }
    }


}

@Composable
fun GraphBuilder(
    nodeSize: Dp,
    nodes: List<GraphNode<Int>>,
    edges: List<Pair<GraphNode<Int>, GraphNode<Int>>>,
    onNodeClick:(GraphNode<Int>)->Unit,
) {

    Box(
        modifier = Modifier
            .padding(8.dp)
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
        nodes.forEach { node ->
            GraphNodeComposable(
                label = "${node.value}",
                size = nodeSize,
                currentOffset = node.offset.value,
                onDrag = {
                    node.onDrag(it)
                },
                onLongClick = {
                   onNodeClick(node)
                }
            )
        }

    }
}