package com.khalekuzzamanjustcse.graph_visualization.ui_layer

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.common_ui.IconComponent
import com.khalekuzzamanjustcse.common_ui.appbar.TopAppbarData
import com.khalekuzzamanjustcse.common_ui.visual_array.static_array.ArrayComposableState
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.DraggableGraphNode
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.Graph

class UndirectedGraphTraversalViewModel(
    private val nodeSizePX: Float,
) : ViewModel() {
    private val tag = "UnTraversal"
    val state = GraphState(nodeSizePX)

    val inputModeTopAppbar =
        TopAppbarData(
            title = "Graph Traversal",
            elevation = 8.dp,
            navigationIcon = object : IconComponent(label = "Menu", icon = Icons.Filled.Menu) {
                override fun onClick() {
                    Log.i(tag, "Clikced:$label")
                }
            },
            actions = listOf(
                object : IconComponent(label = "Add Node", icon = Icons.Filled.AddCircleOutline) {
                    override fun onClick() {
                        state.openInputMenu()
                    }
                },
                object : IconComponent(label = "Edge Edge", icon = Icons.Filled.LinearScale) {
                    override fun onClick() {
                        state.onAddEdgeIconClick()
                    }
                },
                object : IconComponent(label = "Done", icon = Icons.Filled.DoneOutline) {
                    override fun onClick() {
                        state.onInputFinished()
                    }
                }
            )
        )
}

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
    var showNodeInputPopupMenu = mutableStateOf(false)
    val _list = mutableStateOf(listOf(1,2))
    val list:List<Int>
    get() = _list.value


    var showTraversalMode by mutableStateOf(false)
    fun openInputMenu(){
        val tempList=_list.value.toMutableList()
        tempList.add(99)
        _list.value=tempList
        Log.i("GraphState:","$_list")
    }


    fun onTraversalOptionChanged(index: Int) {
        showTraversalMode=false
    }



    private var neighbourSelectedModeOn by mutableStateOf(true)
    private var iterator by mutableStateOf<Iterator<SimulationState>?>(null)

    var graph by mutableStateOf(Graph<Int>())
    var arrayComposableState by mutableStateOf(
        ArrayComposableState<Neighbours<Int>>(
            emptyList(),
            nodeSizePX
        )
    )
    val openNeighboursSelectedPopup: Boolean
        get() = neighbourSelectedModeOn && showPopupWindow
    val traversalOptions = TraversalOptions.options


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
        showPopupWindow = false
    }



    private fun createGraphDemo() {
        graph = Graph(
            DraggableGraphNode(
                value = 10, sizePx = nodeSizePX,
                offset = mutableStateOf(Offset(100f, 100f))
            ),
            DraggableGraphNode(
                value = 20, sizePx = nodeSizePX,
                offset = mutableStateOf(Offset(nodeSizePX + 100f, 100f))
            ),

            DraggableGraphNode(
                value = 30, sizePx = nodeSizePX,
                offset = mutableStateOf(Offset(nodeSizePX + 200f, nodeSizePX + 100f))
            ),
            DraggableGraphNode(
                value = 40, sizePx = nodeSizePX,
                offset = mutableStateOf(Offset(150f, nodeSizePX + 120f))
            ),
        )
        graph.addEdge(0, 1)
        graph.addEdge(0, 2)
        graph.addEdge(0, 3)

    }


    fun onNextClick() {
        if (iterator == null)
            iterator = bfs(
                adjacencyListOfNodeReference = graph.adjacencyListNodeRef,
                onUnVisitedNeighborsOrderSelected = ::onNeighboursOrderSelected
            ).iterator()
        else {
            if (iterator!!.hasNext()) {
                when (val currentState = iterator!!.next()) {
                    Started -> {

                    }

                    Finished -> {
                    }

                    is Simulating -> {
                        val index = currentState.processingNodeIndex
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

                    else -> {}
                }

            }
        }

    }
}