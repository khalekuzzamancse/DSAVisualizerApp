package com.khalekuzzamanjustcse.common_ui.graph_editor

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.khalekuzzamanjustcse.common_ui.command_pattern.AddEdgeCommand
import com.khalekuzzamanjustcse.common_ui.command_pattern.AddNodeCommand
import com.khalekuzzamanjustcse.common_ui.command_pattern.UndoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class GraphEditorCanvasUIState (
    private val minSize: Dp,
    private val minSizePx: Float,
    private val isDirected: Boolean = false,
    private var graphEditor: GraphEditor = GraphEditorGraph(),
    override val onInputDone: (GraphEditorResult) -> Unit = {},
) :GraphEditorCanvas{
    private val _visualEdges = MutableStateFlow(listOf<DrawingEdge>())
    override val visualEdges = _visualEdges.asStateFlow()
    private val reDrawEdges: (Set<Edge>) -> Unit = { basicEdges ->
        _visualEdges.update { getDrawingEdges(basicEdges) }
    }

    private val _visualNodes = MutableStateFlow(setOf<GraphEditorVisualNode>())
    override val visualNodes = _visualNodes.asStateFlow()
    private val onGraphNodesUpdated: (Set<GraphBasicNode>) -> Unit = { basicNodes ->
        _visualNodes.update {
            basicNodes.map { basicNode ->
                GraphEditorVisualNode(
                    basicNode = basicNode, onClick = ::onNodeClick, onDragEnd = ::onNodeDragEnd,
                    size = basicNode.size, sizePx = basicNode.sizePx, showAnchor = false
                )
            }.toSet()
        }
    }
    init {
        graphEditor.onEdgeUpdated=reDrawEdges
        graphEditor.onNodesUpdated = onGraphNodesUpdated
    }



    /*
    Undo and redo operations

     */

    private val undoManager = UndoManager()
    override val disableUndo: Boolean
        get() = undoManager.undoAvailable
    override val disableRedo: Boolean
        get() = undoManager.redoAvailable

    override fun undo() = undoManager.undo()
    override fun redo() = undoManager.redo()


    private var autoGeneratedId = 0
    private val lastTwoClickedNode = arrayOfNulls<GraphEditorVisualNode?>(2)
    private var clickCount = 0


    private val _takeInput = MutableStateFlow(false)
    val takeInput = _takeInput.asStateFlow()
    private var lastClickedPosition = Offset.Zero

    override fun onCanvasTap(offset: Offset) {
        lastClickedPosition = offset
    }

    override fun onNodeInputRequest() {
        _takeInput.update { true }
    }

    private fun onNodeDragEnd(dragged: GraphEditorVisualNode) {
        graphEditor.updateExistingNode(dragged)
    }

    override fun onInputComplete(label: String, size: Dp, sizePx: Float) {
        _takeInput.update { false }
        val node = object : GraphBasicNode {
            override val id = autoGeneratedId++
            override val label = label
            override val position = lastClickedPosition
            override val sizePx = if (sizePx < minSizePx) minSizePx else sizePx
            override val size = if (size < minSize) minSize else size
        }
        val addNodeCommand = AddNodeCommand(node, graphEditor)
        undoManager.execute(addNodeCommand)
    }


    override fun addEdge() {
        val u = lastTwoClickedNode[0]
        val v = lastTwoClickedNode[1]
        if (u != null && v != null) {
            undoManager.execute(AddEdgeCommand(u, v, graphEditor))
        }
    }

    private fun onNodeClick(node: GraphEditorVisualNode) {
        lastTwoClickedNode[clickCount % 2] = node
        clickCount++
    }

    override fun makeAllNodeSameSize() {
        graphEditor.makeHomogenous()
    }

    private fun getDrawingEdges(edgesRef: Set<Edge>): List<DrawingEdge> {
        val drawEdges = mutableListOf<DrawingEdge>()
        edgesRef.forEach { (uId, vId) ->
            val u = visualNodes.value.find { it.id == uId }
            val v = visualNodes.value.find { it.id == vId }
            if (u != null && v != null) {
                drawEdges.add(
                    DrawingEdge(
                        start = u.position + Offset(u.sizePx / 2, u.sizePx / 2),
                        end = v.position + Offset(v.sizePx / 2, v.sizePx / 2),
                        isDirected = isDirected
                    )
                )
            }
        }
        return drawEdges
    }

    override fun onInputComplete() {
        val result = GraphEditorResultImp(
            nodes = graphEditor.nodes.toList(),
            edges = graphEditor.edges.toList(),
            isDirected = isDirected
        )
        onInputDone(result)
        Log.i("AdjacencyList:Graph: ", "${result.adjacencyList}")
        Log.i("AdjacencyList:Graph: ", "${result.isTree}")
        Log.i("AdjacencyList:BT: ", "${result.isBinaryTree}")
    }


}