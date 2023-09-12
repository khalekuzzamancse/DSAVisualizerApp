package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
   val onNextButtonClick:()->Unit={
        val states = sequence[sequenceAt]
        currentState = states
        updateVariable()
        if (sequenceAt < sequence.size - 1)
            sequenceAt++
        else
            executionFinished = true
    }


    //
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ArrayInput {
            arrayCellValue = ArrayCellValue(it)
            sequence = getSelectionSortExecutionSequence(arrayCellValue)
        }

        VariablesStates(variables = variables)
        Spacer(modifier = Modifier.height(16.dp))
        if (arrayCellValue.list.isNotEmpty()) {
            ArrayStateVisualizer(
                array = arrayCellValue,
                markCellAsBlue = currentState.minIndex,
                markCellAsSort = currentState.sortedTill,
                movePointerAt = currentState.i,
                movePointerJ=currentState.j,
                allCellSorted = executionFinished,
                swapElements = if (currentState.shouldSwap) Pair(
                    currentState.i,
                    currentState.minIndex
                ) else Pair(-1, -1),
            )
            IconButton(onClick =onNextButtonClick) {
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

