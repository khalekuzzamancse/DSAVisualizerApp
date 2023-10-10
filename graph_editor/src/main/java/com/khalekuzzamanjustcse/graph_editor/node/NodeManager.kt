package com.khalekuzzamanjustcse.graph_editor.node

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NodeManager(
    private val density: Float,
) {

    private val _nodes = MutableStateFlow(emptySet<Node>())
    val nodes = _nodes.asStateFlow()

    fun nodeTapped(offset: Offset): Node? {
        return _nodes.value.find { it.isInsideCircle(offset) }
    }

    fun removeNode(node: Node) {
        _nodes.update { nodeSet ->
            nodeSet.filter { it.id != node.id }.toSet()
        }
    }

    fun add(node: Node) {
        _nodes.value = _nodes.value + node
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
            set.map { it.disableEdit() }.toSet()
        }
    }
}