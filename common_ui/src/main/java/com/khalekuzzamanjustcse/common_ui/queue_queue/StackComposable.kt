package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.CellPointerComposable
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.VisualElementComposable

@Composable
fun StackVisualizationScreen(
    state: StackState,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .padding(scaffoldPadding)
    ) {
        StackComposable(state)

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StackComposable(
    stackState: StackState
) {
    Box {
        FlowRow(
            modifier = Modifier.border(width = 2.dp, color = Color.Black)
        ) {
            stackState.stackElement.forEach {
                VisualElementComposable(it)
            }
        }
        CellPointerComposable(
            cellSize = stackState.cellSize,
            label = "top",
            currentPosition = stackState.stackTopPointerPosition
        )
    }

}

