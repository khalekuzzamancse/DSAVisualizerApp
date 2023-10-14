package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewAddingEdgeDragManager {
    private val nextAddedEdge = MutableStateFlow<GraphEditorVisualEdgeImp?>(null)
    fun setNewAddingEdge(edge:GraphEditorVisualEdgeImp?){
        nextAddedEdge.value=edge
    }



    val addingEdge = nextAddedEdge.asStateFlow()
    fun onDragStart(offset: Offset) {
        nextAddedEdge.value?.let {
            nextAddedEdge.value =
                it.copy(start = offset, end = offset, control = offset)
        }
    }

    fun onDrag(dragAmount: Offset) {
        nextAddedEdge.value?.let {
            val start = it.start
            val end = it.end + dragAmount
            val control = (start + end) / 2f
            nextAddedEdge.value = it.copy(start = start, end = end, control = control)
        }
    }


}