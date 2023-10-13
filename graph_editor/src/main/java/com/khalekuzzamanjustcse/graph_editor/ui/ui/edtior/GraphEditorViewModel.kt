package com.khalekuzzamanjustcse.graph_editor.ui.ui.edtior

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.data_layer.repoisitory.EditDatabase
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorVisualEdge
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorVisualEdgeImp
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorVisualEdgeMangerImp
import com.khalekuzzamanjustcse.graph_editor.ui.ui.node.Node
import com.khalekuzzamanjustcse.graph_editor.ui.ui.node.NodeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    NodeAdd, DragNode, EdgeAdd, DragEdge, None
}


data class GraphEditorManger(
    private val density: Float,
    private val context: Context,
) {
    private val nodeManger = NodeManager(density)
    private val edgeManger = GraphEditorVisualEdgeMangerImp()
    val edges: StateFlow<List<GraphEditorVisualEdge>>
        get() = edgeManger.edges
    val nodes: StateFlow<Set<Node>>
        get() = nodeManger.nodes

    private var operationMode = GraphEditorMode.None
    private var nextAddedNode: Node? = null
    val currentAddingEdge = edgeManger.currentAddingEdge

    //Edge and Node Deletion

    var selectedNode = MutableStateFlow<Node?>(null)
        private set

    var selectedEdge = MutableStateFlow<GraphEditorVisualEdge?>(null)
        private set
    fun onRemovalRequest() {
        selectedNode.value?.let {
            nodeManger.removeNode(it)
        }
        selectedEdge.value?.let {
            edgeManger.removeEdge(it)
        }
        selectedEdge.value=null
        selectedNode.value=null
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
        nextAddedNode = Node(id = UUID.randomUUID().toString(), density = density, text = cost)
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
        selectedNode.value = nodeManger.nodeTapped(tappedPosition)
        selectedEdge.value = edgeManger.selectedEdge(tappedPosition)

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
        nodeManger.onDragStart(startPosition)
        edgeManger.onDragStart(startPosition)
    }

    fun onDrag(dragAmount: Offset) {
        nodeManger.onDragging(dragAmount)
        edgeManger.dragOngoing(dragAmount, dragAmount)
    }

    fun dragEnd() {
        nodeManger.onDragEnd()
        edgeManger.dragEnded()
    }

    private fun addNode(position: Offset) {
        nextAddedNode?.let {
            val radius = it.minNodeSize.value * density / 2
            nodeManger.add(it.copy(topLeft = position - Offset(radius, radius)))
        }
    }

}