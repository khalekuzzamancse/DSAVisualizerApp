package com.khalekuzzamanjustcse.graph_editor.ui.ui.node

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class GraphEditorNode(
    override val id: String,
    override val label: String,
    override val topLeft: Offset = Offset.Zero,
    override val minNodeSize: Dp = 50.dp,
    override val halfSize: Dp = 20.dp,
    override val color: Color = Color.Red,
):BasicNode

interface BasicNode {
    val id: String
    val label: String
    val topLeft: Offset
    val minNodeSize: Dp
    val halfSize: Dp
    val color: Color
}
