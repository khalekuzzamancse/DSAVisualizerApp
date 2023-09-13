package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Range
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
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.MovePointer
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.PointerName
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.Pointers
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.SelectionSortPointerName
import com.khalekuzzamanjustcse.dsavisulizer.visual_array.VisualArray

fun calculateChangeColor(state: SelectionSortState): List<Pair<Range<Int>, Color>> {
    val commands = mutableListOf<Pair<Range<Int>, Color>>()
    val minIndex = state.minIndex
    val sortedIndex = state.sortedTill
    val i = state.i
    val j = state.j
    val lastIndex = state.currentList.size - 1
    //removePreviousAllColor

    // marking the new minimumIndex
    if (minIndex != -1) {
        //fixes the bugs
        //  commands.add(Pair(Range(0,lastIndex ), Color.Unspecified))
        commands.add(Pair(Range(minIndex, minIndex), Color.Blue))
    }

    if (sortedIndex != -1) {
        commands.add(Pair(Range(0, sortedIndex), Color.Yellow))
    }
    return commands
}


@Preview
@Composable
fun CodeExecutionPreview() {
    SelectionSortCodeExecution()
}

@Composable
fun SelectionSortCodeExecution(

) {

    var arrayCellValue by remember {
        mutableStateOf(ArrayCellValue(emptyList()))
    }
    var currentState by remember {
        mutableStateOf(SelectionSortState(executedLineNo = -1))
    }
    var sequence by remember {
        mutableStateOf(getSelectionSortExecutionSequence(arrayCellValue))
    }

    var sequenceAt by remember {
        mutableStateOf(0)
    }
    var executionFinished by remember {
        mutableStateOf(false)
    }


    var variables by remember {
        mutableStateOf(
            listOf(
                Variable(name = "i"), Variable(name = "j"), Variable(name = "minIndex"),
            )
        )
    }


    var changeColor by remember {
        mutableStateOf(Pair(Range(-1, -1), Color.Unspecified))
    }
    val pointers by remember {
        mutableStateOf(
            Pointers(
                listOf(
                    com.khalekuzzamanjustcse.dsavisulizer.visual_array.CellPointer(
                        position = mutableStateOf(Offset(0f, 0f)),
                        label = "i",
                        icon = Icons.Default.ArrowForward,
                        name = SelectionSortPointerName.I
                    ),
                    com.khalekuzzamanjustcse.dsavisulizer.visual_array.CellPointer(
                        position = mutableStateOf(Offset(0f, 0f)),
                        label = "j",
                        icon = Icons.Default.PlayArrow,
                        name = SelectionSortPointerName.J
                    ),
                    com.khalekuzzamanjustcse.dsavisulizer.visual_array.CellPointer(
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


    val onNextButtonClick: () -> Unit = {
        val states = sequence[sequenceAt]
        currentState = states

        calculateChangeColor(currentState).forEach { command ->
            changeColor = command
        }


        updateVariable()
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
            arrayCellValue = ArrayCellValue(it)
            sequence = getSelectionSortExecutionSequence(arrayCellValue)
        }

        VariablesStates(variables = variables)
        Spacer(modifier = Modifier.height(16.dp))
        if (arrayCellValue.list.isNotEmpty()) {
//            ArrayStateVisualizer(
//                array = arrayCellValue,
//                markCellAsBlue = currentState.minIndex,
//                markCellAsSort = currentState.sortedTill,
//                movePointerAt = currentState.i,
//                movePointerJ = currentState.j,
//                allCellSorted = executionFinished,
//                swapElements = if (currentState.shouldSwap) Pair(
//                    currentState.i,
//                    currentState.minIndex
//                ) else Pair(-1, -1),
//            )


            VisualArray(
                list = inputArray,
                swap = if (currentState.shouldSwap) Pair(
                    currentState.i,
                    currentState.minIndex
                ) else Pair(-1, -1),
//                changeColor = Pair(
//                    Range(currentState.minIndex, currentState.minIndex),
//                    Color.Yellow
//                )
                cellPointers = pointers,
                movePointer = listOf(
                    MovePointer(SelectionSortPointerName.I, currentState.i ),
                    MovePointer(SelectionSortPointerName.J, currentState.j ),
                    MovePointer(SelectionSortPointerName.MinIndex, currentState.minIndex ),
                )
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

