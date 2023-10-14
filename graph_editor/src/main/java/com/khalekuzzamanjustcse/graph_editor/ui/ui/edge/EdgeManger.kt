package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import androidx.compose.ui.geometry.Offset
import com.khalekuzzamanjustcse.graph_editor.ui.ui.basic_concept_demo.EdgePoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GraphEditorEdgeManger : GraphEditorVisualEdgeManger {
    private val _edges: MutableStateFlow<List<GraphEditorVisualEdgeImp>> =
        MutableStateFlow(emptyList())
    override val edges = _edges.asStateFlow()
    private var editingMode: EditingMode? = null
    private var tappedPosition = Offset.Infinite

    //
    private val nextAddedEdge = MutableStateFlow<GraphEditorVisualEdgeImp?>(null)
    val currentAddingEdge = nextAddedEdge.asStateFlow()


    fun addEdge(edge: GraphEditorVisualEdgeImp) {
        editingMode = EditingMode.AddEdge
        nextAddedEdge.value = edge
    }


    /*
    Observing when the canvas is tapped so that:
    Select a point of the edge to edit it.
    Select the edge to remove it.
     */

    private val _selectedEdge = MutableStateFlow<GraphEditorVisualEdgeImp?>(null)
    val selectedEdge = _selectedEdge.asStateFlow()

    override fun onTap(tappedPosition: Offset) {
        this.tappedPosition = tappedPosition
        editingMode = EditingMode.EditEdge
        val tapListener = EdgeSelectionManager(_edges.value, tappedPosition)
        _selectedEdge.value = tapListener.findSelectedEdge()
        _edges.update { tapListener.getEdgesWithSelection() }

    }


    // Tapping handling done
    //-----------Removing selected edge
    fun removeEdge() {
        _selectedEdge.value?.let { activeEdge ->
            _edges.update { edgeSet ->
                edgeSet.filter { it.id != activeEdge.id }
            }
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
            editingMode
            when (editingMode) {
                EditingMode.AddEdge -> {
                    nextAddedEdge.value?.let {
                        val start = it.start
                        val end = it.end + dragAmount
                        val control = (start + end) / 2f
                        nextAddedEdge.value = it.copy(start = start, end = end, control = control)
                    }
                }

                //dragging existing edge
                EditingMode.EditEdge -> {
                    _selectedEdge.value?.let { activeEdge ->
                        _edges.update { edges ->
                            edges.map { edge ->
                                if (edge.id == activeEdge.id) edge.updatePoint(dragAmount)
                                else edge
                            }
                        }
                    }
                }

                else -> {}
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

    private fun GraphEditorVisualEdgeImp.updatePoint(amount: Offset): GraphEditorVisualEdgeImp {
        return when (selectedPoint) {
            EdgePoint.Start -> {
                val newStart = getPositionWithinCanvas(start + amount)
                this.copy(start = newStart, control = (newStart + end) / 2f)
            }

            EdgePoint.End -> {
                val newEnd = getPositionWithinCanvas(end + amount)
                this.copy(end = newEnd, control = (start + newEnd) / 2f)
            }

            EdgePoint.Control -> {
                this.copy(control = getPositionWithinCanvas(control + amount))
            }

            else -> this

        }
    }

}

private fun getPositionWithinCanvas(offset: Offset): Offset {
    var (x, y) = offset
    if (x < 0f) x = 0f
    if (y < 0f) y = 0f
    return Offset(x, y)
}