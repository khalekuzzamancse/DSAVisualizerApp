package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import android.util.Range
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.dsavisulizer.SwappableElement

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun VisualArrayPreview() {
    val list = listOf(1, 2, 3, 4)
    var swap by remember {
        mutableStateOf(Pair(-1, -1))
    }
    var i by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            Button(onClick = {
                swap = Pair(i, i + 1)
                i++
                if (i == list.size - 1)
                    i = 0
            }) {
                Text("Swap")
            }
            Button(onClick = {

            }) {
                Text("changeColor")
            }
        }
        VisualArray(
            list = list,
            swap = swap,
            changeColor = Pair(Range(i,i+1), Color.Blue)
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualArray(
    list: List<Int>,
    swap: Pair<Int, Int> = Pair(-1, -1),
    changeColor: Pair<Range<Int>, Color> = Pair(Range(-1, -1), Color.Unspecified)
) {

    val states by remember {
        mutableStateOf(CellsAndElements.createInstance(list))
    }

    LaunchedEffect(states) {
        for (index in 0 until states.cells.size) {
            states.elements[index].position.value = states.cells[index].position
            states.elements[index].currentCell = index
        }
    }
    LaunchedEffect(swap) {
        states.swapCellElements(swap.first, swap.second)
    }
    LaunchedEffect(changeColor) {
        states.changeCellColor(changeColor.first.lower, changeColor.first.upper, changeColor.second)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            states.cells.forEachIndexed { _, cell ->
                ArrayCell(
                    cellSize = cell.size,
                    backgroundColor = cell.backgroundColor.value
                ) { pos ->
                    cell.position = pos
                }
            }
        }
        states.elements.forEachIndexed { index, element ->
            SwappableElement(
                currentOffset = element.position.value,
                label = "${element.value}",
                size = states.cells[index].size
            )
        }
    }

}

@Composable
private fun ArrayCell(
    modifier: Modifier = Modifier,
    cellSize: Dp,
    backgroundColor: Color,
    onPositionChanged: (Offset) -> Unit,
) {
    Box(
        modifier = modifier
            .size(cellSize)
            .border(width = 1.dp, color = Color.Black)
            .background(backgroundColor)
            .onGloballyPositioned { position ->
                onPositionChanged(position.positionInParent())
            }
    )

}