package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNode


fun <T> bfsTraversal(startNode: GraphNode<T>, visitAction: (GraphNode<T>) -> Unit={}) {
    val visited = mutableSetOf<GraphNode<T>>()
    val queue = mutableListOf<GraphNode<T>>()
    queue.add(startNode)
    visited.add(startNode)
    while (queue.isNotEmpty()) {
        val currentNode = queue.removeAt(0)
//        visitAction(currentNode)
       // Log.i("VisitingNode:","${currentNode.value}")
//        for (neighbor in currentNode.neighbors) {
//            if (neighbor !in visited) {
//                queue.add(neighbor)
//                visited.add(neighbor)
//            }
       // }
    }
}

fun <T> dfsTraversal(startNode: GraphNode<T>, visitAction: (GraphNode<T>) -> Unit) {
    val visited = mutableSetOf<GraphNode<T>>()
    val stack = mutableListOf<GraphNode<T>>()

    stack.add(startNode)
    visited.add(startNode)

    while (stack.isNotEmpty()) {
        val currentNode = stack.removeAt(stack.size - 1)
        visitAction(currentNode)

//        for (neighbor in currentNode.neighbors.asReversed()) {
//            if (neighbor !in visited) {
//                stack.add(neighbor)
//                visited.add(neighbor)
//            }
//        }
    }
}
