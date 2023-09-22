package com.khalekuzzamanjustcse.graph_visualization.graph_input

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun GraphWithEdges() {
    val nodeSize = 64.dp
    val nodeSizePx = nodeSize.value * LocalDensity.current.density


    val offsets = remember {
        mutableListOf(
            mutableStateOf(Offset.Zero), mutableStateOf(Offset.Zero), mutableStateOf(Offset.Zero)
        )
    }
    val positions = remember {
        mutableListOf(
            mutableStateOf(Offset.Zero), mutableStateOf(Offset.Zero), mutableStateOf(Offset.Zero)
        )
    }
    val adjecents = remember {
        mutableListOf(
            mutableIntStateOf(-1), mutableIntStateOf(-1), mutableIntStateOf(-1)
        )
    }

    var u by remember {
        mutableIntStateOf(-1)
    }
    var v by remember {
        mutableIntStateOf(-1)
    }
    var drawLine by remember { mutableStateOf(false) }


    val nodes = listOf(1, 2, 3)

    Box(
        modifier = Modifier
            .drawBehind {
                adjecents.forEachIndexed { u, v ->
                    if (v.intValue != -1) {
                        drawLine(
                            color = Color.Black,
                            start = positions[u].value + Offset(nodeSizePx / 2, nodeSizePx / 2),
                            end = positions[v.intValue].value + Offset(
                                nodeSizePx / 2,
                                nodeSizePx / 2
                            ),
                            strokeWidth = 4f
                        )

                    }
                }
            }
            .fillMaxSize()

    ) {
        Button(onClick = {
            if (u != -1 && v != -1 && u != v)
                adjecents[u].intValue = v
            u=-1
            v=-1
        }) {
            Text(text = "DrawLine")
        }

        nodes.forEachIndexed { index, value ->
            GraphNodeComposable(
                label = "$value",
                size = nodeSize,
                currentOffset = offsets[index].value,
                onDrag = {
                    offsets[index].value += it
                    drawLine = false
                },
                onPositionChanged = {
                    positions[index].value = it.positionInRoot()
                    Log.i("PositionChanged:$index :", "${it.positionInRoot()}")
                },
                onNodeClick = {
                    if (u == -1)
                        u = index
                    else if (v == -1 && u != index)
                        v = index
                }
            )

        }

    }

}

