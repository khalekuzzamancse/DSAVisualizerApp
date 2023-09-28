package com.khalekuzzamanjustcse.graph_visualization.data_layer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface GraphCommonNode<T> {
    val data: T
    val id: Int
    val label: String
    val offset: Offset
    val neighborsId: List<Int>
    fun addNeighbors(id: Int): GraphCommonNode<T>
    fun removeNeighbors(id: Int): GraphCommonNode<T>
    fun changeId(newId: Int): GraphCommonNode<T>
    fun addToOffset(amount: Offset): GraphCommonNode<T>
}

@Immutable
data class GraphNode<T>(
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

    override fun addToOffset(amount: Offset): GraphCommonNode<T> {
        return this.copy(offset = offset + amount)
    }
}

@Immutable
data class AdjacencyList<T>(
    val undirected: Boolean = true,
    val nodes: List<GraphCommonNode<T>> = emptyList()
) {


    //manipulating nodes
    fun addOffset(nodeId: Int, amount: Offset): AdjacencyList<T> {
        val tempNodes = nodes.toMutableList()
        val node = nodes.find { it.id == nodeId }
        val index = nodes.indexOf(node)
        if (node != null && index != -1) {
            tempNodes[index] = node.addToOffset(amount)
        }
        return this.copy(nodes = tempNodes)

    }

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

/*
   Unit test Done
    */
data class Graph<T>(val undirected: Boolean = true) {
    var adjacencyList = AdjacencyList<T>(undirected = undirected)
        private set
    private val lock=Any()

    private val _nodes = MutableStateFlow(adjacencyList.nodes)
    private val _edges = MutableStateFlow(adjacencyList.edges)
    val nodes=_nodes.asStateFlow()
    val edges=_edges.asStateFlow()

    fun setNodes(nodes: List<GraphCommonNode<T>>) {
        synchronized(lock) {
           nodes.forEach {
               addNode(it)
           }
        }
    }

    fun setEdges(edges: List<Pair<Int,Int>>) {
        synchronized(lock) {
           edges.forEach {(u,v) ->
               addEdge(u,v)
           }
        }
    }

    fun addNode(node: GraphCommonNode<T>) {
        adjacencyList = adjacencyList.addNode(node)
        _nodes.update { adjacencyList.nodes }
    }
    fun onNodeDrag(id:Int,amount: Offset){
       adjacencyList= adjacencyList.addOffset(id,amount)
        _nodes.update { adjacencyList.nodes }
        _edges.update { adjacencyList.edges}
    }

    fun removeNode(id: Int) {
        adjacencyList = adjacencyList.removeNode(id)
        _nodes.update { adjacencyList.nodes }
        //there may need to remove some edge
        _edges.update { adjacencyList.edges}
    }

    fun addEdge(uId: Int, vId: Int) {
        adjacencyList = adjacencyList.addEdge(uId, vId)
        _edges.update { adjacencyList.edges}
    }

    fun removeEdge(uId: Int, vId: Int) {
        adjacencyList = adjacencyList.removeEdge(uId, vId)
        _edges.update { adjacencyList.edges}
    }

}
