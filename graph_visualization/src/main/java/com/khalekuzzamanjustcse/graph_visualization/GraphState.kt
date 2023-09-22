package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
    var isInputMode by mutableStateOf(true)
    private var showPopupWindow by mutableStateOf(false)
    private var unVisitedNeighboursOrder by mutableStateOf(emptyList<Int>())

    private var neighbourSelectedModeOn by mutableStateOf(true)
    private var iterator by mutableStateOf<Iterator<SimulationState>?>(null)

    var graph by mutableStateOf(Graph<Int>())
    var arrayComposableState by mutableStateOf(ArrayComposableState<Neighbours<Int>>(emptyList(), nodeSizePX))
    val openNeighboursSelectedPopup: Boolean
        get() = neighbourSelectedModeOn && showPopupWindow


    init {
        createGraphDemo()
    }

    fun onAddNodeRequest(it: String) {
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

    fun onInputFinished() {
        isInputMode = false
    }

    fun onNeighbourSelectedModeChangeRequest() {
        neighbourSelectedModeOn = !neighbourSelectedModeOn
    }


    private fun onNeighboursOrderSelected(): List<Int> {
        return if (neighbourSelectedModeOn)
            unVisitedNeighboursOrder
        else
            emptyList()
    }

    fun onNeighbourOrderSelected() {
        unVisitedNeighboursOrder = arrayComposableState
            .cellsCurrentElements
            .filterNotNull()
            .map { neighbour -> neighbour.nodeIndexRef }
        Log.i(
            "RecordList",
            "${
                arrayComposableState.cellsCurrentElements.filterNotNull()
                    .map { neighbour -> neighbour.nodeIndexRef }
            }"
        )
        showPopupWindow = false
    }

    private fun createGraphDemo() {
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


    val onNext: () -> Unit = {
        if (iterator == null)
            iterator = bfs(
                adjacencyListOfNodeReference = graph.adjacencyListNodeRef,
                onUnVisitedNeighborsOrderSelected = ::onNeighboursOrderSelected
            ).iterator()
        else {
            if (iterator!!.hasNext()) {
                when (val currentState = iterator!!.next()) {
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
                            Log.i("TRAVERSING:SelectChild", "$unVisitedNeighbors")

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

                        if (unVisitedNeighbors.size>1) {
                            arrayComposableState =
                                ArrayComposableState(
                                    list = unVisitedNeighbors,
                                    cellSizePx = nodeSizePX
                                )
                            showPopupWindow = true
                        }

                    }

                    Paused -> {

                    }
                }

            }
        }

    }
}