package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_simulator.GraphSimulatorState
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.GraphDrawer
import kotlin.random.Random

private val random
    get() = Random.nextInt(99)


@Preview
@Composable
fun GraphMakerComposablePreview() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density
    val result = remember {
      mutableStateOf(  GraphEditorResult())
    }
    val state = remember {
        GraphEditorState(size, sizePx) {
            result.value = it
            GraphSimulatorState(it)
        }
    }
    val inputModeOn = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (inputModeOn.value) {
            GraphEditorComposable(
                modifier = Modifier.matchParentSize(),
                state = state
            ) {
                inputModeOn.value = false
                state.onDone()
            }
        } else {
            Column (modifier = Modifier.fillMaxSize()){
                Button(onClick = {
                    inputModeOn.value = !inputModeOn.value
                }) {
                    Text(text = "Go to Graph Editor")
                }
                GraphDrawer(nodes = result.value.nodes, edges = result.value.edges)
            }
        }


    }


}

@Composable
fun GraphEditorComposable(
    modifier: Modifier = Modifier,
    state: GraphEditorState,
    onDone: () -> Unit,
) {

    val enabled = remember {
        mutableStateOf(true)
    }
    val controls = listOf(
        object : ControlButton {
            override val icon = Icons.Filled.Grain
            override val label = "Add Node"
            override val enabled = enabled
            override fun onClick() {
                state.addNode("$random")
            }
        },

        object : ControlButton {
            override val icon = Icons.Filled.LinearScale
            override val label = "Add Edge"
            override val enabled = enabled
            override fun onClick() = state.addEdge()
        },
        object : ControlButton {
            override val icon = Icons.Filled.Undo
            override val label = "Undo"
            override val enabled = enabled
            override fun onClick() = state.undo()
        },
        object : ControlButton {
            override val icon = Icons.Filled.Redo
            override val label = "Redo"
            override val enabled = enabled
            override fun onClick() = state.redo()
        },
        object : ControlButton {
            override val icon = Icons.Filled.DoneOutline
            override val label = "Done"
            override val enabled = enabled
            override fun onClick() = onDone()
        },
    )
    Column(modifier = modifier.fillMaxSize()) {
        ControlsComposable(title = "Graph Editor", controls = controls)
        Spacer(modifier = Modifier.height(20.dp))
        GraphDrawer(
            nodes = state.nodes.collectAsState().value,
            edges = state.edges.collectAsState().value,
            onDrag = state::onDrag,
            onClick = state::onNodeClick,
            onCanvasTapped = state::onCanvasTapped
        )
    }


}
