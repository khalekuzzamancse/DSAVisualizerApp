package com.khalekuzzamanjustcse.graph_visualization

import com.khalekuzzamanjustcse.graph_visualization.data_layer.AdjacencyList
import com.khalekuzzamanjustcse.graph_visualization.data_layer.Graph22
import com.khalekuzzamanjustcse.graph_visualization.data_layer.GraphNode
import org.junit.Before
import org.junit.Test

class AdjacencyListTest {
    private var adjacencyList = AdjacencyList<String>()
    private fun printGraph() {
        println("Nodes : ${adjacencyList.nodes.map { it.label }}")
        println("Edges :\n${adjacencyList.edges.map { " (${it.first.label} ---> ${it.second.label} ) " }}")
    }

    @Before
    fun setup() {
        adjacencyList = adjacencyList.addNode(GraphNode("A", id = 1))
        adjacencyList = adjacencyList.addNode(GraphNode("B", id = 2))
        adjacencyList = adjacencyList.addNode(GraphNode("C", id = 3))
        adjacencyList = adjacencyList.addNode(GraphNode("D", id = 4))
        adjacencyList = adjacencyList.addNode(GraphNode("E", id = 5))
        adjacencyList = adjacencyList.addEdge(1, 2)
        adjacencyList = adjacencyList.addEdge(2, 3)
        printGraph()

    }

    @Test
    fun test() {
        adjacencyList = adjacencyList.removeEdge(1, 2)
        printGraph()

    }

    @Test
    fun removeNode() {
        adjacencyList = adjacencyList.removeNode(1)
        printGraph()
    }


}

class GraphTUnitTest {
    private val graph = Graph22<String>()
    private fun printGraph() {
        println("Nodes : ${graph.adjacencyList.nodes.map { it.label }}")
        println("Edges :\n${graph.adjacencyList.edges.map { " ( ${it.first.label} ---> ${it.second.label}" }} ) ")
    }

    @Before
    fun setUp() {
        graph.addNode(GraphNode("A"))
        graph.addNode(GraphNode("B"))
        graph.addNode(GraphNode("C"))
        graph.addNode(GraphNode("D"))
        graph.addNode(GraphNode("E"))
        graph.addEdge(0, 1)
        graph.addEdge(0, 2)
        graph.addEdge(2, 3)
        printGraph()
    }

    @Test
    fun testAddEdge() {
        val ids = graph.adjacencyList.nodes.map { it.id }
        println("ids : $ids")
        printGraph()
    }

    @Test
    fun removeNode() {
        graph.removeNode(0)
        printGraph()
    }


}