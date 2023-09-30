package com.khalekuzzamanjustcse.common_ui.command_pattern

import com.khalekuzzamanjustcse.common_ui.command_pattern.Command
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditor
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorNode


/*
See the testing in the unit test package
 */
//class AddNodeCommand(
//    private val value: String,
//    private val node: GraphEditorNode,
//    private val graph: GraphEditor,
//) : Command {
//    val nodes = graph.nodes.value
//    val edges = graph._edges
//    override fun execute() = graph.addNode(value)
//    override fun undo() {
//
//    }
//
//    override fun redo() = execute()
//}
////
//
//class AddEdgeCommand<T>(
//    private val uId:Int,
//    private val  vId:Int,
//    private val graph : GraphEditor,
//) : Command {
//    override fun execute()=graph.addEdge(uId, vId)
//    override fun undo()=graph.removeEdge(uId, vId)
//    override fun redo()=execute()
//}
