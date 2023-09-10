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
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay


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
        mutableStateOf(Pair(0,0))
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        ListComposable(
            data = listOf(40, 30, 10, 20),
            minimumIndex = minimumIndex,
            sortedIndex = sortedIndex,
            swapIndex = swapIndex
        )

        HighLightLine(
            onSortedPortionUpdated = {
                sortedIndex=it
            },
            onSwapped = {i,j->
                swapIndex= Pair(i,j)
            },
            onMinimumIndexUpdated = {
                minimumIndex=it
            }
        )
    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HighLightLine(
    onMinimumIndexUpdated: (Int) -> Unit = {},
    onSortedPortionUpdated: (Int) -> Unit = {},
    onSwapped:(Int,Int)->Unit = {i,j->},
) {
    val sequence by remember {
        mutableStateOf(sort())
    }
    Log.i("Seqeucccene", "$sequence")

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
    var list by remember {
        mutableStateOf(emptyList<Int>())
    }
    var minIndex by remember {
        mutableStateOf(-1)
    }
    var temp by remember {
        mutableStateOf(-1)
    }
    var i by remember {
        mutableStateOf(-1)
    }

    var j by remember {
        mutableStateOf(-1)
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
            val currentLine = sequence[sequenceAt].lineNumber - 1
            val previousLine =
                if (sequenceAt == 0) sequence[sequenceAt].lineNumber - 1 else sequence[sequenceAt - 1].lineNumber - 1
            lines[previousLine].textColor.value = Color.Unspecified
            lines[currentLine].textColor.value = Color.Red

            list = states.list
            temp = states.temp
            minIndex = states.minIndex
            i = states.i
            j = states.j
            Log.i("Seqeucccene", "$states")
            if (states.lineNumber == 8) {
                onSortedPortionUpdated(states.i)
                onSwapped(states.i,states.minIndex)

            }
            if (states.lineNumber == 4||states.lineNumber == 7) {
                onMinimumIndexUpdated(states.minIndex)
            }

            if (sequenceAt < sequence.size - 1)
                sequenceAt++
        }) {
            Text(text = "Next")
        }
        val maxLineNumber = lines.lastOrNull()?.lineNumber ?: 1
        val numberOfDigits = maxLineNumber.toString().length

        VariableShow(variableName = "List", value = list.joinToString(" "))

        FlowRow {

            Spacer(modifier = Modifier.width(8.dp))

            VariableShow(
                variableName = "minIndex",
                value = if (minIndex == -1) " " else "$minIndex"
            )
            VariableShow(
                variableName = "temp",
                value = if (temp == -1) " " else "$temp"
            )
            VariableShow(
                variableName = "i",
                value = if (i == -1) " " else "$i"
            )
            VariableShow(
                variableName = "j",
                value = if (j == -1) " " else "$j"
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
    variableName: String,
    value: String,
    fontSize: TextUnit = 16.sp,
    cellSize: Dp = 100.dp
) {
    Column(
        modifier = Modifier
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

data class SelectionSortState(
    val lineNumber: Int,
    val list: List<Int>,
    val i: Int = GARBAGE_VALUE,
    val j: Int = GARBAGE_VALUE,
    val minIndex: Int = GARBAGE_VALUE,
    val temp: Int = GARBAGE_VALUE,
) {
    companion object {
        const val GARBAGE_VALUE = -1
    }
}


@Composable
private fun CodecExecution(
    onExecutionChanged: (currentLine: Int, list: List<Int>, minInd: Int, temp: Int) -> Unit,
    onFinished: () -> Unit,
) {
    val sortedList = remember { mutableListOf(40, 30, 10, 20) }
    val executionTime = 2000L
    LaunchedEffect(Unit) {

        var minIndex: Int = -1
        var temp: Int = -1
        val n = sortedList.size

        onExecutionChanged(2, sortedList, minIndex, temp)// 2:  n = length(arr)
        delay(executionTime)

        onExecutionChanged(3, sortedList, minIndex, temp) //3:  for i from 0 to n-1
        for (i in 0 until n - 1) {

            onExecutionChanged(3, sortedList, minIndex, temp)//
            delay(executionTime)

            minIndex = i // 4: minIndex = i

            onExecutionChanged(4, sortedList, minIndex, temp)
            delay(executionTime)


            onExecutionChanged(5, sortedList, minIndex, temp) // 5: for j from i+1 to n:
            for (j in i + 1 until n) {
                onExecutionChanged(5, sortedList, minIndex, temp)
                delay(executionTime)

                onExecutionChanged(6, sortedList, minIndex, temp)  //6: if arr[j] < arr[minIndex]:
                if (sortedList[j] < sortedList[minIndex]) {
                    minIndex = j
                    delay(executionTime)
                    onExecutionChanged(7, sortedList, minIndex, temp) //7: minIndex = j

                }
            }

            if (minIndex != i) {
                temp = sortedList[i]
                sortedList[i] = sortedList[minIndex]
                sortedList[minIndex] = temp
            }
            delay(executionTime)
            onExecutionChanged(8, sortedList, minIndex, temp) //8: swap(arr[i], arr[minIndex])
        }
        onFinished()
        Log.i("HighLightLine", ": $sortedList")
    }
}

fun sort(): List<SelectionSortState> {
    var sortedList = listOf(40, 30, 10, 20)
    val sequences = mutableListOf<SelectionSortState>()
    val n = sortedList.size
    sequences.add(
        SelectionSortState(
            list = sortedList,
            lineNumber = 2,
            i = -1
        )
    )// 2:  n = length(arr)
    sequences.add(
        SelectionSortState(
            list = sortedList,
            lineNumber = 3,
            i = 0
        )
    ) //3:  for i from 0 to n-1
    for (i in 0 until n - 1) {
        var minIndex = i // 4: minIndex = i
        sequences.add(
            SelectionSortState(
                list = sortedList,
                lineNumber = 4,
                minIndex = minIndex,
                i = i
            )
        )

        sequences.add(
            SelectionSortState(
                list = sortedList,
                lineNumber = 5,
                minIndex = minIndex,
                i = i,
                j = i + 1
            )
        ) // 5: for j from i+1 to n:
        for (j in i + 1 until n) {
            sequences.add(
                SelectionSortState(
                    list = sortedList,
                    lineNumber = 6,
                    minIndex = minIndex,
                    i = i,
                    j = j
                )
            )   //6: if arr[j] < arr[minIndex]:
            if (sortedList[j] < sortedList[minIndex]) {
                minIndex = j
                sequences.add(
                    SelectionSortState(
                        list = sortedList,
                        lineNumber = 7,
                        minIndex = minIndex,
                        i = i,
                        j = j
                    )
                ) //7: minIndex = j
            }

            sequences.add(
                SelectionSortState(
                    list = sortedList,
                    lineNumber = 5,
                    minIndex = minIndex,
                    i = i,
                    j = j + 1
                )
            ) // 5: for j from i+1 to n:
        }
        if (minIndex != i) {
            val tempList = sortedList.toMutableList()
            val temp = sortedList[i]
            tempList[i] = tempList[minIndex]
            tempList[minIndex] = temp
            sortedList = tempList
        }

        sequences.add(
            SelectionSortState(
                list = sortedList,
                lineNumber = 8,
                minIndex = minIndex,
                i = i
            )
        ) //8: swap(arr[i], arr[minIndex])
        sequences.add(
            SelectionSortState(
                list = sortedList,
                lineNumber = 3,
                i = i + 1
            )
        )//3:  for i from 0 to n-1
    }
    return sequences
}
