package com.khalekuzzamanjustcse.graph_visualization

import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNode


sealed class SimulationState
object Finished : SimulationState()
object Started : SimulationState()
enum class DataStructure {
    Stack, Queue
}

data class Simulating(
    val processingNodeIndex: Int,
    val futureProcessingNodeIndices: List<Int>,
    val newlyPushedNodesIndices: List<Int>,
    val intermediateDS: DataStructure = DataStructure.Queue
) : SimulationState()


fun bfs(
    adjacencyListOfNodeReference: List<List<Int>>,
    startNodeIndex: Int = 0,
) = sequence {
    val visited = mutableSetOf<Int>()
    val queue = mutableListOf<Int>()
    queue.add(startNodeIndex)
    visited.add(startNodeIndex)
    //emit the current states
    yield(Started)
    //emit done

    while (queue.isNotEmpty()) {
        val currentNodeIndex = queue.removeAt(0)
        val neighbors = adjacencyListOfNodeReference[currentNodeIndex]
        val addedInQueue = mutableListOf<Int>()
        for (neighborIndex in neighbors) {
            if (neighborIndex !in visited) {
                queue.add(neighborIndex)
                visited.add(neighborIndex)
                //
                addedInQueue.add(neighborIndex)
            }
        }
        yield(
            Simulating(
                processingNodeIndex = currentNodeIndex,
                newlyPushedNodesIndices = addedInQueue,
                futureProcessingNodeIndices = queue
            )
        )
    }
    //when finished
    yield(Finished)
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
