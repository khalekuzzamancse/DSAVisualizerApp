package com.khalekuzzamanjustcse.graph_visualization.ui_layer.screen

import com.khalekuzzamanjustcse.common_ui.queue_stack.Screen

object UndirectedTraversalScreen : Screen {
    override val route: String
        get() = "UnDirected Traversal Screen"
}

object DirectedTraversalScreen : Screen {
    override val route: String
        get() = "Directed Traversal Screen"
}

object OtherScreen : Screen {
    override val route: String
        get() = "Other Screen"
}


