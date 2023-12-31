package com.khalekuzzamanjustcse.graph_visualization.swap_able_array

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable


@Preview
@Composable
fun ArrayCellPreview() {
    val cellSize = 64.dp
    val cellSizePx = cellSize.value * LocalDensity.current.density
    val list = listOf(1, 2, 3, 4, 5)
    val state = remember {
        ArrayComposableState(list = list, cellSizePx = cellSizePx)
    }

    Column {
        Button(onClick = {
               Log.i("CurrentArrayState", "${state.cellsCurrentElements}")

        }) {
            Text(text = "State")
        }
        ArrayComposable(
            modifier=Modifier.fillMaxSize(),
            cellSize = cellSize,
            onCellPositionChanged = state::onCellPositionChanged,
            state = state,
            onDragEnd = state::onDragEnd,
            onDragStart = state::onDragStart
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ArrayComposable(
    modifier: Modifier=Modifier,
    invisibleCell: Boolean = false,
    state: ArrayComposableState<T>,
    cellSize: Dp,
    onCellPositionChanged: (Int, Offset) -> Unit = { _, _ -> },
    onDragStart: (Int)->Unit,
    onDragEnd: (Int) -> Unit = {},
) {
    Box(modifier = modifier) {
        FlowRow {
//            state.list.forEachIndexed { index, _ ->
            state.cells.forEachIndexed { index, _ ->
                ArrayCell(cellSize = cellSize,
                    hideBorder = invisibleCell,
                    onPositionChanged = { position ->
                        onCellPositionChanged(index, position.positionInParent())
                    })
            }
        }
        state.elements.forEachIndexed { index, value ->
            GraphNodeComposable(
                label = "${value.value}",
                size = cellSize,
                currentOffset = value.position.value,
                onDrag = { dragAmount ->
                    state.onDragElement(index, dragAmount)
                }, onDragEnd = {
                    onDragEnd(index)
                }
                , onDragStart = {
                    onDragStart(index)
                }
            )
        }
    }

}


@Composable
fun ArrayCell(
    modifier: Modifier = Modifier,
    cellSize: Dp,
    backgroundColor: Color = Color.Unspecified,
    hideBorder: Boolean = false,
    onPositionChanged: (LayoutCoordinates) -> Unit,
) {
    Box(
        modifier = modifier
            .size(cellSize)
            .border(width = if (hideBorder) 0.dp else 1.dp, color = Color.Black)
            .background(backgroundColor)
            .onGloballyPositioned { position ->
                onPositionChanged(position)
            }
    )

}