package com.khalekuzzamanjustcse.graph_visualization.graph_input

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class Graph<T> {
    val nodes: MutableList<GraphNode<T>> = mutableListOf()
    val edges: MutableList<Pair<GraphNode<T>, GraphNode<T>>> = mutableListOf()
    private val lastClickedTwoNodes: MutableList<GraphNode<T>> = mutableListOf()


    constructor()

    constructor(vararg initialNodes: GraphNode<T>) {
        nodes.addAll(initialNodes)
    }

    fun addNode(node: GraphNode<T>) {
        nodes.add(node)
    }

    fun addEdge(u: GraphNode<T>, v: GraphNode<T>) {
        u.addNeighbor(v)
        v.addNeighbor(u)
        edges.add(Pair(u, v))
    }


    fun getAllNodes() = nodes

    fun onNodeLongClick(node: GraphNode<T>) {
        if (lastClickedTwoNodes.size < 2 && node !in lastClickedTwoNodes) {
            lastClickedTwoNodes.add(node)
        } else if (node !in lastClickedTwoNodes) {
            lastClickedTwoNodes[0] = lastClickedTwoNodes[1]
            lastClickedTwoNodes[1] = node
        }
    }

    fun getLastClickedPair() = lastClickedTwoNodes.map { it } //returning a copy


}


interface GraphNode<T> {
    val value: T
    val sizePx: Float
    val neighbors: MutableList<GraphNode<T>>
    val positions: MutableState<Offset>
    val offset: MutableState<Offset>
    val color: MutableState<Color>
    fun addNeighbor(node: GraphNode<T>)
    fun onDrag(dragAmount: Offset)
    fun getCenter(): Offset
}

data class DraggableGraphNode<T>(
    override val value: T,
    override val sizePx: Float,
    override val neighbors: MutableList<GraphNode<T>> = mutableListOf(),
    override val positions: MutableState<Offset> = mutableStateOf(Offset.Zero),
    override val offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    override val color: MutableState<Color> = mutableStateOf(Color.Red),
) : GraphNode<T> {
    override fun addNeighbor(node: GraphNode<T>) {
        neighbors.add(node)
    }

    override fun onDrag(dragAmount: Offset) {
        offset.value += dragAmount
    }

    override fun getCenter(): Offset {
        return offset.value + Offset(sizePx / 2, sizePx / 2)
    }
}

