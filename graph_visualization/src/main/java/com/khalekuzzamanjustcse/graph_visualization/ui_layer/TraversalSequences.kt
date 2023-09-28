package com.khalekuzzamanjustcse.graph_visualization.ui_layer

import android.util.Log
import com.khalekuzzamanjustcse.graph_visualization.graph_simulator.Finished
import com.khalekuzzamanjustcse.graph_visualization.graph_simulator.Paused
import com.khalekuzzamanjustcse.graph_visualization.graph_simulator.SelectChild
import com.khalekuzzamanjustcse.graph_visualization.graph_simulator.Simulating
import com.khalekuzzamanjustcse.graph_visualization.graph_simulator.Started

interface SimulationSequence {
    fun  next():Any
}

data class BFSSimulationSequence(
    private val adjacencyListOfNodeReference: Map<Int, List<Int>>,
    private val startNodeIndex: Int,
    private val onUnVisitedNeighborsOrderSelected: () -> List<Int> ={ emptyList() }
) : SimulationSequence {

    private val result = sequence {
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
            if (neighbors != null) {
                for (neighborIndex in neighbors) {
                    if (neighborIndex !in visited) {
                        unVisitedNeighbours.add(neighborIndex)
                    }
                }
            }
            yield(SelectChild(unVisitedNeighbours.distinct()))
            val selectedOrder = onUnVisitedNeighborsOrderSelected()
            Log.i("TRAVERSING:NeighbourReceived", "$selectedOrder")
//            yield(Paused)
            val neighborsSelectModeOn = selectedOrder.isNotEmpty()
            if (neighborsSelectModeOn) {
                for (neighborIndex in selectedOrder) {
                    if (neighborIndex !in visited) {
                        queue.add(neighborIndex)
                        visited.add(neighborIndex)
                    }
                }
            } else {
                if (neighbors != null) {
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
        }
        //when finished
        yield(Finished)
    }
    private val iterator=result.iterator().iterator()

    override fun next(): Any {
        return if(iterator.hasNext()){
            iterator.next()
        } else
            Finished
    }

}

data class DFSSimulationSequence(
    private val adjacencyListOfNodeReference: Map<Int, List<Int>>,
    private val startNodeIndex: Int
) : SimulationSequence {

    private val result = sequence {
        val visited = mutableSetOf<Int>()
        val stack = mutableListOf<Int>()
        stack.add(startNodeIndex)
        visited.add(startNodeIndex)
        yield(Started)
        while (stack.isNotEmpty()) {
            val currentNodeIndex = stack.removeAt(stack.size - 1)
            yield(
                Simulating(
                    processingNodeIndex = currentNodeIndex,
                    newlyPushedNodesIndices = emptyList(),
                    futureProcessingNodeIndices = stack
                )
            )
            //
            val neighbors = adjacencyListOfNodeReference[currentNodeIndex]
            val unvisitedNeighbours = mutableListOf<Int>()
            if (neighbors != null) {
                for (neighborIndex in neighbors) {
                    if (neighborIndex !in visited) {
                        unvisitedNeighbours.add(neighborIndex)
                    }
                }
            }
            yield(SelectChild(unvisitedNeighbours.distinct()))
            val neighborsSelectModeOn = unvisitedNeighbours.isNotEmpty()
            if (neighborsSelectModeOn) {
                for (neighborIndex in unvisitedNeighbours) {
                    stack.add(neighborIndex)
                    visited.add(neighborIndex)
                }
            }
        }
        // When finished
        yield(Finished)
    }

    private val iterator = result.iterator().iterator()

    override fun next(): Any {
        return if (iterator.hasNext()) {
            iterator.next()
        } else
            Finished
    }
}


