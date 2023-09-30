package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class GraphEditorNode(
    val id: Int,
    val size: Dp,
    val label: String,
    val position: Offset = Offset.Zero,
    val onClick: (GraphEditorNode) -> Unit = {},
    val onDragEnd: (GraphEditorNode) -> Unit = { }
)


data class Edge(
    val startNodeId: Int,
    val endNodeId: Int,
    val weight: Int = 1,
)

data class DrawingEdge(
    val start: Offset,
    val end: Offset,
    val isDirected: Boolean = false,
)
