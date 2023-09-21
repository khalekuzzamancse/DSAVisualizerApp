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
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GraphBuilderPreview() {
    val nodeSize = 64.dp
    val sizePx = nodeSize.value * LocalDensity.current.density
    var onInputMode by remember { mutableStateOf(true) }
    val graph by remember {
        mutableStateOf(
            Graph(
                DraggableGraphNode(value = 0, sizePx = sizePx),
                DraggableGraphNode(value = 1, sizePx = sizePx),
                DraggableGraphNode(value = 2, sizePx = sizePx),
                DraggableGraphNode(value = 3, sizePx = sizePx),
            )
        )
    }
    var bfsIterator by remember { mutableStateOf<Iterator<Any>?>(null) }
    val onNext: () -> Unit = {
        if (bfsIterator == null)
            bfsIterator = bfs(graph.adjacencyListNodeRef).iterator()
        else {
            if (bfsIterator!!.hasNext()) {
                val next = bfsIterator!!.next()
                if (next is Int) {
                    Log.i("TRAVERSING:BFS", "${graph.nodeByRef(next)}")
                    graph.changeNodeColor(next, Color.Blue)
                }
            }
        }


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
                    } catch (_: Exception) {

                    }
                },
                onAddEdgeClick = {
                    if (graph.lastClickedTwoNodeRef.size == 2) {
                        graph.addEdge(
                            graph.lastClickedTwoNodeRef.first(),
                            graph.lastClickedTwoNodeRef.last()
                        )
                    }
                },
                isOnInputMode = onInputMode,
                onInputComplete = {
                    onInputMode = false

                },
                onNextClick = {
                    onNext()
//                    bfs(graph.adjacencyListNodeRef, onNodeProcessing = {
//                        graph.changeNodeColor(it, Color.Blue)
//                        Log.i("TRAVERSING:BFS", "${graph.nodeByRef(it)}")
//                    })

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
                nodeSize = nodeSize,
                edgesRef = graph.edges,
                graph = graph
            )
        }
    }


}

@Composable
fun GraphBuilder(
    nodeSize: Dp,
    edgesRef: List<Pair<Int, Int>>,
    graph: Graph<Int>,
) {

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .drawBehind {
                edgesRef.forEach { (i, j) ->
                    val u = graph.nodeByRef(i)
                    val v = graph.nodeByRef(j)
                    if (u != null && v != null) {
                        drawLine(
                            color = Color.Black,
                            start = u.getCenter(),
                            end = v.getCenter(),
                            strokeWidth = 4f
                        )
                    }

                }

            }


    ) {
        graph.nodes.forEachIndexed { i, node ->
            GraphNodeComposable(
                label = "${node.value}",
                size = nodeSize,
                currentOffset = node.offset.value,
                onDrag = {
                    node.onDrag(it)
                },
                color = node.color.value,
                onLongClick = {
                    graph.onNodeLongClick(i)
                }
            )
        }

    }
}