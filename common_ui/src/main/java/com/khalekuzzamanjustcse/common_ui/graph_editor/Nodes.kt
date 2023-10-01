package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp



data class GraphEditorVisualNode(
    val basicNode: GraphBasicNode,
    val onClick: (GraphEditorVisualNode) -> Unit = {},
    val onDragEnd: (GraphEditorVisualNode) -> Unit = { },
    override val size: Dp,
    override val id: Int = basicNode.id,
    override val label: String = basicNode.label,
    override val position: Offset = basicNode.position,
    override val sizePx: Float=0f,
) : GraphBasicNode

data class Edge(
    override val startNodeId: Int,
    override val endNodeId: Int,
):GraphBasicEdge

data class DrawingEdge(
    override val start: Offset,
    override val end: Offset,
    override val isDirected: Boolean = false,
):VisualEdge
