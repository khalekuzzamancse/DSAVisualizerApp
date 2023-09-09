package com.khalekuzzamanjustcse.dsavisulizer

import PseudocodeLine
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
private fun HighLightLine() {
    val lines by remember {
        mutableStateOf(
            listOf(
                PseudocodeLine(1, "selectionSort(arr):"),
                PseudocodeLine(2, "n = length of arr"),
                PseudocodeLine(3, "for i from 0 to n - 1"),
                PseudocodeLine(4, "minIndex = i"),
                PseudocodeLine(5, "for j from i + 1 to n - 1"),
                PseudocodeLine(6, "if arr[j] < arr[minIndex]"),
                PseudocodeLine(7, "minIndex = j"),
                PseudocodeLine(8, "swap(arr[i], arr[minIndex])"),
            )
        )
    }

    var executionAt by remember {
        mutableStateOf(1)
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


    ElevatedCard(
        shape = RectangleShape,
        modifier = Modifier
            .border(width = 1.dp, Color.Black)
            .padding(16.dp)

    ) {
        val maxLineNumber = lines.lastOrNull()?.lineNumber ?: 1
        val numberOfDigits = maxLineNumber.toString().length

        VariableShow(variableName = "List", value = list.joinToString(" "))

        Row {

            Spacer(modifier = Modifier.width(8.dp))

            VariableShow(variableName = "minIndex", value = "$minIndex")
            VariableShow(variableName = "temp", value = "$temp")

        }
        Spacer(modifier = Modifier.height(32.dp))
        for (line in lines) {
            Row {
                Text(
                    text = "${line.lineNumber.toString().padStart(numberOfDigits, '0')}: ",
                    modifier = Modifier.width(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = line.text,
                    fontSize = 16.sp,
                    color = line.textColor.value
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }

    CodecExecution { it, sortedList, minInd, tmp ->
        if (it == 3 || it == 5)
            lines[7].textColor.value = Color.Unspecified
        if (it > 1)
            lines[it - 2].textColor.value = Color.Unspecified

        lines[it - 1].textColor.value = Color.Red
        list = sortedList
        minIndex = minInd
        temp = tmp


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


@Composable
private fun CodecExecution(onExecutionChanged: (currentLine: Int, list: List<Int>, minInd: Int, temp: Int) -> Unit) {
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
            delay(executionTime)
            minIndex = i // 4: minIndex = i
            onExecutionChanged(4, sortedList, minIndex, temp)
            delay(executionTime)
            onExecutionChanged(5, sortedList, minIndex, temp) // 5: for j from i+1 to n:
            for (j in i + 1 until n) {
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
        Log.i("HighLightLine", ": $sortedList")
    }
}
