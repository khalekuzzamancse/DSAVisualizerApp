package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
private fun GraphEditorComposable() {
    val size = 50.dp
    val sizePx = size.value * LocalDensity.current.density

    val editor = remember {
        GraphEditor(size, sizePx)
    }
    Column {
        Row {
            MyButton(label = "Add") {
                editor.addNode("33")
            }
            MyButton(label = "AddEdge") {
                editor.addEdge()
            }
        }

        GraphBuilder(
            nodes = editor.nodes.collectAsState().value.toList(),
            edges =editor.edges.collectAsState().value.toList(),
        )
    }


}
