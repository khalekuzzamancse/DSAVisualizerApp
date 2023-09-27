package com.khalekuzzamanjustcse.graph_visualization.data_layer

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
It is independent of UI
it know the nodes and it adjacent
and it has capabilities to manipulate data
it does not know it will will represent in UI
the node are represented using integer internally

 */
@Immutable
data class DataLayerGraphNode<T>(
    val data: T
)

@Immutable
data class DataLayerGraphEdge(
    val uIndexRef: Int,
    val vIndexRef: Int,
    val cost: Int = 0,
)


data class DataLayerGraph<T>(
    val isUnDirected: Boolean = true,
) {

    private var _nodes = MutableStateFlow(emptyList<DataLayerGraphNode<T>>())
    private var _edges = MutableStateFlow(emptyList<DataLayerGraphEdge>())
    val nodes = _nodes.asStateFlow()
    val edges = _edges.asStateFlow()
    val nodesList:List<DataLayerGraphNode<T>>
        get()=_nodes.value
    val edgesList:List<DataLayerGraphEdge>
        get()=_edges.value

    private val numberOfNodes: Int
        get() = _nodes.value.size

    //-----------Manipulating nodes and edges---------
    fun setNodes(nodes: List<DataLayerGraphNode<T>>) {
        _nodes.update { nodes }
    }

    fun setEdges(edges: List<DataLayerGraphEdge>) {
        _edges.update { edges }
    }

    //---------------Manipulating Nodes------------------------//
    fun addNode(value: T) {
        _nodes.value = _nodes.value + DataLayerGraphNode(data = value)
    }

    fun removeNode(indexRef: Int) {
        //removing all edges associated with the node
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

    }


    //---------------Manipulating Edges------------------------//

    fun addEdge(edge: DataLayerGraphEdge) {
        if (isANode(edge.uIndexRef) && isANode(edge.vIndexRef))
            _edges.value = _edges.value + edge
    }

    fun removeEdge(index: Int) {
        val edge = _edges.value.toMutableList()
        if (isAnEdge(index)) {
            _edges.update {
                edge.removeAt(index)
                edge
            }
        }
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
//        println("Edges:${_edges.value.map { Pair(it.uIndexRef,it.vIndexRef) }}")
        println()
    }

    private fun isANode(indexRef: Int): Boolean {
        return indexRef in 0 until numberOfNodes
    }

    private fun isAnEdge(indexRef: Int): Boolean {
        return indexRef in 0 until _edges.value.size
    }
}

fun main() {
    runBlocking {
        val graph = DataLayerGraph<Int>()
        launch {
            graph.nodes.collect {
                graph.printAdjacency()
            }
        }
        delay(1000)
        graph.addNode(10)
        delay(1000)
        graph.addNode(20)
        delay(1000)
        graph.addNode(30)

        delay(1000)
        launch {
            graph.edges.collect {
                graph.printAdjacency()
            }
        }
        val edge12 = DataLayerGraphEdge(0, 1)
        val edge23 = DataLayerGraphEdge(1, 2)
        graph.addEdge(edge12)
        delay(1000)
        graph.addEdge(edge23)
        delay(1000)
        graph.removeNode(0)
        delay(1000)
        graph.removeEdge(0)

    }
}
