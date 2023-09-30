package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GraphEditorScreen(
    private val nodeSize: Dp,
    private val nodeSizePx: Float,
    private val isDirected: Boolean = false,
) {
    private var graphEditor = GraphEditor(nodeSize, nodeSizePx)
    val nodes: StateFlow<Set<GraphEditorNode>>
        get() = graphEditor.nodes
    val edges: StateFlow<List<DrawingEdge>>
        get() = graphEditor.edges


    fun addNode(value: String) {
        graphEditor.addNode(value)
    }

    fun addEdge() {
        graphEditor.addEdge()
    }
    init {
        val scope= CoroutineScope(Dispatchers.IO)
        scope.launch {
            nodes.collect{

            }
        }
    }

}