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
    fun addEdge(start: Offset, end: Offset, cost: String? = null, isDirected: Boolean = true)
    fun onCanvasDragging(dragAmount: Offset)
    fun onDragStart(position: Offset)
    fun onDragEnd()

}

class GraphEditorVisualEdgeMangerImp(
    private val minTouchTargetPx: Float,
) : GraphEditorVisualEdgeManger {
    private var autoGenerateId = 3
    private val _edges = MutableStateFlow(
        listOf(
            GraphEditorVisualEdgeImp(
                id = 1,
                start = Offset(50f, 100f),
                end = Offset(500f, 100f),
                edgeCost = "55 Tk.",
                isDirected = true,
                minTouchTargetPx = minTouchTargetPx,
            ),
            GraphEditorVisualEdgeImp(
                id = 2,
                start = Offset(150f, 200f),
                end = Offset(500f, 200f),
                edgeCost = "50 Tk.",
                isDirected = true,
                minTouchTargetPx = minTouchTargetPx,
            )
        )
    )
    override val edges = _edges.asStateFlow()
    override fun addEdge(
        start: Offset, end: Offset,
        cost: String?, isDirected: Boolean
    ) {
        val edge = GraphEditorVisualEdgeImp(
            id = autoGenerateId++,
            start = start,
            end = end,
            edgeCost = cost,
            isDirected = isDirected,
            minTouchTargetPx = minTouchTargetPx,
        )
        _edges.value = _edges.value + edge
    }
    private var dragging: GraphEditorVisualEdge? = null

    override fun onCanvasDragging(dragAmount: Offset) {
        dragging?.let {
            _edges.update { list ->
                list.map { edge ->
                    if (edge.id == dragging!!.id) {
                        edge.onAnchorPointDrag(dragAmount)
                    } else edge
                }
            }
        }
    }

    override fun onDragStart(position: Offset) {
        setDraggingEdge(edges.value.find { it.isAnchorTouched(position) })
    }

    override fun onDragEnd() {
        setDraggingEdge(null)
    }

    private fun setDraggingEdge(edge: GraphEditorVisualEdge?) {
        dragging = edge
    }
}