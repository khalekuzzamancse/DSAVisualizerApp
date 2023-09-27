package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.graph_visualization.command_pattern.UndoManager
import com.khalekuzzamanjustcse.graph_visualization.data_layer.DataLayerGraph
import com.khalekuzzamanjustcse.graph_visualization.data_layer.DataLayerGraphEdge

data class GraphDrawerViewModel(
    private val size: Dp,
    private val sizePx: Float,
) : ViewModel() {

    private val graph = DataLayerGraph<String>()
    val nodes = graph.nodes
    val edges = graph.drawingEdge
    private val undoManager = UndoManager()
    val disableUndo: Boolean
        get() = undoManager.undoAvailable
    val disableRedo: Boolean
        get() = undoManager.redoAvailable


    fun undo() {
        undoManager.undo()
    }

    fun redo() {
        undoManager.redo()
    }

    fun addNode(data: String) {
        val addNodeCommand = AddNodeCommand(
            NodeComposableState(
                label = data,
                size = size,
                sizePx = sizePx,
                offset = lastTappedLocation
            ), graph
        )
        undoManager.execute(addNodeCommand)
    }

    //
    private val recentlyClickedNodes = Array(2) { 0 }
    private var clickCount = 0
    private var lastTappedLocation = Offset.Zero
    private var lastTappedNodeIndexRef = -1

    //event handlers
    fun onDrag(nodeIndex: Int, offset: Offset) {
        graph.onDrag(nodeIndex, offset)
    }

    fun onNodeClick(nodeIndex: Int) {
        recentlyClickedNodes[clickCount % 2] = nodeIndex
        clickCount++
        lastTappedNodeIndexRef = nodeIndex
    }

    fun removeNode() {
        val removeNodeCommand = RemoveNodeCommand(lastTappedNodeIndexRef, graph)
        undoManager.execute(removeNodeCommand)
    }

    fun onCanvasTapped(offset: Offset) {
        lastTappedLocation = offset
    }

    fun addEdge() {
        val addEdgeCommand = AddEdgeCommand(
            DataLayerGraphEdge(recentlyClickedNodes[0], recentlyClickedNodes[1]),
            graph
        )
        undoManager.execute(addEdgeCommand)
    }


}

