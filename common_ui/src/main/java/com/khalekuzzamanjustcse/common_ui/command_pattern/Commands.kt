package com.khalekuzzamanjustcse.common_ui.command_pattern

import com.khalekuzzamanjustcse.common_ui.graph_ed.GraphBasicNode
import com.khalekuzzamanjustcse.common_ui.graph_ed.GraphEditor


/*
See the testing in the unit test package
 */
class AddNodeCommand(
    private val node: GraphBasicNode,
    private val graph: GraphEditor,
) : Command {
    val nodes = graph.nodes.map { it }.toSet()

    override fun execute() = graph.addNode(node)
    override fun undo() {
        graph.resetGraph(nodes)
    }

    override fun redo() = execute()
}


class AddEdgeCommand(
    private val u: GraphBasicNode,
    private val v: GraphBasicNode,
    private val graph: GraphEditor,
) : Command {
    override fun execute()=graph.addEdge(u, v)
    override fun undo()=graph.removeEdge(u, v)
    override fun redo()=execute()
}
