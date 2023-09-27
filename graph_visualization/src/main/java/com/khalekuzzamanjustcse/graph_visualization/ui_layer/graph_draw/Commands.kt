package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw

import com.khalekuzzamanjustcse.graph_visualization.command_pattern.Command
import com.khalekuzzamanjustcse.graph_visualization.data_layer.DataLayerGraph
import com.khalekuzzamanjustcse.graph_visualization.data_layer.DataLayerGraphEdge

/*
See the testing in the unit test package
 */
class AddNodeCommand<T>(
    private val node:NodeComposableState,
    private val graph : DataLayerGraph<T>
) : Command {
    override fun execute() {
        graph.addNode(node)
    }

    override fun undo() {
        graph.removeNode(graph.nodesList.lastIndex)
    }

    override fun redo() {
        execute()
    }
}

class RemoveNodeCommand<T>(
    private val nodeId: Int,
    private val graph : DataLayerGraph<T>
) : Command {
    private val nodesList = graph.nodesList
    private val edgesList = graph.edgesList
    override fun execute() {
        graph.removeNode(nodeId)
    }

    override fun undo() {
        graph.setNodes(nodesList)
        graph.setEdges(edgesList)
    }

    override fun redo() {
        execute()
    }

}


class AddEdgeCommand<T>(
    private val edge: DataLayerGraphEdge,
    private val graph : DataLayerGraph<T>
) : Command {

    override fun execute() {
        graph.addEdge(edge)
    }

    override fun undo() {
        graph.removeEdge(graph.edgesList.lastIndex)
    }

    override fun redo() {
        execute()
    }

}

class RemoveEdgeCommand<T>(
    private val edgeId: Int,
    private val graph :DataLayerGraph<T>
) : Command {

    private  val removedEdge=graph.edgesList.last()

    override fun execute() {
        graph.removeEdge(edgeId)
    }

    override fun undo() {
        graph.addEdge(removedEdge)
    }

    override fun redo() {
        execute()
    }

}
