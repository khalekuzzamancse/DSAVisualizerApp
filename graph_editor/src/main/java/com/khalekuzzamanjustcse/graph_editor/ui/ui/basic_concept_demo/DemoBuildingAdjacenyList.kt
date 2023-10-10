package com.khalekuzzamanjustcse.graph_editor.ui.ui.basic_concept_demo

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DemoAdjacentList() {
    val minTouchTargetPx = 30.dp.value * LocalDensity.current.density
    val density = LocalDensity.current.density
    var node by remember {
        mutableStateOf(
            DemoNode(
                center = Offset(50f, 50f),
                density = density,
                radius = 25.dp
            )
        )
    }

    var demoEdge by remember {
        mutableStateOf(
            DemoEdge(
                start = Offset(100f, 100f),
                end = Offset(250f, 100f),
                minTouchTargetPx = minTouchTargetPx,
                cost = "5 Tk"
            )
        )
    }
    val textMeasurer = rememberTextMeasurer()
    var selectedPoint by remember {
        mutableStateOf<EdgePoint?>(null)
    }
    var nodeSelected by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            Button(onClick = {
                if(node.isInsideCircle(demoEdge.start))
                {
                    Log.i("EdgePointStatus: ","EdgeStart->node")
                }
                else{
                    Log.i("EdgePointStatus: ","EdgeStart->--")
                }
                if(node.isInsideCircle(demoEdge.end))
                {
                    Log.i("EdgePointStatus: ","EdgeEnd->node")
                }
                else{
                    Log.i("EdgePointStatus: ","EdgeEnd->--")
                }

            }) {
                Text(text = "Check Adjacency")
            }
        }
        Canvas(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { touchedPosition ->
                            demoEdge = demoEdge.goEditMode(touchedPosition)
                            nodeSelected = node.isInsideCircle(touchedPosition)
                            if (nodeSelected)
                                node = node.enableEdit(true)

                        })
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            demoEdge = demoEdge.updatePoint(dragAmount)
                            if (nodeSelected) {
                                node = node.updateCenter(dragAmount)
                            }
                        },
                        onDragEnd = {
                            selectedPoint = null
                            nodeSelected = false
                            node = node.enableEdit(false)
                        }
                    )
                }
        ) {
            drawDemoEdge(
                demoEdge,
                textMeasurer
            )
            //draw sample nodes
            drawCircle(
                color = node.color,
                radius = node.radiusPx,
                center = node.center
            )
        }
    }



}