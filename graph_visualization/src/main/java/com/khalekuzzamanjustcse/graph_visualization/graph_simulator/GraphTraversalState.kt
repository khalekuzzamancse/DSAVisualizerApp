//package com.khalekuzzamanjustcse.graph_visualization.graph_simulator
//
//import android.util.Log
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Code
//import androidx.compose.material.icons.filled.Memory
//import androidx.compose.material.icons.filled.NextPlan
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.khalekuzzamanjustcse.common_ui.ControlButton
//import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorResult
//import com.khalekuzzamanjustcse.common_ui.queue_stack.QueueState
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.NodeComposableState
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_editor.GraphEditorResult
//import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_editor.GraphEditorState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//class GraphTraversalState(
//    val size: Dp = 50.dp,
//    private val sizePx: Float = 0f,
//
//    ) {
//    private val _inputMode = MutableStateFlow(true)
//    val inputMode = _inputMode.asStateFlow()
//    private val _showVariableState = MutableStateFlow(true)
//    val showVariableState = _showVariableState.asStateFlow()
//    private val _graphCanvasHeight = MutableStateFlow(50.dp)
//    val graphCanvasHeightPx = _graphCanvasHeight.asStateFlow()
//    private val _currentProcessingNode = MutableStateFlow("")
//    val currentProcessingNode = _currentProcessingNode.asStateFlow()
//
//    var simulationState = GraphSimulatorState(GraphEditorResult())
//    val editorState = GraphEditorState(size, sizePx,
//        onInputComplete = { sState ->
//            simulationState = GraphSimulatorState(sState)
//            _inputMode.update { false }
//            val height = (sState.maxYOffsetNode + sizePx)
//            _graphCanvasHeight.update { height.toInt().dp }
//        }
//    )
//    var queueState = QueueState(size, sizePx = sizePx)
//
//
//    val controls = listOf(
//        object : ControlButton {
//            override val icon = Icons.Filled.NextPlan
//            override val label = "Next"
//            override val enabled = mutableStateOf(true)
//            override fun onClick() {
//                simulationState.onNext { result ->
//                    when (result) {
//                        is Simulating -> {
//                            Log.i("SimulationResult", "$result")
//                            if (result.dequeue) {
//                                queueState.dequeue()
//                            }
//                            _currentProcessingNode.update {
//                                simulationState.nodes.value
//                                    .find { it.id == result.processingNodeIndex }?.label ?: ""
//                            }
//                            result.newlyPushedNodesIndices.forEach { nodeId ->
//                                val node = simulationState.nodes.value.find { it.id == nodeId }
//                                if (node != null) {
//                                    queueState.enqueue(node.label)
//                                }
//
//                            }
//                        }
//
//                        else -> {}
//                    }
//                }
//
//            }
//        },
//        object : ControlButton {
//            override val icon = Icons.Filled.Code
//            override val label = "Pseudocode Execution"
//            override val enabled = mutableStateOf(true)
//            override fun onClick() {
//            }
//        },
//        object : ControlButton {
//            override val icon = Icons.Filled.Memory
//            override val label = "Variables Status"
//            override val enabled = mutableStateOf(true)
//            override fun onClick() {
//                _showVariableState.value = !_showVariableState.value
//            }
//        }
//    )
//
//    fun onTraversalChanged(index: Int) {
//        simulationState.onTraversalChanged(TraversalOptions.getOption(index))
//        queueState = QueueState(size, sizePx)
//    }
//
//}
