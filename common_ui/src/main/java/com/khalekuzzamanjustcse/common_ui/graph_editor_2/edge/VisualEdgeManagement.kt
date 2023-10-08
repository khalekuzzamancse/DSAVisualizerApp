package com.khalekuzzamanjustcse.common_ui.graph_editor_2.edge

import android.util.Log
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


enum class DragType {
    Normal, AfterLongPress
}

interface GraphEditorVisualEdgeManger {
    val edges: StateFlow<List<GraphEditorVisualEdge>>
    fun addEdge(start: Offset, end: Offset, cost: String? = null, isDirected: Boolean = true)
    fun setEdges(edges: List<GraphEditorVisualEdge>)
    fun dragStarted(type: DragType, position: Offset)
    fun dragOngoing(type: DragType, dragAmount: Offset)
    fun dragEnded()


}

class GraphEditorVisualEdgeMangerImp(
    private val minTouchTargetPx: Float,
) : GraphEditorVisualEdgeManger {
    private var autoGenerateId = 3

    private val _edges: MutableStateFlow<List<GraphEditorVisualEdge>> = MutableStateFlow(
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
                start = Offset(50f, 200f),
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

    override fun setEdges(edges: List<GraphEditorVisualEdge>) {
        _edges.update { edges }
    }

    private var dragging: GraphEditorVisualEdge? = null


    private var dragMode = DragType.Normal


    private fun moveEdge(dragAmount: Offset) {
        dragging?.let {
            _edges.update { list ->
                list.map { edge ->
                    if (edge.id == dragging!!.id) {
                        edge.move(dragAmount)
                    } else edge
                }
            }
        }
    }

    private fun transformEdge(dragAmount: Offset) {
        dragging?.let {
            _edges.update { list ->
                list.map { edge ->
                    if (edge.id == dragging!!.id) {
                        val (x, y) = dragAmount
                        if (kotlin.math.abs(x) > kotlin.math.abs(y)) {
                            when {
                                x > 0 -> edge.changeSize(dragAmount)
                                x < 0 -> edge.changeSize(dragAmount)
                                else -> {
                                    edge
                                }
                            }
                        } else {
                            when {
                                y > 0 -> edge.onAnchorPointDrag(dragAmount)
                                y < 0 ->
                                    edge.onAnchorPointDrag(dragAmount)

                                else -> edge
                            }
                        }
                    } else edge
                }
            }
        }
    }

    override fun dragStarted(type: DragType, position: Offset) {
        dragging = edges.value.find { it.isAnchorTouched(position) }
        dragMode = type
        dragging?.let {
            _edges.update { list ->
                list.map { edge ->
                    if (edge.id == dragging!!.id) {
                        edge.onReadyToOperation()
                    } else edge
                }
            }

        }
    }

    override fun dragOngoing(type: DragType, dragAmount: Offset) {
        when (type) {
            DragType.Normal -> {
                transformEdge(dragAmount)
            }
            DragType.AfterLongPress -> moveEdge(dragAmount)
        }
    }

    override fun dragEnded() {

        dragging?.let {
            _edges.update { list ->
                list.map { edge ->
                    if (edge.id == dragging!!.id) {
                        edge.onMoveOperationEnd()
                    } else edge
                }
            }
            dragging = null
        }

    }
}