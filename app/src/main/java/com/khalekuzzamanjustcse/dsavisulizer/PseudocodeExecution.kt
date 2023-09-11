package com.khalekuzzamanjustcse.dsavisulizer

import PseudocodeLine
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun CodeExecutionPreview() {
    CodeExecution()
}

@Composable
fun CodeExecution() {
    var minimumIndex by remember {
        mutableStateOf(-1)
    }
    var sortedIndex by remember {
        mutableStateOf(-1)
    }
    var swapIndex by remember {
        mutableStateOf(Pair(-1, -1))
    }
    var executionFinished by remember {
        mutableStateOf(false)
    }
  var arrayCellValue by remember {
      mutableStateOf( ArrayCellValue(emptyList()))
  }




    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ArrayInputComposable(){
            Log.i("LISTCHANGED","$it")
            arrayCellValue= ArrayCellValue(it)
        }
        if(arrayCellValue.list.isNotEmpty()){
            ListComposable(
                data = arrayCellValue,
                minimumIndex = minimumIndex,
                sortedIndex = sortedIndex,
                swapIndex = swapIndex,
                executionFinished = executionFinished
            )
            HighLightLine(
                arrayCellValue = arrayCellValue,
                onSortedPortionUpdated = {
                    sortedIndex = it
                },
                onSwapped = { i, j ->
                    swapIndex = Pair(i, j)
                    minimumIndex = -1
                },
                onMinimumIndexUpdated = {
                    minimumIndex = it
                },
                onListUpdated = {
                    // list=it
                },
                onExecutionFinished = {
                    executionFinished=true
                }

            )
        }



    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HighLightLine(
    arrayCellValue: ArrayCellValue,
    onMinimumIndexUpdated: (Int) -> Unit,
    onSortedPortionUpdated: (Int) -> Unit,
    onListUpdated: (List<Int>) -> Unit,
    onExecutionFinished: ()->Unit,
    onSwapped: (Int, Int) -> Unit,
) {
    val sequence by remember {
        mutableStateOf(getSelectionSortExecutionSequence(arrayCellValue))
    }

    var currentState by remember {
        mutableStateOf(SelectionSortState(executedLineNo = 1))
    }



    val lines by remember {
        mutableStateOf(
            listOf(
                PseudocodeLine(1, "selectionSort(arr):"),
                PseudocodeLine(2, "n = length of arr"),
                PseudocodeLine(3, "for i from 0 to n - 1"),
                PseudocodeLine(lineNumber = 4, text = "minIndex = i", marginFactor = 2),
                PseudocodeLine(5, "for j from i + 1 to n - 1"),
                PseudocodeLine(
                    lineNumber = 6,
                    text = "if arr[j] < arr[minIndex]",
                    marginFactor = 2
                ),
                PseudocodeLine(lineNumber = 7, text = "minIndex = j", marginFactor = 3),
                PseudocodeLine(8, "swap(arr[i], arr[minIndex])"),
            )
        )
    }

    var sequenceAt by remember {
        mutableStateOf(0)
    }


    onListUpdated(currentState.currentList)
    if (currentState.shouldSwap) {
        onSortedPortionUpdated(currentState.i)
        onSwapped(currentState.i, currentState.minIndex)
    }
    if (currentState.executedLineNo == 4 || currentState.executedLineNo == 7) {
        onMinimumIndexUpdated(currentState.minIndex)
    }




    ElevatedCard(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, Color.Black)
            .padding(16.dp)

    ) {

        Button(onClick = {
            val states = sequence[sequenceAt]
            currentState = states
            val currentLine = sequence[sequenceAt].executedLineNo - 1
            val previousLine =
                if (sequenceAt == 0) sequence[sequenceAt].executedLineNo - 1 else sequence[sequenceAt - 1].executedLineNo - 1
            lines[previousLine].textColor.value = Color.Unspecified
            lines[currentLine].textColor.value = Color.Red
            if (sequenceAt < sequence.size - 1)
                sequenceAt++
            else{
                onExecutionFinished()
                val line = sequence.last().executedLineNo-1
                lines[line].textColor.value=Color.Unspecified
            }


        }) {
            Text(text = "Next")
        }
        val maxLineNumber = lines.lastOrNull()?.lineNumber ?: 1
        val numberOfDigits = maxLineNumber.toString().length

        VariableShow(
            modifier =Modifier.fillMaxWidth(),
            variableName = "List",
            value = currentState.currentList.joinToString(" "),

        )

        FlowRow {

            Spacer(modifier = Modifier.width(8.dp))

            VariableShow(
                variableName = "minIndex",
                value = if (currentState.minIndex == SelectionSortState.GARBAGE) " " else "${currentState.minIndex}"
            )
            VariableShow(
                variableName = "temp",
                cellSize = 50.dp,
                value = if (currentState.temp == SelectionSortState.GARBAGE) " " else "${currentState.temp}"
            )
            VariableShow(
                variableName = "i",
                cellSize = 50.dp,
                value = if (currentState.i == SelectionSortState.GARBAGE) " " else "${currentState.i}"
            )
            VariableShow(
                variableName = "j",
                cellSize = 50.dp,
                value = if (currentState.j == SelectionSortState.GARBAGE) " " else "${currentState.j}"
            )

        }
        Spacer(modifier = Modifier.height(32.dp))
        for (line in lines) {
            Row {
                Text(
                    text = "${line.lineNumber.toString().padStart(numberOfDigits, '0')}: ",
                    modifier = Modifier.width(24.dp)
                )
                val margin = line.marginFactor * 8
                Spacer(modifier = Modifier.width(margin.dp))
                Text(
                    text = line.text,
                    fontSize = 16.sp,
                    color = line.textColor.value
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }

}

@Composable
private fun VariableShow(
    modifier: Modifier=Modifier,
    variableName: String,
    value: String,
    fontSize: TextUnit = 16.sp,
    cellSize: Dp =Dp.Unspecified
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .size(cellSize)

    ) {

        Box(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Black)

        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                text = value,
                fontSize = fontSize,
            )
        }
        Text(
            text = variableName,
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
        )


    }


}
