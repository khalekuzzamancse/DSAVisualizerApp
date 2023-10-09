package com.khalekuzzamanjustcse.graph_editor.edge

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


interface GraphEditorVisualEdgeManger {
    val edges: StateFlow<List<GraphEditorVisualEdge>>
    fun addEdge(cost: String)
    fun onTap(tappedPosition: Offset)
    fun dragOngoing(dragAmount: Offset, position: Offset)
    fun dragEnded()


}

class GraphEditorVisualEdgeMangerImp(
    private val minTouchTargetPx: Float,
) : GraphEditorVisualEdgeManger {
    private var autoGenerateId = 3

    private val _edges: MutableStateFlow<List<GraphEditorVisualEdgeImp>> =
        MutableStateFlow(emptyList())
    override val edges = _edges.asStateFlow()
    private var _isDirected = MutableStateFlow(true)
    val isDirected = _isDirected.asStateFlow()

    //
    private val nextAddedEdge = MutableStateFlow<GraphEditorVisualEdgeImp?>(null)
    val currentAddingEdge = nextAddedEdge.asStateFlow()
    private val _showEdgeInputPopUp = MutableStateFlow(false)
    val showEdgeInputPopUp = _showEdgeInputPopUp.asStateFlow()

    //
    private val _selectedEdge = MutableStateFlow<GraphEditorVisualEdge?>(null)
    val selectedEdge = _selectedEdge.asStateFlow()

    override fun addEdge(
        cost: String
    ) {
        nextAddedEdge.value = GraphEditorVisualEdgeImp(
            id = autoGenerateId,
            start = Offset.Zero,
            end = Offset.Zero,
            control = Offset.Zero,
            cost = cost,
            isDirected = isDirected.value,
            minTouchTargetPx = minTouchTargetPx
        )
        _showEdgeInputPopUp.value = false
    }

    fun onEdgeInputRequest() {
        _showEdgeInputPopUp.value = true
    }

    fun onGraphTypeChanged() {
        _isDirected.value = false
    }


    override fun onTap(tappedPosition: Offset) {
        _selectedEdge.value = _edges.value.find {
            it.isAnyControlTouched(tappedPosition)
        }
        _selectedEdge.value?.let {
            _selectedEdge.value = it.goEditMode(tappedPosition)
            _edges.value=_edges.value-it as GraphEditorVisualEdgeImp
        }

    }

    fun onDragStart(offset: Offset) {
        nextAddedEdge.value?.let {
            nextAddedEdge.value =
                it.copy(start = offset, end = offset, control = offset)
        }
    }


    override fun dragOngoing(dragAmount: Offset, position: Offset) {
        nextAddedEdge.value?.let {
            val start = it.start
            val end = it.end + dragAmount
            val control = (start + end) / 2f
            nextAddedEdge.value = it.copy(start = start, end = end, control = control)
        }
        selectedEdge.value?.let {
            _selectedEdge.value=it.updatePoint(dragAmount)
        }
    }

    override fun dragEnded() {
        nextAddedEdge.value?.let {
            _edges.value = edges.value + it
            nextAddedEdge.value = null
        }
        _selectedEdge.value?.let {
            _edges.value=_edges.value+it as GraphEditorVisualEdgeImp
        }

    }
}