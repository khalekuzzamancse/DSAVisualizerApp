package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.CellPointerComposable
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.VisualElementComposable

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun QueueComposablePreview() {
    val size = 64.dp
    val density = LocalDensity.current
    var cnt by remember {
        mutableIntStateOf(1)
    }
    val queueState = remember {
        QueueState(cellSize = size, density = density)

    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()

    ) {
        FlowRow {
            MyButton(label = "Enqueue") {
                queueState.enqueue("${cnt * 2}")
                cnt++
            }
            MyButton(label = "Dequeue") {
                queueState.dequeue()
            }
        }

        QueueComposable(queueState)


    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QueueComposable(
    state: QueueState
) {
    Box{
        FlowRow(
            modifier = Modifier.border(width = 2.dp, color = Color.Black)
        ) {
            state.element.forEach {
                VisualElementComposable(it)
            }
        }

        CellPointerComposable(
            cellSize = state.cellSize,
            label = "rear",
            currentPosition = state.rearPointerPosition
        )
        CellPointerComposable(
            cellSize = state.cellSize,
            label = "front",
            currentPosition = state.frontPointerPosition
        )
    }



}
