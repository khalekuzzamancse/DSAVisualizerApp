package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNode


sealed class SimulationState
object Finished : SimulationState()
object Started : SimulationState()
object Paused : SimulationState()
data class SelectChild(val unVisitedNeighboursRef: List<Int>) : SimulationState()
data class Simulating(
    val processingNodeIndex: Int,
    val futureProcessingNodeIndices: List<Int>,
    val newlyPushedNodesIndices: List<Int>,
    val intermediateDS: DataStructure = DataStructure.Queue
) : SimulationState()


enum class DataStructure {
    Stack, Queue
}


fun bfs(
    adjacencyListOfNodeReference: List<List<Int>>,
    startNodeIndex: Int = 0,
    onUnVisitedNeighborsOrderSelected: () -> List<Int> = { emptyList() }
) = sequence {
    val visited = mutableSetOf<Int>()
    val queue = mutableListOf<Int>()
    queue.add(startNodeIndex)
    visited.add(startNodeIndex)
    yield(Started)
    while (queue.isNotEmpty()) {
        val currentNodeIndex = queue.removeAt(0)
        yield(
            Simulating(
                processingNodeIndex = currentNodeIndex,
                newlyPushedNodesIndices = emptyList(),
                futureProcessingNodeIndices = queue
            )
        )
        //
        val neighbors = adjacencyListOfNodeReference[currentNodeIndex]
        val addedInQueue = mutableListOf<Int>()

        //finding unvisited neighbours
        val unVisitedNeighbours = mutableListOf<Int>()
        for (neighborIndex in neighbors) {
            if (neighborIndex !in visited) {
                unVisitedNeighbours.add(neighborIndex)
            }
        }
        yield(SelectChild(unVisitedNeighbours.distinct()))
        val selectedOrder = onUnVisitedNeighborsOrderSelected()
        Log.i("TRAVERSING:NeighbourReceived", "$selectedOrder")
        yield(Paused)
        yield(Paused)
        val neighborsSelectModeOn = selectedOrder.isNotEmpty()
        if (neighborsSelectModeOn) {
            for (neighborIndex in selectedOrder) {
                if (neighborIndex !in visited) {
                    queue.add(neighborIndex)
                    visited.add(neighborIndex)
                }
            }
        } else {
            for (neighborIndex in neighbors) {
                if (neighborIndex !in visited) {
                    queue.add(neighborIndex)
                    visited.add(neighborIndex)
                    //
                    addedInQueue.add(neighborIndex)
                }
            }
        }


    }
    //when finished
    yield(Finished)
}

