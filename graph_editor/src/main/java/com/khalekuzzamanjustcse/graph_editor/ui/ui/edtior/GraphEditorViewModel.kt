package com.khalekuzzamanjustcse.graph_editor.ui.ui.edtior

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.data_layer.repoisitory.EditDatabase
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorVisualEdgeImp
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorEdgeManger
import com.khalekuzzamanjustcse.graph_editor.ui.ui.node.GraphEditorNode
import com.khalekuzzamanjustcse.graph_editor.ui.ui.node.GraphEditorNodeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/*
 onTap:
 Tapping support two operations:
 1:Add a node to a to tapped location.
 2:Select a control point of an edge
 to distinguish which operation is running ,we have to keep track of the operation.
 if the user clicked to add node button then change mode=AddNode
 when onTap() executed if mode==AddNode then add the node,otherwise do something else.
 after tap if mode==AddNode is on then change the mode.
 */
enum class GraphEditorMode {
    NodeAdd, EdgeAdd, None
}


data class GraphEditorManger(
    private val density: Float,
    private val context: Context,
) {
    private val nodeManger = GraphEditorNodeManager(density)
    private val edgeManger = GraphEditorEdgeManger()
    val edges: StateFlow<List<GraphEditorVisualEdgeImp>>
        get() = edgeManger.edges
    val nodes: StateFlow<Set<GraphEditorNode>>
        get() = nodeManger.nodes

    private var operationMode = GraphEditorMode.None
    private var nextAddedGraphEditorNode: GraphEditorNode? = null
    val currentAddingEdge = edgeManger.currentAddingEdge

    //Edge and Node Deletion
    val selectedNode= nodeManger.selectedNode

    var selectedEdge =edgeManger.selectedEdge

    fun onRemovalRequest() {
        nodeManger.removeNode()
        edgeManger.removeEdge()
    }


    /*
    Database operations,
    we should not hold the context in view model but for demo purposes used it
     */
    private val dao =
        EditDatabase(context, density)
    private val scope = CoroutineScope(Dispatchers.Main)

    fun onSave() {
        dao.insert(nodes.value.toList())
        dao.insertEdge(edges.value)
    }

    init {
        try {
            scope.launch {
                dao.getNodes().collect {
                    nodeManger.setNode(it.toSet())
                }

            }
        } catch (_: Exception) {

        }
        try {
            scope.launch {
                dao.getEdges().collect {
                    edgeManger.setEdge(it)

                }
            }
        } catch (_: Exception) {
//
        }
    }

    //Direction
    private var hasDirection: Boolean = true
    fun onDirectionChanged(hasDirection: Boolean) {
        this.hasDirection = hasDirection
    }


    fun onAddNodeRequest(cost: String) {
        nextAddedGraphEditorNode = GraphEditorNode(id = UUID.randomUUID().toString(), label = cost)
        operationMode = GraphEditorMode.NodeAdd
    }


    fun onEdgeConstInput(cost: String) {
        operationMode = GraphEditorMode.EdgeAdd
        edgeManger.addEdge(
            GraphEditorVisualEdgeImp(
                id = UUID.randomUUID().toString(),
                start = Offset.Zero,
                end = Offset.Zero,
                control = Offset.Zero,
                cost = cost,
                minTouchTargetPx = 30.dp.value * density,
                isDirected = hasDirection,
            )
        )
    }

    fun onTap(tappedPosition: Offset) {
        nodeManger.observeCanvasTap(tappedPosition)
      //  selectedEdge.value = edgeManger.selectedEdge(tappedPosition)

        when (operationMode) {
            GraphEditorMode.NodeAdd -> {
                addNode(tappedPosition)
                operationMode = GraphEditorMode.None
            }

            else -> {
                edgeManger.onTap(tappedPosition)
            }
        }
    }


    fun onDragStart(startPosition: Offset) {
        edgeManger.onDragStart(startPosition)
    }

    fun onDrag(dragAmount: Offset) {
        nodeManger.onDragging(dragAmount)
        edgeManger.dragOngoing(dragAmount, dragAmount)
    }

    fun dragEnd() {
        edgeManger.dragEnded()
    }

    private fun addNode(position: Offset) {
        nextAddedGraphEditorNode?.let {
            val radius = it.minNodeSize.value * density / 2
            nodeManger.add(it.copy(topLeft = position - Offset(radius, radius)))
        }
    }

}