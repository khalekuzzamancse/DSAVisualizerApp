package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.khalekuzzamanjustcse.graph_editor.ui.ui.basic_concept_demo.EdgePoint

/*
There are two reasons for dragging:
If an existing edge is being dragged
a new edge is adding
Or neither existing edge is being dragged nor new edge being added.
it will handle just the existing edge dragging,
 */

class ExistingEdgeDragManager(private val selectedEdge: GraphEditorVisualEdgeImp) {

    fun onDrag(dragAmount: Offset): GraphEditorVisualEdgeImp {
        return selectedEdge.updatePoint(dragAmount)
    }


    private fun GraphEditorVisualEdgeImp.updatePoint(amount: Offset): GraphEditorVisualEdgeImp {
        Log.i("SelectedPoint","${selectedEdge.selectedPoint}")
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

    private fun getPositionWithinCanvas(offset: Offset): Offset {
        var (x, y) = offset
        if (x < 0f) x = 0f
        if (y < 0f) y = 0f
        return Offset(x, y)
    }
}