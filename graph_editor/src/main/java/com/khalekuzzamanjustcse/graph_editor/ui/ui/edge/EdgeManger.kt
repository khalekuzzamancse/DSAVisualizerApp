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

    fun addEdge(edge: GraphEditorVisualEdgeImp) {
        _edges.value = edges.value + edge.copy(selectedPoint=EdgePoint.End)
        _selectedEdge.value = edge.copy(selectedPoint=EdgePoint.End)
        newlyAdding=true
    }
    /*
    Observing when the canvas is tapped so that:
    Select a point of the edge to edit it.
    Select the edge to remove it.
     */

    private val _selectedEdge = MutableStateFlow<GraphEditorVisualEdgeImp?>(null)
    val selectedEdge = _selectedEdge.asStateFlow()
    private var newlyAdding=false


    override fun onTap(tappedPosition: Offset) {
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
        if(newlyAdding){
            _selectedEdge.value?.let { activeEdge ->
                _edges.update { edges ->
                    edges.map { edge ->
                        if (edge.id == activeEdge.id)
                            edge.copy(
                                start = offset,
                                end = offset,
                                control = offset,
                                selectedPoint = EdgePoint.End
                            )
                        else edge
                    }
                }
            }
        }

    }

    override fun dragOngoing(dragAmount: Offset, position: Offset) {
        _selectedEdge.value?.let { activeEdge ->
            _edges.update { edges ->
                edges.map { edge ->
                    if (edge.id == activeEdge.id) ExistingEdgeDragManager(edge).onDrag(
                        dragAmount
                    ) else edge
                }
            }
        }
    }

    override fun dragEnded() {
        _selectedEdge.value=null
        newlyAdding=false
    }

    override fun setEdge(edges: List<GraphEditorVisualEdgeImp>) {
        _edges.update { edges }
    }

}

