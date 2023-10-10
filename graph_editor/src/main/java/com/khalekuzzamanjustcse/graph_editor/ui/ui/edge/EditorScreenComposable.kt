package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.ui.components.NodeDataInput
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edtior.GraphEditorManger
import com.khalekuzzamanjustcse.graph_editor.ui.ui.node.drawNode

@OptIn(ExperimentalMaterial3Api::class)
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = "Graph Editor",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(
                        onClick = { openAddNodePopup = true },



                    ) {
                        Icon(imageVector = Icons.Filled.AddCircleOutline, null)
                    }

                    IconButton(
                        onClick = {
                            viewModel.onRemoveNodeRequest()
                        },
                        enabled = viewModel.selectedNode.collectAsState().value != null,
                    ) {
                        Icon(imageVector = Icons.Filled.RemoveCircleOutline, null)

                    }

                    IconButton(
                        onClick = {
                            openAddEdgePopup = true
                        },



                    ) {
                        Icon(imageVector = Icons.Filled.Moving, null)
                    }
                    IconButton(
                        onClick = {
                            viewModel.onRemoveEdgeRequest()
                        },
                        enabled = viewModel.selectedEdge.collectAsState().value != null,

                    ) {
                        Icon(imageVector = Icons.Filled.CallSplit, null)
                    }
                    IconButton(
                        onClick = {
                            viewModel.onRemoveEdgeRequest()
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.Save, null)
                    }
                    IconButton(
                        onClick = {
                            viewModel.onRemoveEdgeRequest()
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.Print, null)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )

            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
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
                            onDrag = { _, dragAmount ->

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

}




