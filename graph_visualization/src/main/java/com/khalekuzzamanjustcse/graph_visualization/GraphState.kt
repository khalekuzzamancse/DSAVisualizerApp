package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.DraggableGraphNode
import com.khalekuzzamanjustcse.graph_visualization.graph_input.Graph
import com.khalekuzzamanjustcse.graph_visualization.swap_able_array.ArrayComposableState

data class Neighbours<T>(
    val nodeIndexRef: Int,
    val nodeValue: T
) {
    override fun toString(): String {
        return "$nodeValue"
    }
}

data class GraphState(
    val nodeSizePX: Float
) {
    var inputModeOn by mutableStateOf(true)
    var showPicker by mutableStateOf(false)
    var unVisitedNeigboursOrder by mutableStateOf(emptyList<Int>())

    var neighbourSelectedModeOn by mutableStateOf(true)
    var bfsIterator by mutableStateOf<Iterator<SimulationState>?>(null)
    var graph by mutableStateOf(Graph<Int>())
    var state by mutableStateOf(ArrayComposableState<Neighbours<Int>>(emptyList(), nodeSizePX))
    val openNeighboursSelectedPopup: Boolean
        get() = neighbourSelectedModeOn && showPicker


    init {
        createGraphDemo()
    }

    fun onAddNode(it: String) {
        try {
            val value = it.toInt()
            val node = DraggableGraphNode(value = value, sizePx = nodeSizePX)
            graph.addNode(node)

        } catch (_: Exception) {
        }
    }

    fun onAddEdgeIconClick() {
        if (graph.lastClickedTwoNodeRef.size == 2) {
            graph.addEdge(
                graph.lastClickedTwoNodeRef.first(),
                graph.lastClickedTwoNodeRef.last()
            )
        }
    }

    fun onInputComplete() {
        inputModeOn = false
    }

    fun onNeighbourSelectedModeChangeRequest() {
        neighbourSelectedModeOn = !neighbourSelectedModeOn
    }


    fun onNeigboursOrderSelected(): List<Int> {
        return if (neighbourSelectedModeOn)
            unVisitedNeigboursOrder
        else
            emptyList()
    }

    fun onNeighbourOrderSelected() {
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

    fun createGraphDemo() {
        graph = Graph(
            DraggableGraphNode(value = 10, sizePx = nodeSizePX),
            DraggableGraphNode(value = 20, sizePx = nodeSizePX),
            DraggableGraphNode(value = 30, sizePx = nodeSizePX),
            DraggableGraphNode(value = 40, sizePx = nodeSizePX),
        )
        graph.addEdge(0, 1)
        graph.addEdge(0, 2)
        graph.addEdge(0, 3)

    }

    fun updateState(list: List<Neighbours<Int>>) {
        state = ArrayComposableState(list = list, cellSizePx = nodeSizePX)
    }


    val onNext: () -> Unit = {
        if (bfsIterator == null)
            bfsIterator = bfs(
                adjacencyListOfNodeReference = graph.adjacencyListNodeRef,
                onUnVisitedNeighborsOrderSelected = ::onNeigboursOrderSelected
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
                                ArrayComposableState(
                                    list = unVisitedNeighbors,
                                    cellSizePx = nodeSizePX
                                )
                            showPicker = true
                        }

                    }

                    Paused -> {

                    }
                }

            }
        }

    }
}