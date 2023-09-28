package com.khalekuzzamanjustcse.graph_visualization.data_layer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

interface GraphCommonEdge {
    val start: Offset
    val end: Offset
    val cost: Any?
}

interface GraphCommonNode<T> {
    val data: T
    val id: Int
    val label: String
    val offset: Offset
    val neighborsId: List<Int>
    fun addNeighbors(id: Int): GraphCommonNode<T>
    fun removeNeighbors(id: Int): GraphCommonNode<T>
    fun changeId(newId: Int): GraphCommonNode<T>
}

@Immutable
data class GraphNode22<T>(
    override val data: T,
    override val offset: Offset = Offset.Zero,
    override val neighborsId: List<Int> = emptyList(),
    override val id: Int = 0,
) : GraphCommonNode<T> {
    override val label: String
        get() = "$data"

    override fun addNeighbors(id: Int): GraphCommonNode<T> {
        return this.copy(neighborsId = this.neighborsId + id)
    }

    override fun removeNeighbors(id: Int): GraphCommonNode<T> {
        return this.copy(neighborsId = this.neighborsId.filter { it != id })
    }

    override fun changeId(newId: Int): GraphCommonNode<T> {
        return this.copy(id = newId)
    }
}

@Immutable
data class AdjacencyList<T>(
    val undirected: Boolean = true,
    val nodes: List<GraphCommonNode<T>> = emptyList()
) {

    fun addNode(node: GraphCommonNode<T>) = this.copy(nodes = nodes + node)
    fun removeNode(id: Int) = this.copy(nodes = nodes.filter { it.id != id })
    val edges: List<Pair<GraphCommonNode<T>, GraphCommonNode<T>>>
        get() {
            var edges: List<Pair<GraphCommonNode<T>, GraphCommonNode<T>>> = emptyList()
            nodes.forEach { node ->
                edges = edges + getNeighborsByIds(node.neighborsId).map { node to it }
            }
            return edges
        }


    fun addEdge(uId: Int, vId: Int): AdjacencyList<T> {
        val tempNodes = nodes.toMutableList()
        val nodeU = nodes.find { it.id == uId }
        val indexOfU = nodes.indexOf(nodeU)
        val nodeV = nodes.find { it.id == vId }
        val indexOfV = nodes.indexOf(nodeV)
        if (nodeU != null && indexOfU != -1 && nodeV != null && indexOfV != -1) {
            tempNodes[indexOfU] = nodeU.addNeighbors(vId)
        }
        if (undirected) {
            if (nodeV != null && indexOfV != -1 && nodeU != null) {
                tempNodes[indexOfV] = nodeV.addNeighbors(uId)
            }
        }
        return this.copy(nodes = tempNodes)
    }

    fun removeEdge(uId: Int, vId: Int): AdjacencyList<T> {
        val nodes = nodes.map { node ->
            when (node.id) {
                vId -> node.removeNeighbors(uId)
                uId -> node.removeNeighbors(vId)
                else -> node
            }
        }
        //keep edge list up to date
        return this.copy(nodes = nodes)
    }


    private fun getNeighborsByIds(ids: List<Int>): List<GraphCommonNode<T>> {
        val neighbours = mutableListOf<GraphCommonNode<T>>()
        ids.forEach { id ->
            val neighbor = nodes.find { it.id == id }
            if (neighbor != null) {
                neighbours.add(neighbor)
            }
        }
        return neighbours.toList()
    }

}

/*
   Unit test Done
    */
data class Graph22<T>(val undirected: Boolean = true) {
    var adjacencyList = AdjacencyList<T>(undirected = undirected)
        private set
    private var autoGeneratedId = 0
    fun addNode(node: GraphCommonNode<T>) {
        adjacencyList = adjacencyList.addNode(node.changeId(autoGeneratedId++))
    }

    fun removeNode(id: Int) {
        adjacencyList = adjacencyList.removeNode(id)
    }

    fun addEdge(uId: Int, vId: Int) {
        adjacencyList = adjacencyList.addEdge(uId, vId)
    }

    fun removeEdge(uId: Int, vId: Int) {
        adjacencyList = adjacencyList.removeEdge(uId, vId)
    }

}
