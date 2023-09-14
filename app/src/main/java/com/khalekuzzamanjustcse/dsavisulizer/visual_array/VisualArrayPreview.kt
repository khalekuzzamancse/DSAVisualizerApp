package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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



    val pointers by remember {
        mutableStateOf(
            Pointers(
                listOf(
                    CellPointer(
                        position = mutableStateOf(Offset(0f, 200f)),
                        label = "i",
                        icon = Icons.Default.ArrowForward,
                        name = SelectionSortPointerName.I
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset(200f, 200f)),
                        label = "j",
                        icon = Icons.Default.PlayArrow,
                        name = SelectionSortPointerName.J
                    ),
                )
            )
        )
    }
    var movePointer by remember {
        mutableStateOf(MovePointer(PointerName.NULL, -1))
    }
    var cellColors by remember {
        mutableStateOf(listOf(CellColor(-1, Color.Unspecified)))
    }

    Column(
        modifier =
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
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
                movePointer = MovePointer(SelectionSortPointerName.I, 3)
            }) {
                Text("pointer i")
            }
            Button(onClick = {
                movePointer = MovePointer(SelectionSortPointerName.J, 2)
            }) {
                Text("pointer j")
            }
            Button(onClick = {
                cellColors= listOf(
                    CellColor(0, Color.Yellow),
                    CellColor(1, Color.Yellow),
                    CellColor(2, Color.Green),
                )
            }) {
                Text("changeCellColor")
            }

        }
        VisualArray(
            list = list,
            swap = swap,
            cellPointers = pointers,
            movePointer = listOf(movePointer),
            changeCellColor = cellColors
        )
    }

}
