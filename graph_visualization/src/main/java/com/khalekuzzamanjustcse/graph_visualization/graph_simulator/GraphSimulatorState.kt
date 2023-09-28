package com.khalekuzzamanjustcse.graph_visualization.graph_simulator

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.BFSSimulationSequence
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.SimulationSequence
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GraphSimulatorState(
    private val result: GraphEditorResult
) {
    private val _nodes = MutableStateFlow(result.nodes)
    val nodes = _nodes.asStateFlow()
    private val _edges = MutableStateFlow(result.edges)
    val edges = _edges.asStateFlow()
    private val adjacencyList = result.adjacencyList
    var bfsSequence: SimulationSequence? = null
    fun processing(id: Int) {
        _nodes.update { list ->
            list.map { if (it.id == id) it.copy(color = Color.Red) else it }
        }
    }

    init {
        if (nodes.value.isNotEmpty()) {
            bfsSequence = BFSSimulationSequence(adjacencyList, nodes.value[0].id)
        }


    }
}

sealed class SimulationResult
object Finished : SimulationResult()
object Started : SimulationResult()
object Paused : SimulationResult()
data class SelectChild(val unVisitedNeighboursRef: List<Int>) : SimulationResult()
data class Simulating(
    val processingNodeIndex: Int,
    val futureProcessingNodeIndices: List<Int>,
    val newlyPushedNodesIndices: List<Int>,
    val intermediateDS: DataStructure = DataStructure.Queue
) : SimulationResult()


enum class DataStructure {
    Stack, Queue
}