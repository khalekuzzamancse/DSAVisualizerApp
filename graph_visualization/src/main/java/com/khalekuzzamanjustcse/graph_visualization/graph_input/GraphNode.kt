package com.khalekuzzamanjustcse.graph_visualization.graph_input

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


/*
to avoid  wanted behaviour storing the nodes only one list.
do not copy the node or something equivalent.
to avoid modifying from outside exposing the same node list as immutable but observable
so that when node or edge added then observable the update
 */

class Graph<T> {
    private val _nodes: MutableState<List<GraphNode<T>>> = mutableStateOf(emptyList())
    private val _edges: MutableState<List<Pair<Int, Int>>> = mutableStateOf(emptyList())
    private val _lastClickedTwoNodeRef: MutableState<List<Int>> = mutableStateOf(emptyList())
    val nodes: List<GraphNode<T>>
        get() = _nodes.value
    val edges: List<Pair<Int, Int>>
        get() = _edges.value

    val adjacencyListNodeRef: List<List<Int>>
        get() = _nodes.value.map { it.neighbourReference }

    val lastClickedTwoNodeRef: List<Int>
        get() = _lastClickedTwoNodeRef.value


    constructor()
    constructor(vararg initialNodes: GraphNode<T>) {
        _nodes.value = initialNodes.toList()
    }

    fun addNode(node: GraphNode<T>) {
        val newList = _nodes.value.toMutableList()
        newList.add(node)
        _nodes.value = newList
    }

    fun addEdge(refU: Int, refV: Int) {

        val u = nodeByRef(refU)
        val v = nodeByRef(refV)
        if (u != null && v != null) {
            u.addNeighbor(refV)
            v.addNeighbor(refU)
            val newEdges = _edges.value.toMutableList()
            newEdges.add(Pair(refU, refV))
            _edges.value = newEdges
        }
    }

    fun nodeByRef(index: Int): GraphNode<T>? {
        return if (index >= 0 && index < _nodes.value.size)
            _nodes.value[index]
        else
            null
    }

    fun onNodeLongClick(node: Int) {
        val newList = _lastClickedTwoNodeRef.value.toMutableList()
        if (newList.size < 2 && node !in newList) {
            newList.add(node)
        } else if (node !in newList) {
            newList[0] = newList[1]
            newList[1] = node
        }
        _lastClickedTwoNodeRef.value = newList
    }

    fun changeNodeColor(nodeRef: Int, color: Color) {
        if (nodeRef >= 0 && nodeRef < _nodes.value.size) {
            //avoid copy the node,modify the origal reference
            _nodes.value[nodeRef].color.value = color
        }
    }


}

interface GraphNode<T> {
    val value: T
    val sizePx: Float
    var neighbourReference: List<Int>
    val offset: MutableState<Offset>
    val color: MutableState<Color>
    fun addNeighbor(nodeRef: Int)
    fun onDrag(dragAmount: Offset)
    fun getCenter(): Offset
}

data class DraggableGraphNode<T>(
    override val value: T,
    override val sizePx: Float,
    override val offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    override val color: MutableState<Color> = mutableStateOf(Color.Red),
    override var neighbourReference: List<Int> = emptyList(),
) : GraphNode<T> {
    override fun addNeighbor(nodeRef: Int) {
        val newList = neighbourReference.toMutableList()
        newList.add(nodeRef)
        neighbourReference = newList
    }

    override fun onDrag(dragAmount: Offset) {
        offset.value += dragAmount
    }

    override fun getCenter(): Offset {
        return offset.value + Offset(sizePx / 2, sizePx / 2)
    }
}

