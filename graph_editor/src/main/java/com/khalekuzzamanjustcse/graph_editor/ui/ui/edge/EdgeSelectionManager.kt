package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import android.util.Range
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.khalekuzzamanjustcse.graph_editor.ui.ui.basic_concept_demo.EdgePoint

class EdgeSelectionManager(
    private val edges: List<GraphEditorVisualEdgeImp>,
    private val tappedPosition: Offset,
) {
    private var selectedEdge: GraphEditorVisualEdgeImp? = null

    init {
        selectedEdge = edges.find { edge -> isAnyControlTouched(edge) }
    }

    fun findSelectedEdge(): GraphEditorVisualEdgeImp? {
        return selectedEdge
    }

    fun getEdgesWithSelection(): List<GraphEditorVisualEdgeImp> {
        val point = findSelectedPoint()
        return highLightPoint(point)
    }

    private fun highLightPoint(point: EdgePoint): List<GraphEditorVisualEdgeImp> {
        selectedEdge?.let { activeEdge ->
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
            var updatedEdges = edges - activeEdge
            updatedEdges = updatedEdges + highLightedEdge
            return updatedEdges

        }
        return deSelectEdges()
    }

    private fun deSelectEdges()=
         edges.map { it.copy(selectedPoint = EdgePoint.None, pathColor = Color.Black) }


    private fun findSelectedPoint(): EdgePoint {
        selectedEdge?.let { edge ->
            return if (isStartTouched(edge)) EdgePoint.Start
            else if (isEndTouched(edge)) EdgePoint.End
            else if (isControlTouched(edge)) EdgePoint.Control else EdgePoint.None
        }
        return EdgePoint.None
    }

    private fun isAnyControlTouched(edge: GraphEditorVisualEdgeImp): Boolean {
        return isStartTouched(edge) || isEndTouched(edge) || isControlTouched(edge)
    }


    private fun isControlTouched(edge: GraphEditorVisualEdgeImp) =
        isTargetTouched(edge, edge.pathCenter)

    private fun isStartTouched(edge: GraphEditorVisualEdgeImp) =
        isTargetTouched(edge, edge.start)

    private fun isEndTouched(edge: GraphEditorVisualEdgeImp) =
        isTargetTouched(edge, edge.end)

    private fun isTargetTouched(
        edge: GraphEditorVisualEdgeImp,
        target: Offset
    ): Boolean {
        val minTouchTargetPx = edge.minTouchTargetPx
        return tappedPosition.x in Range(
            target.x - minTouchTargetPx / 2,
            target.x + minTouchTargetPx / 2
        ) &&
                tappedPosition.y in Range(
            target.y - minTouchTargetPx / 2,
            target.y + minTouchTargetPx / 2
        )
    }
}