package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import androidx.compose.ui.geometry.Offset
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorVisualEdge
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorVisualEdgeImp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface GraphEditorVisualEdgeManger {
    val edges: StateFlow<List<GraphEditorVisualEdge>>
    fun onControlPointTapped(tappedEdge: GraphEditorVisualEdge)
    fun onControlPointDragging(draggingEdge: GraphEditorVisualEdge, dragAmount: Offset)

}

class GraphEditorVisualEdgeMangerImp(
) : GraphEditorVisualEdgeManger {
    private val _edges = MutableStateFlow(
        listOf(
            GraphEditorVisualEdgeImp(
                id = 1,
                start = Offset(50f, 100f),
                end = Offset(500f, 100f),
                edgeCost = "55 Tk.",
                isDirected = true
            ),
            GraphEditorVisualEdgeImp(
                id = 2,
                start = Offset(150f, 200f),
                end = Offset(500f, 200f),
                edgeCost = "50 Tk.",
                isDirected = true
            )
        )
    )
    override val edges = _edges.asStateFlow()
    override fun onControlPointTapped(tappedEdge: GraphEditorVisualEdge) {

    }

    override fun onControlPointDragging(draggingEdge: GraphEditorVisualEdge, dragAmount: Offset) {
        _edges.update { list ->
            list.map { edge ->
                if (edge.id == draggingEdge.id) {
                    edge.onAnchorPointDrag(dragAmount)
                } else edge
            }
        }
    }
}
