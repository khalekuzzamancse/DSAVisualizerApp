package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.Graph
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable
import com.khalekuzzamanjustcse.graph_visualization.graph_input.NodeValueInputer


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun UndirectedGraphTraversalScreen(
    list: List<Int>,
    edgesRef: List<Pair<Int, Int>>,
    graph: Graph<Int>,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp),
) {
    val nodeSize = 64.dp
//    val sizePx = nodeSize.value * LocalDensity.current.density
//    val graphState = remember {
//        GraphState(sizePx)
//    }

    Log.i("RecompositionScreen", ":UnDirScreen")

    Column(
        modifier = Modifier
            .padding(scaffoldPadding)
    ) {

        Text(list.joinToString(","))
//        CustomDropDownMenu(
//            expanded = graphState.showTraversalMode,
//            options = graphState.traversalOptions,
//            onItemClick = graphState::onTraversalOptionChanged
//        )
        GraphBuilder(
            nodeSize = nodeSize,
            edgesRef = edgesRef,
            graph = graph
        )

//        PopupWithRadioButtons(
//            isOpen = graphState.openNeighboursSelectedPopup,
//            state = graphState.arrayComposableState,
//            onListReOrdered = graphState::onNeighbourOrderSelected
//        )
    }
}

@Composable
fun GraphBuilder(
    nodeSize: Dp,
    edgesRef: List<Pair<Int, Int>>,
    graph: Graph<Int>,
) {
    Log.i("RecompositionScreen", ":GraphBuilder")

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
                            strokeWidth = 4f,
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
                onNodeClick = {
                    graph.onNodeLongClick(i)
                }
            )
        }

    }
}