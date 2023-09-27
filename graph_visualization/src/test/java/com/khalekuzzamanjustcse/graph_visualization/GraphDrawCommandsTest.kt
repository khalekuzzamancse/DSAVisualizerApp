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

    private fun printGraph(graph :DataLayerGraph<String>){
        println("Nodes: ${graph.nodesList.map { it.data }}")
        println("Adjacency: ")
        graph.printAdjacency()
    }

    @Test
    fun testAddNodeCommand() {
        val graph =DataLayerGraph<String>()
        val addNodeCommand = AddNodeCommand("Dhaka", graph)
        addNodeCommand.execute()
        printGraph(graph)
        assertEquals(1, graph.nodesList.size)
        addNodeCommand.undo()
        assertEquals(0, graph.nodesList.size)
        printGraph(graph)
        addNodeCommand.redo()
        assertEquals(1, graph.nodesList.size)
        printGraph(graph)
    }
    @Test
    fun testRemoveNodeCommand() {
        val graph =DataLayerGraph<String>()
        val commands= listOf(
            AddNodeCommand("Dhaka",graph),
            AddNodeCommand("Delhi",graph),
        )
        commands.forEach { it.execute() }

        printGraph(graph)
        val removeNodeCommand = RemoveNodeCommand(0,graph)
        removeNodeCommand.execute()
        assertEquals(1, graph.nodesList.size)
        printGraph(graph)
        removeNodeCommand.undo()
        assertEquals(2, graph.nodesList.size)
        printGraph(graph)
        removeNodeCommand.redo()
        assertEquals(1, graph.nodesList.size)
        printGraph(graph)
    }
    @Test
    fun testAddEdgeCommand() {
        val graph =DataLayerGraph<String>()
        val commands= listOf(
            AddNodeCommand("Dhaka",graph),
            AddNodeCommand("Delhi",graph),
            AddNodeCommand("Lahore",graph),
        )
        commands.forEach { it.execute() }
        printGraph(graph)
        val edge = DataLayerGraphEdge(0, 1)
        val addEdgeCommand = AddEdgeCommand(edge, graph)
        addEdgeCommand.execute()

        assertEquals(1, graph.edgesList.size)
        printGraph(graph)

        addEdgeCommand.undo()
        assertEquals(0, graph.edgesList.size)
        printGraph(graph)
        addEdgeCommand.redo()
        assertEquals(1, graph.edgesList.size)
        printGraph(graph)
    }
    @Test
    fun testRemoveEdgeCommand() {
        val graph =DataLayerGraph<String>()
        val commands= listOf(
            AddNodeCommand("Dhaka",graph),
            AddNodeCommand("Delhi",graph),
            AddNodeCommand("Lahore",graph),
            AddEdgeCommand(DataLayerGraphEdge(0,1), graph)
        )
        commands.forEach { it.execute() }
        printGraph(graph)
        val removeEdgeCommand = RemoveEdgeCommand(0,graph)
        removeEdgeCommand.execute()
        assertEquals(0, graph.edgesList.size)
        printGraph(graph)

        removeEdgeCommand.undo()
        assertEquals(1, graph.edgesList.size)
        printGraph(graph)

        removeEdgeCommand.redo()
        assertEquals(0, graph.edgesList.size)
        printGraph(graph)

    }

}
