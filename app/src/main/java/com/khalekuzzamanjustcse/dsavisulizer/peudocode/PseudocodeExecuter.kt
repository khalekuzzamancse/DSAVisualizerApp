package com.khalekuzzamanjustcse.dsavisulizer.peudocode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
val selectionSortPseudocode = listOf(
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

@Preview
@Composable
private fun PseudocodeExecutorPreview() {

    val executingLine by remember {
        mutableStateOf(2)
    }

    PseudocodeExecutor(
        codes = selectionSortPseudocode,
        executingLine = executingLine,
        lineStatus = Pair(3,"i= 1")
    )

}

@Composable
fun PseudocodeExecutor(
    codes: List<PseudocodeLine>,
    executingLine: Int = -1,
    lineStatus:Pair<Int,String> = Pair(-1,""),
) {
    val maxLineNumber = codes.lastOrNull()?.lineNumber ?: 1
    val numberOfDigits = maxLineNumber.toString().length

    Column {
        codes.forEachIndexed { index, line ->
            Row {
                Text(
                    text = "${line.lineNumber.toString().padStart(numberOfDigits, '0')} : ",
                    modifier = Modifier.width(24.dp)
                )
                val margin = line.marginFactor * 8
                Spacer(modifier = Modifier.width(margin.dp))
                Text(
                    text = line.text,
                    fontSize = 16.sp,
                    color = if (executingLine == index + 1) Color.Red else Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                if(lineStatus.first==index + 1){
                    Text(
                        text = " // ${lineStatus.second}",
                        fontSize = 16.sp,
                        color = Color.Blue
                    )
                }
            }
        }
    }

}