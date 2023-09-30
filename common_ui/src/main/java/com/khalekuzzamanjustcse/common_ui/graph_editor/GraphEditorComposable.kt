package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun GraphEditorComposable() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density

    val editor = remember {
        GraphEditor(size, sizePx)
    }
    Column {
        FlowRow {
            MyButton(label = "Add") {
                editor.addNode("33")
            }
            MyButton(label = "AddEdge") {
                editor.addEdge()
            }
            MyButton(label = "RemoveNode") {
                editor.removeNode()
            }
            MyButton(label = "Redo") {

            }
        }

        GraphBuilder(
            nodes = editor.nodes.collectAsState().value.toList(),
            edges =editor.edges.collectAsState().value.toList(),
        )
    }


}
