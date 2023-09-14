package com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
private fun HistoryTablePreview() {
    val history = listOf(
        listOf(1, 2, 3, 4),
        listOf(5, 6, 7, 8),
        listOf(9, 10, 11, 12)
    )
    HistoryTable(history)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryTable(
    list: List<List<Int>>
) {
    FlowRow(modifier = Modifier.fillMaxWidth()) {
        list.forEach {
            SingleColumn(list = it)
        }
    }

}

@Composable
private fun SingleColumn(
    modifier: Modifier = Modifier,
    list: List<Int>,
    cellSize: Dp = 64.dp
) {
    Column(modifier = modifier
        .padding(top=8.dp)) {
        list.forEach {
            Box(
                modifier = modifier
                    .size(cellSize)
                    .border(width = 1.dp, color = Color.Black)
            ) {
                Text(
                    text = if (it == PageReplacementState.EMPTY) "" else "$it",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )

            }
        }
    }

}