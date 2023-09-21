package com.khalekuzzamanjustcse.graph_visualization

import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNode


fun bfs(
    adjacencyListOfNodeReference: List<List<Int>>,
    startNodeIndex: Int = 0,
    onNodeProcessing: (nodeReference: Int) -> Unit={}
)= sequence {
    val visited = mutableSetOf<Int>()
    val queue = mutableListOf<Int>()
    queue.add(startNodeIndex)
    visited.add(startNodeIndex)
    while (queue.isNotEmpty()) {
        val currentNodeIndex = queue.removeAt(0)
       // onNodeProcessing(currentNodeIndex)

        val neighbors = adjacencyListOfNodeReference[currentNodeIndex]
        for (neighborIndex in neighbors) {
            if (neighborIndex !in visited) {
                queue.add(neighborIndex)
                visited.add(neighborIndex)
            }
        }
        yield(currentNodeIndex)
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
