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
import com.khalekuzzamanjustcse.graph_visualization.swap_able_array.ArrayComposableState


data class Neighbours<T>(
    val nodeIndexRef: Int,
    val nodeValue: T
) {
    override fun toString(): String {
        return "$nodeValue"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GraphBuilderPreview() {
    val nodeSize = 64.dp
    val sizePx = nodeSize.value * LocalDensity.current.density
    var onInputMode by remember { mutableStateOf(true) }
    var showPicker by remember {
        mutableStateOf(false)
    }

    var unVisitedNeigboursOrder by remember {
        mutableStateOf(emptyList<Int>())
    }
    var neighbourSelectedModeOn by remember {
        mutableStateOf(true)
    }
    val onNeigboursOrderSelected: () -> List<Int> = {
        if (neighbourSelectedModeOn)
            unVisitedNeigboursOrder
        else
            emptyList()
    }

    val graph by remember {
        mutableStateOf(
            Graph(
                DraggableGraphNode(value = 10, sizePx = sizePx),
                DraggableGraphNode(value = 20, sizePx = sizePx),
                DraggableGraphNode(value = 30, sizePx = sizePx),
                DraggableGraphNode(value = 40, sizePx = sizePx),
            )
        )
    }
    graph.addEdge(0, 1)
    graph.addEdge(0, 2)
    graph.addEdge(0, 3)

    var state = remember {
        ArrayComposableState(list = emptyList<Neighbours<Int>>(), cellSizePx = sizePx)
    }

    var bfsIterator by remember { mutableStateOf<Iterator<SimulationState>?>(null) }
    val onNext: () -> Unit = {
        if (bfsIterator == null)
            bfsIterator = bfs(
                adjacencyListOfNodeReference = graph.adjacencyListNodeRef,
                onUnVisitedNeighborsOrderSelected = onNeigboursOrderSelected
            ).iterator()
        else {
            if (bfsIterator!!.hasNext()) {
                when (val currentState = bfsIterator!!.next()) {
                    Started -> {
                        Log.i("TRAVERSING:BFS", "Started")
                    }

                    Finished -> {
                        Log.i("TRAVERSING:BFS", "Finished")
                    }

                    is Simulating -> {
                        val index = currentState.processingNodeIndex
                        Log.i("TRAVERSING:BFS", "${graph.nodeByRef(index)}")
                        graph.changeNodeColor(index, Color.Blue)
                    }

                    is SelectChild -> {
                        val unVisitedNeighbors = mutableListOf<Neighbours<Int>>()
                        currentState.unVisitedNeighboursRef.forEach { nodeRef ->
                            val node = graph.nodeByRef(nodeRef)
                            if (node != null) {
                                unVisitedNeighbors.add(
                                    Neighbours(
                                        nodeIndexRef = nodeRef,
                                        nodeValue = node.value
                                    )
                                )
                            }

                        }
                        currentState.unVisitedNeighboursRef
                            .map { graph.nodeByRef(it) }
                            .filterNotNull()
                            .mapIndexed { index, v ->
                                Neighbours(
                                    nodeIndexRef = index,
                                    nodeValue = v
                                )
                            }

                        if (unVisitedNeighbors.isNotEmpty()) {
                            state =
                                ArrayComposableState(list = unVisitedNeighbors, cellSizePx = sizePx)
                            showPicker = true
                        }

                    }

                    Paused -> {

                    }
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
                },
                onNeighbourSelectedModeClick = {
                    neighbourSelectedModeOn=!neighbourSelectedModeOn
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
            PopupWithRadioButtons(
                isOpen = neighbourSelectedModeOn&&showPicker,
                state = state
            ) {
                unVisitedNeigboursOrder = state
                    .cellsCurrentElements
                    .filterNotNull()
                    .map { neighbour -> neighbour.nodeIndexRef }
                Log.i(
                    "RecoredList",
                    "${
                        state.cellsCurrentElements.filterNotNull()
                            .map { neighbour -> neighbour.nodeIndexRef }
                    }"
                )
                showPicker = false
            }
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