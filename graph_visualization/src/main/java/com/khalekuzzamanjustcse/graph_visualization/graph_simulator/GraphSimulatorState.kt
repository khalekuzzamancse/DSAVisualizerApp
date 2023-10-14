//package com.khalekuzzamanjustcse.graph_visualization.graph_simulator
//
//import androidx.compose.ui.graphics.Color
//import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorResultImp
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.BFSSimulationSequence
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.DFSSimulationSequence
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.SimulationSequence
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.NodeComposableState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//data class GraphSimulatorState(
//    private val result: GraphEditorResultImp
//) {
//    private val _nodes = MutableStateFlow(
//        result.nodes.map {
//            NodeComposableState(it)
//        }
//    )
//    val nodes=_nodes.asStateFlow()
//
//    private val _edges = MutableStateFlow(result.edges)
//    val edges = _edges.asStateFlow()
//    private val adjacencyList = result.adjacencyList
//    private var sequence: SimulationSequence? = null
//
//    private fun processing(id: Int) {
//        _nodes.update { list ->
//            list.map { if (it.id == id) it.copy(color = Color.Red) else it }
//        }
//    }
//
//
//    fun onNext(onProcessing:(SimulationResult)->Unit={}) {
//        when (val res = sequence?.next()) {
//            is Simulating -> {
//                processing(res.processingNodeIndex)
//                onProcessing(res)
//            }
//
//            is Started -> {
//            }
//
//            is Finished -> {
//            }
//        }
//    }
//
//    fun onTraversalChanged(traversal: GraphTraversalOption) {
//        _nodes.update { list ->
//            list.map { it.copy(color = Color.Green) }
//        }
//        sequence = when (traversal) {
//            is BFS -> { BFSSimulationSequence(adjacencyList, nodes.value[0].id) }
//            is DFS -> {DFSSimulationSequence(adjacencyList, nodes.value[0].id) }
//        }
//    }
//
//    init {
//        if (nodes.value.isNotEmpty()) {
//            sequence = BFSSimulationSequence(adjacencyList, nodes.value[0].id)
//        }
//
//    }
//}
//
//sealed class SimulationResult
//object Finished : SimulationResult()
//object Started : SimulationResult()
//object  Paused : SimulationResult()
//data class SelectChild(val unVisitedNeighboursRef: List<Int>) : SimulationResult()
//data class Simulating(
//    val processingNodeIndex: Int,
//    val futureProcessingNodeIndices: List<Int>,
//    val newlyPushedNodesIndices: List<Int>,
//    val dequeue:Boolean = false,
//    val intermediateDS: DataStructure = DataStructure.Queue
//) : SimulationResult()
//
//
//enum class DataStructure {
//    Stack, Queue
//}