package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ListItem(
    val value: MutableState<Int>,
    val backgroundColor: MutableState<Color> = mutableStateOf(UNSORTED_ELEMENT_COLOR),
    val valueColor: MutableState<Color> = mutableStateOf(Color.Red),
) {
    companion object {
        private val MINIMUM_ELEMENT_COLOR = Color.Green
        private val SORTED_ELEMENT_COLOR = Color.Yellow
        private val UNSORTED_ELEMENT_COLOR = Color.Unspecified
    }

    fun markAsMinimum() {
        backgroundColor.value = MINIMUM_ELEMENT_COLOR
    }

    fun markAsSorted() {
        backgroundColor.value = SORTED_ELEMENT_COLOR
    }

    fun removeAsMinimum() {
        backgroundColor.value = UNSORTED_ELEMENT_COLOR
    }


    fun boolIsMarkedAsMinimum() = backgroundColor.value == MINIMUM_ELEMENT_COLOR

}

@Preview
@Composable
private fun ListViewPreview() {

    var minimumIndex by remember {
        mutableStateOf(3)
    }
    var sortedIndex by remember {
        mutableStateOf(0)
    }
    val swapIndex by remember {
        mutableStateOf(Pair(-1, -1))
    }

    Column {
        ArrayStateVisualizer(
            array = ArrayCellValue(listOf(40, 30, 10, 20)),
            markCellAsBlue = minimumIndex,
            markCellAsSort = sortedIndex,
            swapElements = swapIndex,

            )
        Button(onClick = {
            minimumIndex--
            sortedIndex++
        }) {
            Text(text = "Change")
        }
    }


}


data class ArrayCellValue(
    val list: List<Int>
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArrayStateVisualizer(
    array: ArrayCellValue,
    size: Dp = 64.dp,
    swapElements: Pair<Int, Int>,
    markCellAsBlue: Int,
    markCellAsSort: Int,
    allCellSorted: Boolean = false,
) {


    val list by remember {
        mutableStateOf(
            array.list.map {
                ListItem(value = mutableStateOf(it))
            }
        )
    }
    val isValidIndex: (Int) -> Boolean = {
        it >= 0 && it < list.size
    }


    if (allCellSorted) {
        list.forEach {
            it.markAsSorted()
        }

    }
    //Swapping Value
    LaunchedEffect(swapElements) {
        if (isValidIndex(swapElements.first) && isValidIndex(swapElements.second)) {
            val temp = list[swapElements.first].value.value
            list[swapElements.first].value.value = list[swapElements.second].value.value
            list[swapElements.second].value.value = temp
        }
    }

    val resetMinIndex = markCellAsBlue == -1
    if (resetMinIndex) {
        for (start in list.indices) {
            if (list[start].boolIsMarkedAsMinimum())
                list[start].removeAsMinimum()
        }
    }

    if (markCellAsBlue >= 0 && markCellAsBlue < list.size) {
        for (start in 0 until markCellAsBlue) {
            if (list[start].boolIsMarkedAsMinimum())
                list[start].removeAsMinimum()
        }
        list[markCellAsBlue].markAsMinimum()
    }
    if (markCellAsSort >= 0 && markCellAsSort < list.size) {
        list[markCellAsSort].markAsSorted()
    }


    val padding = 8.dp
    FlowRow(
        modifier = Modifier.border(width = 2.dp, color = Color.Black)
    ) {
        list.forEach {
            Box(
                modifier = Modifier
                    .size(size)
                    .border(width = 1.dp, color = Color.Black)
                    .background(it.backgroundColor.value)
            ) {
                Text(
                    text = "${it.value.value}",
                    style = TextStyle(color = Color.White, fontSize = 16.sp),
                    modifier = Modifier
                        .padding(padding)
                        .clip(CircleShape)
                        .background(it.valueColor.value)
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

        }

    }
}