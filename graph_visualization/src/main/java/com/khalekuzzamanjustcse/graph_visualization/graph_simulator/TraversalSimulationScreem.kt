package com.khalekuzzamanjustcse.graph_visualization.graph_simulator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.queue_stack.QueueVisualizationScreen
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.GraphDrawer
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_editor.GraphEditorComposable


@Preview
@Composable
fun TraversalPreview() {
    val size = 50.dp
    val density = LocalDensity.current.density
    val sizePx = size.value * density
    val screenState = remember {
        GraphTraversalScreenViewModel(size, sizePx)
    }
    GraphTraversalScreen(screenState)
}

@Composable
fun GraphTraversalScreen(
    screenState: GraphTraversalScreenViewModel
) {
    val density = LocalDensity.current.density
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (screenState.inputMode.collectAsState().value) {
            GraphEditorComposable(
                modifier = Modifier.matchParentSize(),
                state = screenState.editorState,
                onDone = {
                    screenState.editorState.onDone()
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GraphTraversalScreenControl(
                    title = "GraphTraversal",
                    controls = screenState.controls,
                    dropdownMenuOptions = TraversalOptions.options,
                    onOptionSelected = screenState::onTraversalChanged
                )
                Spacer(modifier = Modifier.height(4.dp))
                GraphDrawer(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(
                            (screenState.graphCanvasHeightPx.collectAsState().value) / density
                        ),
                    nodes = screenState.simulationState.nodes.collectAsState().value,
                    edges = screenState.simulationState.edges.collectAsState().value,
                )
                if (screenState.showVariableState.collectAsState().value) {
                    QueueVisualizationScreen(
                        screenState.queueState
                    )
                }

            }
        }
    }


}