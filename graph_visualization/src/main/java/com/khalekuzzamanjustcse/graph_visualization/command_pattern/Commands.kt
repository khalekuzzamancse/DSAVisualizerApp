package com.khalekuzzamanjustcse.graph_visualization.command_pattern

import com.khalekuzzamanjustcse.graph_visualization.data_layer.Graph22
import com.khalekuzzamanjustcse.graph_visualization.data_layer.GraphNode

/*
See the testing in the unit test package
 */
class AddNodeCommand<T>(
    private val node:GraphNode<T>,
    private val graph : Graph22<T>
) : Command {
    override fun execute()=graph.addNode(node)
    override fun undo()=graph.removeNode(node.id)
    override fun redo()=execute()
}

class RemoveNodeCommand<T>(
    private val nodeId: Int,
    private val graph : Graph22<T>
) : Command {
    private val nodesList = graph.nodes.value
    private val edgesList = graph.edges.value
    override fun execute() {
        graph.removeNode(nodeId)
    }

    override fun undo() {
        graph.setNodes(nodesList)
        graph.setEdges(edgesList.map { it.first.id to it.second.id })
    }
    override fun redo() {
        execute()
    }

}


class AddEdgeCommand<T>(
    private val uId:Int,
    private val  vId:Int,
    private val graph : Graph22<T>
) : Command {
    override fun execute()=graph.addEdge(uId, vId)
    override fun undo()=graph.removeEdge(uId, vId)
    override fun redo()=execute()
}
