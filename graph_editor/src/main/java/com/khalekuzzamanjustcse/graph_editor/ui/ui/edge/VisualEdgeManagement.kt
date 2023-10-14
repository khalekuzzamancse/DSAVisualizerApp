package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface GraphEditorVisualEdgeManger {
    val edges: StateFlow<List<GraphEditorVisualEdgeImp>>
    fun onTap(tappedPosition: Offset)
    fun dragOngoing(dragAmount: Offset, position: Offset)
    fun dragEnded()
    fun setEdge(edges: List<GraphEditorVisualEdgeImp>)

}

