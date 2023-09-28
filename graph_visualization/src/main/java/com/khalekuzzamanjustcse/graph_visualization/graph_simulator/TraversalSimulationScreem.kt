package com.khalekuzzamanjustcse.graph_visualization.graph_simulator

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw.GraphDrawer
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorComposable
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorResult
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.GraphEditorState
import kotlin.random.Random

private val random
    get() = Random.nextInt(99)


@Preview
@Composable
fun GraphTraversalPreview() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density
    val result = remember {
        mutableStateOf(GraphEditorResult())
    }
    val simulationState = remember {
        mutableStateOf(GraphSimulatorState(GraphEditorResult()))
    }
    val state = remember {
        GraphEditorState(size, sizePx) {
            result.value = it
            simulationState.value = GraphSimulatorState(it)
        }
    }
    val inputModeOn = remember {
        mutableStateOf(false)
    }
    var hideButton by remember {
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
                hideButton = true
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                if (!hideButton) {
                    Button(onClick = {
                        inputModeOn.value = !inputModeOn.value
                    }) {
                        Text(text = "Go to Graph Editor")
                    }
                }
                Button(onClick = {
                    when (val res = simulationState.value.bfsSequence?.next()) {
                        is Simulating -> {
                            Log.i("Simulating:Procesing", "${res.processingNodeIndex}")
                            simulationState.value.processing(res.processingNodeIndex)
                        }

                        is Started -> {
                            Log.i("Simulating:Procesing", "Started")
                        }

                        is Finished -> {
                            Log.i("Simulating:Procesing", "Finsished")
                        }
                    }

                }) {
                    Text(text = "BFS Next")
                }
                Button(onClick = { simulationState.value.onTraversalChanged() }) {

                    Text(text = "ChangeTraversal")
                }
                Button(onClick = {

                    when (val res = simulationState.value.dfsSequence?.next()) {
                        is Simulating -> {
                            Log.i("Simulating:Procesing", "${res.processingNodeIndex}")
                            simulationState.value.processing(res.processingNodeIndex)
                        }

                        is Started -> {
                            Log.i("Simulating:Procesing", "Started")
                        }

                        is Finished -> {
                            Log.i("Simulating:Procesing", "Finsished")
                        }
                    }

                }) {
                    Text(text = "DFS Next")
                }
                GraphDrawer(
                    nodes = simulationState.value.nodes.collectAsState().value,
                    edges = simulationState.value.edges.collectAsState().value,
                )
            }
        }


    }


}