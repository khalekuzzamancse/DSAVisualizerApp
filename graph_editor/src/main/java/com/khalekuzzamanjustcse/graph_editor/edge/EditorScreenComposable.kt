package com.khalekuzzamanjustcse.graph_editor.edge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.components.MyButton
import com.khalekuzzamanjustcse.graph_editor.components.NodeDataInput
import com.khalekuzzamanjustcse.graph_editor.edtior.GraphEditorManger
import com.khalekuzzamanjustcse.graph_editor.node.NodeManager
import com.khalekuzzamanjustcse.graph_editor.node.drawNode

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun GraphEditor() {
    val density = LocalDensity.current.density


    val textMeasurer = rememberTextMeasurer() //
    val viewModel = remember {
        GraphEditorManger(density)
    }


    var openAddNodePopup by remember { mutableStateOf(false) }
    var openAddEdgePopup by remember { mutableStateOf(false) }
    //
    val nodes = viewModel.nodes.collectAsState().value
    val edges = viewModel.edges.collectAsState().value
    val currentDrawingEdge = viewModel.currentAddingEdge.collectAsState().value


    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxSize()
    ) {
        NodeDataInput(
            isOpen = openAddNodePopup,
            message = "Enter Node Value"
        ) {
            viewModel.onAddNodeRequest(cost = it)
            openAddNodePopup = false
        }
        NodeDataInput(
            isOpen = openAddEdgePopup,
            message = "Enter Edge Cost"
        ) {
            viewModel.onEdgeConstInput(it)
            openAddEdgePopup = false
        }
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            MyButton(label = "AddNode") {
                openAddNodePopup = true
            }
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "AddEdge",
                onClick = {
                    openAddEdgePopup = true
                }
            )
            MyButton(
                label = "RemoveNode",
                enabled = viewModel.selectedNode.collectAsState().value!=null,
            ) {
                viewModel.onRemoveNodeRequest()
            }
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "RemoveEdge",
                enabled = viewModel.selectedEdge.collectAsState().value!=null

            ) {
                viewModel.onRemoveEdgeRequest()
            }
        }

        Canvas(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { touchedPosition ->
                            viewModel.onTap(touchedPosition)
                        })
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {

                            viewModel.onDragStart(it)
                        },
                        onDrag = { change, dragAmount ->

                            viewModel.onDrag(dragAmount)
                        },
                        onDragEnd = {

                            viewModel.dragEnd()
                        }
                    )
                }
        ) {
            edges.forEach {
                drawEdge(it, textMeasurer)
            }
            currentDrawingEdge?.let {
                drawEdge(it, textMeasurer)
            }
            nodes.forEach {
                drawNode(it, textMeasurer)
            }


        }

    }
}

@Composable
fun EditorSection(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ControlSection(
    modifier: Modifier = Modifier
) {

}