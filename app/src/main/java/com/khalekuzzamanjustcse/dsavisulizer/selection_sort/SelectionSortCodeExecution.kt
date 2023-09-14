package com.khalekuzzamanjustcse.dsavisulizer.selection_sort

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.dsavisulizer.R
import com.khalekuzzamanjustcse.dsavisulizer.Variable
import com.khalekuzzamanjustcse.dsavisulizer.VariablesStates
import com.khalekuzzamanjustcse.dsavisulizer.peudocode.PseudocodeExecutor
import com.khalekuzzamanjustcse.dsavisulizer.peudocode.selectionSortPseudocode
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.ArrayInput
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.CellColor
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.CellPointer
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.MovePointer
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.Pointers
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.SelectionSortPointerName
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.VisualArray


@Preview
@Composable
fun CodeExecutionPreview() {
    SelectionSortCodeExecution()
}

@Composable
fun SelectionSortCodeExecution(

) {


    var currentState by remember {
        mutableStateOf(SelectionSortState(executedLineNo = -1))
    }
    var sequence by remember {
        mutableStateOf(getSelectionSortExecutionSequence(emptyList()))
    }

    var sequenceAt by remember {
        mutableStateOf(0)
    }
    var executionFinished by remember {
        mutableStateOf(false)
    }
    var cellColors by remember {
        mutableStateOf(listOf(CellColor(-1, Color.Unspecified)))
    }


    var variables by remember {
        mutableStateOf(
            listOf(
                Variable(name = "i"), Variable(name = "j"), Variable(name = "minIndex"),
            )
        )
    }


    val pointers by remember {
        mutableStateOf(
            Pointers(
                listOf(
                  CellPointer(
                        position = mutableStateOf(Offset(0f, 0f)),
                        label = "i",
                        icon = Icons.Default.ArrowForward,
                        name = SelectionSortPointerName.I
                    ),
                 CellPointer(
                        position = mutableStateOf(Offset(0f, 0f)),
                        label = "j",
                        icon = Icons.Default.PlayArrow,
                        name = SelectionSortPointerName.J
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset(0f, 0f)),
                        label = "min",
                        icon = Icons.Default.PlayArrow,
                        name = SelectionSortPointerName.MinIndex
                    ),
                )
            )
        )
    }

    val updateVariable: () -> Unit = {
        val updatedVariables = variables.toMutableList()
        updatedVariables[0] =
            updatedVariables[0].copy(value = if (currentState.i == -1) null else "${currentState.i}")
        updatedVariables[1] =
            updatedVariables[1].copy(value = if (currentState.j == -1) null else "${currentState.j}")
        updatedVariables[2] =
            updatedVariables[2].copy(value = if (currentState.minIndex == -1) null else "${currentState.minIndex}")
        variables = updatedVariables
    }

    val updatedCellColor: (SelectionSortState) -> Unit = {
        val minIndex = it.minIndex
        val sortedColor = Color.Yellow
        val minIndexColor = Color.Green

        val list = mutableListOf<CellColor>()
        if (minIndex != -1) {
            for (index in cellColors.indices) {
                if (cellColors[index].color == minIndexColor)
                    list.add(cellColors[index].copy(color = Color.Unspecified))
            }
            //change min index color
            list.add(CellColor(minIndex, minIndexColor))
        }
        if(it.shouldSwap){
            for (index in cellColors.indices) {
                if (cellColors[index].color == minIndexColor)
                    list.add(cellColors[index].copy(color = Color.Unspecified))
            }
        }
        if (it.sortedTill!=-1) {
            list.add(CellColor(it.sortedTill, sortedColor))
        }
        //when executing finish
        if(it.i==it.currentList.size-1){
            list.add(CellColor(it.i,sortedColor))
        }
        cellColors=list

    }


    val onNextButtonClick: () -> Unit = {
        val states = sequence[sequenceAt]
        currentState = states
        updateVariable()
        updatedCellColor(states)

        if (sequenceAt < sequence.size - 1)
            sequenceAt++
        else
            executionFinished = true
    }


    var inputArray by remember {
        mutableStateOf(emptyList<Int>())
    }


    //
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ArrayInput {
            inputArray = it
            sequence = getSelectionSortExecutionSequence(inputArray)
        }

        VariablesStates(variables = variables)
        Spacer(modifier = Modifier.height(16.dp))
        if (inputArray.isNotEmpty()) {
            VisualArray(
                list = inputArray,
                swap = if (currentState.shouldSwap) Pair(
                    currentState.i,
                    currentState.minIndex
                ) else Pair(-1, -1),
                cellPointers = pointers,
                movePointer = listOf(
                    MovePointer(SelectionSortPointerName.I, currentState.i),
                    MovePointer(SelectionSortPointerName.J, currentState.j),
                    MovePointer(SelectionSortPointerName.MinIndex, currentState.minIndex),
                ),
                changeCellColor = cellColors
            )


            IconButton(onClick = onNextButtonClick) {
                Icon(
                    painter = painterResource(id = R.drawable.run_next),
                    contentDescription = null
                )
            }
            PseudocodeExecutor(
                codes = selectionSortPseudocode,
                executingLine = if (executionFinished) -1 else currentState.executedLineNo,
            )
        }


    }

}

