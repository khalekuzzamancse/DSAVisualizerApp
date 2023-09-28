package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import kotlin.random.Random


private val random
    get() = Random.nextInt(99)

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun GraphTraversalScreen() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density
    val viewModel = remember {
        ViewModel(size = size, sizePx = sizePx)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp
        ) {
            Column(
                horizontalAlignment =Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Graph Input",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(50.dp))
                FlowRow {
                    MyButton(
                        label = "AddEdge", onClick = viewModel::addEdge
                    )
                    MyButton(label = "AddNode", onClick = {
                        viewModel.addNode("$random")
                    })
                    MyButton(label = "RemoveNode", onClick = viewModel::removeNode)
                    MyButton(
                        enabled = viewModel.disableUndo,
                        label = "Undo",
                        onClick = viewModel::undo
                    )
                    MyButton(
                        enabled = viewModel.disableRedo,
                        label = "Redo",
                        onClick = viewModel::redo
                    )
                }
            }

        }
        GraphDrawer(
            nodes = viewModel.nodes.collectAsState().value,
            edges = viewModel.edges.collectAsState().value,
            onDrag = viewModel::onDrag,
            onClick = viewModel::onNodeClick,
            onCanvasTapped = viewModel::onCanvasTapped
        )
    }

}
