package com.khalekuzzamanjustcse.common_ui.queue_queue

import android.util.Log
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

@Composable
fun StackComposablePreview(scaffoldPadding: PaddingValues = PaddingValues(0.dp)) {
    val size = 64.dp
    val density = LocalDensity.current
    var cnt by remember {
        mutableIntStateOf(1)
    }
    val stackState = remember {
        StackState(cellSize = size, density = density)

    }
    Column(
        modifier = Modifier
            .padding(scaffoldPadding)
    ) {
        FlowRow {
            MyButton(label = "Push") {
                stackState.push("${cnt * 2}")
                cnt++
            }
            MyButton(label = "Pop") {
                stackState.pop()
            }
        }

        StackComposable(stackState)

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

@Composable
fun MyButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text(text = label)
    }

}