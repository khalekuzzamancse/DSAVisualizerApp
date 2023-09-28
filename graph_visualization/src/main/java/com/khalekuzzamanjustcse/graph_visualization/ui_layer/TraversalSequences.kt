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

