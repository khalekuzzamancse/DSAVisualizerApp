package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.ControlButton
import com.khalekuzzamanjustcse.common_ui.ControlsComposable

@Preview
@Composable
fun GraphEditorComposable() {
    val minSize=50.dp
    val minSizePx=minSize.value* LocalDensity.current.density
    val editor = remember {
       GraphEditorCanvasUIState(minSize, minSizePx)
    }
    val enabled = remember {
        mutableStateOf(true)
    }
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val controls = listOf(
        object : ControlButton {
            override val icon = Icons.Filled.Grain
            override val label = "Add Node"
            override val enabled = enabled
            override fun onClick() {
                editor.onNodeInputRequest()
            }
        },

        object : ControlButton {
            override val icon = Icons.Filled.LinearScale
            override val label = "Add Edge"
            override val enabled = enabled
            override fun onClick() = editor.addEdge()
        },
        object : ControlButton {
            override val icon = Icons.Filled.Grain
            override val label = "AllNodeSameSize"
            override val enabled = enabled
            override fun onClick() {
                editor.makeAllNodeSameSize()
            }
        },
        object : ControlButton {
            override val icon = Icons.Filled.Undo
            override val label = "Undo"
            override val enabled = enabled
            override fun onClick() {
             editor.undo()
            }
        },
        object : ControlButton {
            override val icon = Icons.Filled.Redo
            override val label = "Redo"
            override val enabled = enabled
            override fun onClick() {
                editor.redo()
            }
        },
        object : ControlButton {
            override val icon = Icons.Filled.DoneOutline
            override val label = "Done"
            override val enabled = enabled
            override fun onClick() {
                editor.onInputComplete()
            }
        },
    )
    Column {
        ControlsComposable(title = "Graph Editor", controls = controls)
        NodeDataInput(
            editor.takeInput.collectAsState().value,
            onInputComplete = { text ->
                val res = measurer.measure(text = text, density = density)
                val mx = maxOf(res.size.height, res.size.width)
                val si = mx.dp
                val siPx = si.value * density.density
                editor.onInputComplete(text, si, siPx)

            }
        )
        GraphBuilder(
            nodes = editor.visualNodes.collectAsState().value.toList(),
            edges = editor.visualEdges.collectAsState().value.toList(),
            onCanvasTapped = editor::onCanvasTap
        )
    }


}
