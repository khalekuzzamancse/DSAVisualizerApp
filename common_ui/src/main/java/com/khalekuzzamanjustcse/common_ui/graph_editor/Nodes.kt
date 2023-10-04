package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.Dp


interface GraphBasicNode {
    val id: Int
    val label: String
    val position: Offset
    val sizePx: Float
    val size: Dp
}

interface GraphBasicEdge {
    val startNodeId: Int
    val endNodeId: Int
}

interface VisualEdge {
    val start: Offset
    val end: Offset
    val isDirected: Boolean
}

interface GraphEditorVisualEdge {
    val id: Int
    val start: Offset
    val end: Offset
    val controlPoint: Offset
    val edgeCost: String?
    val isDirected: Boolean
    val showAnchor: Boolean
    fun onAnchorPointDrag(dragAmount: Offset): GraphEditorVisualEdge
}

data class GraphEditorVisualEdgeImp(
    override val id: Int,
    override val start: Offset,
    override val end: Offset,
    override val controlPoint: Offset = start.plus(end).div(2f),
    override val edgeCost: String?,
    override val isDirected: Boolean,
    override val showAnchor: Boolean = true
) : GraphEditorVisualEdge {
    override fun onAnchorPointDrag(dragAmount: Offset) =
        this.copy(controlPoint = controlPoint + dragAmount)
}


data class GraphEditorVisualNode(
    val basicNode: GraphBasicNode,
    val onClick: (GraphEditorVisualNode) -> Unit = {},
    val onDragEnd: (GraphEditorVisualNode) -> Unit = { },
    override val size: Dp,
    override val id: Int = basicNode.id,
    override val label: String = basicNode.label,
    override val position: Offset = basicNode.position,
    override val sizePx: Float = 0f,
    val showAnchor: Boolean,
) : GraphBasicNode


/*
Used the Decorator Design Pattern to give
additional properties and behavior(method)
 */
@Immutable
data class GraphSimulationNode(
    val basicNode: GraphBasicNode,
    val color: Color = Color.Green,
    val backgroundColor: Color = Color.Unspecified,
    override val label: String = basicNode.label,
    override val size: Dp = basicNode.size,
    override val sizePx: Float = basicNode.sizePx,
    override val id: Int = basicNode.id,
    override val position: Offset = Offset.Zero,
) : GraphBasicNode {
    val textColor: Color
        get() = if (color.luminance() > 0.5) Color.Black else Color.White
    private val halfSize: Float
        get() = sizePx / 2
    val center: Offset
        get() = position + Offset(halfSize, halfSize)

    val topCenter: Offset
        get() = position + Offset(halfSize, 0f)
    val bottomCenter: Offset
        get() = Offset(position.x + halfSize, position.y + sizePx)

    val leftCenter: Offset
        get() = Offset(position.x, position.y + halfSize)
    val rightCenter: Offset
        get() = Offset(position.x + sizePx, position.y + sizePx)
}

data class Edge(
    override val startNodeId: Int,
    override val endNodeId: Int,
) : GraphBasicEdge

data class DrawingEdge(
    override val start: Offset,
    override val end: Offset,
    override val isDirected: Boolean = false,
) : VisualEdge
