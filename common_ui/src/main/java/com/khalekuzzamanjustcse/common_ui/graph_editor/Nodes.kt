package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

interface GraphBasicNode {
    val id: Int
    val label: String
    val position: Offset
    val sizePx:Float
    val size:Dp
}

data class GraphEditorVisualNode(
    val basicNode: GraphBasicNode,
    val onClick: (GraphEditorVisualNode) -> Unit = {},
    val onDragEnd: (GraphEditorVisualNode) -> Unit = { },
    override val size: Dp,
    override val id: Int = basicNode.id,
    override val label: String = basicNode.label,
    override val position: Offset = basicNode.position,
    override val sizePx: Float=0f,
) : GraphBasicNode {

}

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
