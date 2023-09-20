package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.DraggableGraphNode
import com.khalekuzzamanjustcse.graph_visualization.graph_input.Graph
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable

@Preview
@Composable
fun GraphPreivew() {
    var addNode by remember {
        mutableIntStateOf(-1)
    }
    var addEdge by remember {
        mutableStateOf(Pair(-1, -1))
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            addNode = 20
        }) {
            Text("AddNode")
        }
        GraphBuilder(
            addNode = addNode,
            onNodeAdded = { addNode = -1 },
        )
    }

}


@Composable
fun GraphBuilder(
    addNode: Int = -1,
    onNodeAdded: () -> Unit,
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
    graph.addEdge(graph.nodes[0], graph.nodes[1])


    if (addNode != -1) {
        graph.addNode(DraggableGraphNode(value = addNode, sizePx = sizePx))
        onNodeAdded()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                graph.edges.forEach { (u, v) ->
                    val nodeU = graph.getNode(u)
                    val nodeV = graph.getNode(v)
                    if (nodeU != null && nodeV != null) {
                        drawLine(
                            color = Color.Black,
                            start = nodeU.getCenter(),
                            end = nodeV.getCenter(),
                            strokeWidth = 4f
                        )
                    }
                }
            }

    ) {
        graph.nodes.forEach { node ->
            GraphNodeComposable(
                label = "${node.value}",
                size = nodeSize,
                currentOffset = node.offset.value,
                onDrag = {
                    node.onDrag(it)
                },
                onPositionChanged = {
                },
                onLongClick = {
                }
            )
        }

    }
}