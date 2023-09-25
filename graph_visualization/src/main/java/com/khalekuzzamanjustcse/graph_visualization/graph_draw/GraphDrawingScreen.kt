package com.khalekuzzamanjustcse.graph_visualization.graph_draw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton

@Preview
@Composable
fun GraphTraversalScreen() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density
    val viewModel = remember {
        GraphDrawerViewModel(size = size, sizePx = sizePx)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            MyButton(label = "AddEdge", onClick = viewModel::addEdge)
            MyButton(label = "AddNode", onClick ={
                viewModel.addNode("33")
            })
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

