package com.khalekuzzamanjustcse.graph_editor.edge

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.components.MyButton
import com.khalekuzzamanjustcse.graph_editor.components.NodeDataInput

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun GraphEditor() {
    val minTouchTargetPx = 30.dp.value * LocalDensity.current.density
    val edgeManger = remember {
        GraphEditorVisualEdgeMangerImp(minTouchTargetPx)
    }
    val textMeasurer = rememberTextMeasurer()
    val currentDrawingEdge = edgeManger.currentAddingEdge.collectAsState().value
    val edges = edgeManger.edges.collectAsState().value

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxSize()
    ) {
        NodeDataInput(
            isOpen = false,
            message = "Enter Node Value"
        ) {

        }
        NodeDataInput(
            isOpen = edgeManger.showEdgeInputPopUp.collectAsState().value,
            message = "Enter Edge Cost"
        ) {
            edgeManger.addEdge(it)
        }
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            MyButton(label = "AddNode") {
            }
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "RemoveNode",
                onClick = { }
            )

            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "AddEdge",
                onClick = edgeManger::onEdgeInputRequest
            )
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "RemoveEdge",
                onClick = {}
            )
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "Undirected",
                onClick = { edgeManger.onGraphTypeChanged() },
                enabled = edgeManger.isDirected.collectAsState().value
            )
        }

        Canvas(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { touchedPosition ->
                            edgeManger.onTap(touchedPosition)
                        })
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = edgeManger::onDragStart,
                        onDrag = { change, dragAmount ->
                            edgeManger.dragOngoing(dragAmount, change.position)
                        },
                        onDragEnd = edgeManger::dragEnded,
                    )
                }
        ) {
            edges.forEach {
                drawEdge(it, textMeasurer)
            }
            currentDrawingEdge?.let {
                drawEdge(it, textMeasurer)
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