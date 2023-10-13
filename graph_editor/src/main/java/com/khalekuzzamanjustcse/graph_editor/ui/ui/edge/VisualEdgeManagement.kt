package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


enum class EditingMode {
    AddEdge, EditEdge,
}

interface GraphEditorVisualEdgeManger {
    val edges: StateFlow<List<GraphEditorVisualEdge>>
    fun onTap(tappedPosition: Offset)
    fun dragOngoing(dragAmount: Offset, position: Offset)
    fun dragEnded()
    fun setEdge(edges: List<GraphEditorVisualEdgeImp>)


}

class GraphEditorVisualEdgeMangerImp(
) : GraphEditorVisualEdgeManger {

    private val _edges: MutableStateFlow<List<GraphEditorVisualEdgeImp>> =
        MutableStateFlow(emptyList())
    override val edges = _edges.asStateFlow()
    private var editingMode: EditingMode? = null

    //
    private val nextAddedEdge = MutableStateFlow<GraphEditorVisualEdgeImp?>(null)
    val currentAddingEdge = nextAddedEdge.asStateFlow()


    fun addEdge(edge: GraphEditorVisualEdgeImp) {
        editingMode = EditingMode.AddEdge
        nextAddedEdge.value = edge
    }

    fun removeEdge(edge: GraphEditorVisualEdge) {
        _edges.update { edgeSet ->
            edgeSet.filter { it.id != edge.id }
        }
    }



    fun selectedEdge(offset: Offset): GraphEditorVisualEdge? {
        return _edges.value.find { it.isAnyControlTouched(offset) }
    }


    override fun onTap(tappedPosition: Offset) {
        editingMode = EditingMode.EditEdge
        _edges.update { edges ->
            edges.map { it.goEditMode(tappedPosition) }
        }
    }

    fun onDragStart(offset: Offset) {
        if (editingMode == EditingMode.AddEdge) {
            nextAddedEdge.value?.let {
                nextAddedEdge.value =
                    it.copy(start = offset, end = offset, control = offset)
            }
        }
    }

    override fun dragOngoing(dragAmount: Offset, position: Offset) {
        editingMode?.let {
            when (it) {
                EditingMode.AddEdge -> {
                    nextAddedEdge.value?.let {
                        val start = it.start
                        val end = it.end + dragAmount
                        val control = (start + end) / 2f
                        nextAddedEdge.value = it.copy(start = start, end = end, control = control)
                    }
                }

                EditingMode.EditEdge -> {
                    _edges.update { edges ->
                        edges.map { edge -> edge.updatePoint(dragAmount) }
                    }

                }
            }
        }


    }

    override fun dragEnded() {
        editingMode?.let { mode ->
            when (mode) {
                EditingMode.AddEdge -> {
                    nextAddedEdge.value?.let {
                        _edges.value = edges.value + it
                        nextAddedEdge.value = null
                    }
                }

                else -> {}
            }
        }
        editingMode = null
    }

    override fun setEdge(edges: List<GraphEditorVisualEdgeImp>) {
        _edges.update { edges }
    }
}