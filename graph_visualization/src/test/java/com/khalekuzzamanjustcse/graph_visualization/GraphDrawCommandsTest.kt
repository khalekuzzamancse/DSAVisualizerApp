package com.khalekuzzamanjustcse.graph_visualization

import com.khalekuzzamanjustcse.graph_visualization.data_layer.DataLayerGraph
import com.khalekuzzamanjustcse.graph_visualization.data_layer.DataLayerGraphEdge
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.AddEdgeCommand
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.AddNodeCommand
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.RemoveEdgeCommand
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.RemoveNodeCommand
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CommandTest {

    val graph =DataLayerGraph<String>()

    private fun printGraph(){
        println("Nodes: ${graph.nodesList.map { it.data }}")
        println("Adjacency: ")
        graph.printAdjacency()
    }
    @Before
    fun  setup() {
        graph.addNode("A")
        graph.addNode("B")
        graph.addNode("C")
        graph.addNode("D")
    }


    @Test
    fun testAddNodeCommand() {
        val addNodeCommand = AddNodeCommand("E", graph)
        addNodeCommand.execute()
        printGraph()
        assertEquals(5, graph.nodesList.size)
        addNodeCommand.undo()
        assertEquals(4, graph.nodesList.size)
        printGraph()
        addNodeCommand.redo()
        assertEquals(5, graph.nodesList.size)
        printGraph()
    }
    @Test
    fun testRemoveNodeCommand() {
        printGraph()
        val removeNodeCommand = RemoveNodeCommand(0,graph)
        removeNodeCommand.execute()
        assertEquals(3, graph.nodesList.size)
        printGraph()
        removeNodeCommand.undo()
        assertEquals(4, graph.nodesList.size)
        printGraph()
        removeNodeCommand.redo()
        assertEquals(3, graph.nodesList.size)
        printGraph()
    }
    @Test
    fun testAddEdgeCommand() {

        printGraph()
        val edge = DataLayerGraphEdge(0, 1)
        val addEdgeCommand = AddEdgeCommand(edge, graph)
        addEdgeCommand.execute()

        assertEquals(1, graph.edgesList.size)
        printGraph()

        addEdgeCommand.undo()
        assertEquals(0, graph.edgesList.size)
        printGraph()
        addEdgeCommand.redo()
        assertEquals(1, graph.edgesList.size)
        printGraph()
    }
    @Test
    fun testRemoveEdgeCommand() {
        val edge = DataLayerGraphEdge(0, 1)
        val addEdgeCommand = AddEdgeCommand(edge, graph)
        addEdgeCommand.execute()
        printGraph()
        val removeEdgeCommand = RemoveEdgeCommand(0,graph)
        removeEdgeCommand.execute()
        assertEquals(0, graph.edgesList.size)
        printGraph()

        removeEdgeCommand.undo()
        assertEquals(1, graph.edgesList.size)
        printGraph()

        removeEdgeCommand.redo()
        assertEquals(0, graph.edgesList.size)
        printGraph()

    }

}
