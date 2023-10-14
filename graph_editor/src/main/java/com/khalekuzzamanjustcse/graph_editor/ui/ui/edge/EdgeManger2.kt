package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import android.util.Range
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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

    private var _selectedPoint = EdgePoint.None

    override fun onTap(tappedPosition: Offset) {
        this.tappedPosition = tappedPosition
        editingMode = EditingMode.EditEdge

        _selectedEdge.value = findSelectedEdge()
        val anEdgeIsSelected = _selectedEdge.value != null
        if (anEdgeIsSelected) {
            _selectedPoint = selectPoint(_selectedEdge.value)
            highLightPoint(_selectedEdge.value, _selectedPoint)
        } else {
            deSelectEdges()
        }
    }

    private fun deSelectEdges() {
        _selectedEdge.value = null
        _selectedPoint = EdgePoint.None
        _edges.update { edges ->
            edges.map {
                it.copy(
                    selectedPoint = EdgePoint.None,
                    pathColor = Color.Black
                )
            }
        }
    }

    private fun findSelectedEdge(): GraphEditorVisualEdgeImp? {
        return _edges.value.find { edge ->
            edge.isAnyControlTouched(tappedPosition)
        }
    }


    private fun highLightPoint(edge: GraphEditorVisualEdgeImp?, point: EdgePoint) {
        edge?.let { activeEdge ->
            val highLightedEdge = when (point) {
                EdgePoint.Start -> activeEdge.copy(
                    selectedPoint = EdgePoint.Start,
                    pathColor = Color.Blue,
                    showSelectedPoint = true
                )

                EdgePoint.End -> activeEdge.copy(
                    selectedPoint = EdgePoint.End,
                    pathColor = Color.Blue,
                    showSelectedPoint = true
                )

                EdgePoint.Control -> activeEdge.copy(
                    selectedPoint = EdgePoint.Control,
                    pathColor = Color.Blue,
                    showSelectedPoint = true
                )

                else -> activeEdge.copy(
                    selectedPoint = EdgePoint.None,
                    pathColor = Color.Black
                )
            }
            _edges.value = _edges.value - activeEdge
            _edges.value = _edges.value + highLightedEdge
        }

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
        editingMode?.let {editingMode
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

    private fun selectPoint(edge: GraphEditorVisualEdgeImp?): EdgePoint {
        edge?.let {
            return if (edge.isStartTouched(tappedPosition)) {
                EdgePoint.Start
            } else if (edge.isEndTouched(tappedPosition)) {
                EdgePoint.End
            } else if (edge.isControlTouched(tappedPosition)) {
                EdgePoint.Control
            } else EdgePoint.None
        }
        return EdgePoint.None
    }

    private fun GraphEditorVisualEdgeImp.updatePoint(amount: Offset): GraphEditorVisualEdgeImp {
        return when (selectedPoint) {
            EdgePoint.Start -> {
                var (x, y) = start + amount
                if (x < 0f)
                    x = 0f
                if (y < 0f)
                    y = 0f
                val newStart = Offset(x, y)
                this.copy(start = newStart, control = (newStart + end) / 2f)
            }

            EdgePoint.End -> {
                var (x, y) = end + amount
                if (x < 0f)
                    x = 0f
                if (y < 0f)
                    y = 0f
                val newEnd = Offset(x, y)
                this.copy(end = newEnd, control = (start + newEnd) / 2f)
            }

            EdgePoint.Control -> {
                var (x, y) = control + amount
                if (x < 0f)
                    x = 0f
                if (y < 0f)
                    y = 0f
                val newMid = Offset(x, y)
                this.copy(control = newMid)
            }

            else -> this

        }
    }


}

fun GraphEditorVisualEdgeImp.isAnyControlTouched(touchPosition: Offset): Boolean {
    return isStartTouched(touchPosition) || isEndTouched(touchPosition) ||
            isControlTouched(touchPosition)
}


private fun GraphEditorVisualEdgeImp.isControlTouched(touchPosition: Offset) =
    isTargetTouched(touchPosition, pathCenter)

private fun GraphEditorVisualEdgeImp.isStartTouched(touchPosition: Offset) =
    isTargetTouched(touchPosition, start)

private fun GraphEditorVisualEdgeImp.isEndTouched(touchPosition: Offset) =
    isTargetTouched(touchPosition, end)

private fun GraphEditorVisualEdgeImp.isTargetTouched(
    touchPosition: Offset,
    target: Offset
): Boolean {
    return touchPosition.x in Range(
        target.x - minTouchTargetPx / 2,
        target.x + minTouchTargetPx / 2
    ) &&
            touchPosition.y in Range(
        target.y - minTouchTargetPx / 2,
        target.y + minTouchTargetPx / 2
    )
}