package com.khalekuzzamanjustcse.common_ui.queue_stack

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.CellPointerComposable
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.VisualElementComposable


@Preview
@Composable
fun QueueVisualizationScreenPreview() {
//    val size=50.dp
//    val density= LocalDensity.current
//    val state= remember {
//        QueueState(size,density)
//    }
//    Column {
//        FlowRow {
//            MyButton(label = "Enqueue") {
//                state.enqueue("10")
//            }
//            MyButton(label = "Dequeue") {
//                state.dequeue()
//            }
//        }
//        QueueVisualizationScreen(state)
//    }

}

@Composable
fun QueueVisualizationScreen(
    state: QueueState,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp),
) {

    Column(
        modifier = Modifier
            .padding(scaffoldPadding)
            .fillMaxSize()

    ) {
        QueueComposable(state)


    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QueueComposable(
    state: QueueState
) {
    Box {
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

