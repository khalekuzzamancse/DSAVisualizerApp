package com.khalekuzzamanjustcse.graph_visualization.graph_simulator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.GraphDrawer
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.ControlButton
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorComposable
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorResult
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class GraphTraversalScreenState(
    val size: Dp = 50.dp,
    val sizePx: Float = 0f,
) {
    private val _inputMode = MutableStateFlow(true)
    val inputMode = _inputMode.asStateFlow()

    private val _simulationState = MutableStateFlow(GraphSimulatorState(GraphEditorResult()))
    var simulationState =_simulationState.asStateFlow()

    val editorState = GraphEditorState(size, sizePx,
        onInputComplete = { sState ->
            _simulationState.update { GraphSimulatorState(sState) }
            _inputMode.update { false }
        }
    )

    val controls = listOf(
        object : ControlButton {
            override val icon = Icons.Filled.NextPlan
            override val label = "Next"
            override val enabled = mutableStateOf(true)
            override fun onClick() = simulationState.value.onNext()
        },
        object : ControlButton {
            override val icon = Icons.Filled.Code
            override val label = "Pseudocode Execution"
            override val enabled = mutableStateOf(true)
            override fun onClick() {
            }
        },
        object : ControlButton {
            override val icon = Icons.Filled.Memory
            override val label = "Variables Status"
            override val enabled = mutableStateOf(true)
            override fun onClick() {
            }
        }
    )

    fun onTraversalChanged(index: Int) =
        simulationState.value.onTraversalChanged(TraversalOptions.getOption(index))
}

@Preview
@Composable
fun GraphTraversalPreview() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density
    val screenState = remember {
        GraphTraversalScreenState(size, sizePx)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (screenState.inputMode.collectAsState().value) {
            GraphEditorComposable(
                modifier = Modifier.matchParentSize(),
                state = screenState.editorState,
                onDone = {
                  screenState.editorState.onDone()
                }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                GraphTraversalScreenControl(
                    title = "GraphTraversal",
                    controls = screenState.controls,
                    dropdownMenuOptions = TraversalOptions.options,
                    onOptionSelected = screenState::onTraversalChanged
                )
                Spacer(modifier = Modifier.height(4.dp))
                GraphDrawer(
                    nodes = screenState.simulationState.collectAsState().value.nodes.collectAsState().value,
                    edges = screenState.simulationState.collectAsState().value.edges.collectAsState().value,
                )
            }
        }
    }


}