package com.khalekuzzamanjustcse.graph_visualization

import com.khalekuzzamanjustcse.common_ui.DropdownMenuOption


sealed class GraphTraversalOption(private val algorithmName: String) : DropdownMenuOption {
    override val label: String
        get() = algorithmName
}

object BFS : GraphTraversalOption("BFS")
object DFS : GraphTraversalOption("DFS")

object TraversalOptions {
    val options = listOf(BFS, DFS)

    fun getOption(index: Int) = options[index]
}