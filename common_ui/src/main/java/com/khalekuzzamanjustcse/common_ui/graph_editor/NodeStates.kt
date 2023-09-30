package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class GraphEditorNodeState(
    val id: Int,
    val size: Dp,
    val label: String,
    val position: Offset = Offset.Zero,
    val onClick: (Int) -> Unit = {},
    val onDragEnd: (Int, Offset) -> Unit = { _, _ -> }
)
