package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.Graph
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphBuilderScreenTopAppbar
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GraphBuilderPreview() {
    val nodeSize = 64.dp
    val sizePx = nodeSize.value * LocalDensity.current.density
    val graphState = remember {
        GraphState(sizePx)
    }

    Scaffold(
        topBar = {
            GraphBuilderScreenTopAppbar(
                title = "Graph",
                onNodeAdded = graphState::onAddNodeRequest,
                onAddEdgeClick = graphState::onAddEdgeIconClick,
                isOnInputMode = graphState.isInputMode,
                onInputComplete = graphState::onInputFinished,
                onNextClick = { graphState.onNext() },
                onNeighbourSelectedModeClick = graphState::onNeighbourSelectedModeChangeRequest,
                traversalOptions = TraversalOptions.options,
                onTraversalOptionsSelected = {
                    graphState.onTraversalOptionChanged(TraversalOptions.getOption(it))
                },
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
                edgesRef = graphState.graph.edges,
                graph = graphState.graph
            )
            PopupWithRadioButtons(
                isOpen = graphState.openNeighboursSelectedPopup,
                state = graphState.arrayComposableState,
                onListReOrdered = graphState::onNeighbourOrderSelected
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
                onNodeClick = {
                    graph.onNodeLongClick(i)
                }
            )
        }

    }
}