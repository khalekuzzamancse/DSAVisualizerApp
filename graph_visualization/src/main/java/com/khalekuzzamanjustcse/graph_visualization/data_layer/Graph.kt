package com.khalekuzzamanjustcse.graph_visualization.data_layer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.EdgeComposableState
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.NodeComposableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/*
It is independent of UI
it know the nodes and it adjacent
and it has capabilities to manipulate data
it does not know it will will represent in UI
the node are represented using integer internally
Use synchronous block to lock,tread each operation as automatic,
these operations will done from coroutine so by chance multiple thread can not do multiple
operations to avoid use synchronization because each operation depends on other.
to avoid deadlock because of using multiple lock object as a result
there is a chance of getting multiple block access by separate threads so use the
same lock for that operations that can depends on each other

 */

@Immutable
data class DataLayerGraphEdge(
    val uIndexRef: Int,
    val vIndexRef: Int,
    val cost: Int = 0,
)


data class DataLayerGraph<T>(
    val isUnDirected: Boolean = true,
) {
    private val lock = Any()
    private var _nodes = MutableStateFlow(emptyList<NodeComposableState>())
    private var _edges = MutableStateFlow(emptyList<DataLayerGraphEdge>())
    val nodes = _nodes.asStateFlow()
    val drawingEdge = MutableStateFlow(emptyList<EdgeComposableState>())

    val nodesList: List<NodeComposableState>
        get() = _nodes.value
    val edgesList: List<DataLayerGraphEdge>
        get() = _edges.value

    private val numberOfNodes: Int
        get() = _nodes.value.size

    private fun updateDrawingEdge() {
        drawingEdge.update {
            _edges.value.map {
                val start = _nodes.value[it.uIndexRef].center
                val end = _nodes.value[it.vIndexRef].center
                EdgeComposableState(start, end)
            }
        }
    }

    //-----------Manipulating nodes and edges---------
    fun setNodes(nodes: List<NodeComposableState>) {
        synchronized(lock) {
            _nodes.update { nodes }
        }

    }

    fun setEdges(edges: List<DataLayerGraphEdge>) {
        synchronized(lock) {
            _edges.update { edges }
        }

    }

    //---------------Manipulating Nodes------------------------//
    fun addNode(value: T) {
//        synchronized(lock) {
//            _nodes.value = _nodes.value + DataLayerGraphNode(data = value)
//        }
    }

    fun addNode(node: NodeComposableState) {
        synchronized(lock) {
            _nodes.value = _nodes.value + node
        }
    }

    fun removeNode(indexRef: Int) {
        //removing all edges associated with the node
        synchronized(lock) {
            _nodes.update { currentNodes ->
                val nodes = currentNodes.toMutableList()
                if (isANode(indexRef)) {
                    nodes.removeAt(indexRef)
                }
                nodes.toList()
            }
            _edges.update { edges ->
                edges.filter { edge -> edge.uIndexRef != indexRef && edge.vIndexRef != indexRef }
                    .map { edge ->
                        var u = edge.uIndexRef
                        var v = edge.vIndexRef
                        if (u > indexRef)
                            u--
                        if (v > indexRef)
                            v--
                        if (isANode(u) && isANode(v))
                            edge.copy(uIndexRef = u, vIndexRef = v)
                        else edge
                    }
            }
            //update edges
            updateDrawingEdge()
        }

    }


    //---------------Manipulating Edges------------------------//

    fun addEdge(edge: DataLayerGraphEdge) {
        synchronized(lock) {
            if (isANode(edge.uIndexRef) && isANode(edge.vIndexRef)) {
                _edges.value = _edges.value + edge
            }
            updateDrawingEdge()

        }

    }

    fun removeEdge(index: Int) {
        synchronized(lock) {
            val edge = _edges.value.toMutableList()
            if (isAnEdge(index)) {
                _edges.update {
                    edge.removeAt(index)
                    edge
                }
            }
            updateDrawingEdge()
        }

    }

    //Node property manipulation
    fun onDrag(nodeIndex: Int, offset: Offset) {
        val oldState = _nodes.value[nodeIndex]
        val updatedNode = oldState.copy(offset = offset + oldState.offset)
        _nodes.update { it.toMutableList().apply { set(nodeIndex, updatedNode) } }
        //move the edges also
        updateDrawingEdge()
    }

    val adjacentList: List<List<Int>>
        get() {
            val adjacency: List<MutableList<Int>> = List(numberOfNodes) { mutableListOf() }
            _edges.value.forEach { edge ->
                val u = edge.uIndexRef
                val v = edge.vIndexRef
                if (isANode(u) && isANode(v)) {
                    adjacency[u].add(v)
                    if (isUnDirected) {
                        adjacency[v].add(u)
                    }
                }

            }
            return adjacency.toList()
        }

    fun printAdjacency() {
        adjacentList.forEachIndexed { node, neighbors ->
            println("$node :$neighbors")
        }
        println("Edges:${
            _edges.value
                .map {
                    if (isUnDirected)
                        "${_nodes.value[it.uIndexRef].label} <-->${_nodes.value[it.vIndexRef].label}\n"
                    else "${_nodes.value[it.uIndexRef].label} -->${_nodes.value[it.vIndexRef].label}\n"
                }
        }"
        )
        println()
    }

    private fun isANode(indexRef: Int): Boolean {
        return indexRef in 0 until numberOfNodes
    }

    private fun isAnEdge(indexRef: Int): Boolean {
        return indexRef in 0 until _edges.value.size
    }
}
