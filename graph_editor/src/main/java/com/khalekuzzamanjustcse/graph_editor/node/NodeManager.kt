package com.khalekuzzamanjustcse.graph_editor.node

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NodeManager(
    private val density: Float,
) {
    private var autoGeneratedId = 1
    private val _nodes = MutableStateFlow(emptySet<Node>())
    val nodes = _nodes.asStateFlow()

    private var lastTappedLocation = Offset(50f, 50f)
    fun onCanvasTap(offset: Offset){
        lastTappedLocation = offset
    }
    fun add() {
        _nodes.value = _nodes.value + Node(
            id = autoGeneratedId++,
            center = lastTappedLocation,
            density = density,
            radius = 25.dp
        )
    }

    fun onDragStart(offset: Offset) {
        _nodes.update { set ->
            set.map { it.enableEdit(offset) }.toSet()
        }

    }

    fun onDragging(dragAmount: Offset) {
        _nodes.update { set ->
            set.map { it.updateCenter(dragAmount) }.toSet()
        }
    }

    fun onDragEnd() {
        _nodes.update { set ->
            set.map { it.copy(dragEnabled = false) }.toSet()
        }
    }
}