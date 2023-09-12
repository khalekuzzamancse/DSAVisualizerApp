package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Immutable
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

    fun removeAsSorted() {
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
    var swapIndex by remember {
        mutableStateOf(Pair(-1, -1))
    }

    Column {
        ListComposable(
            data = ArrayCellValue(listOf(40, 30, 10, 20)),
            minimumIndex = minimumIndex,
            sortedIndex = sortedIndex,
            swapIndex = swapIndex,

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
fun ListComposable(
    data: ArrayCellValue,
    size: Dp = 64.dp,
    swapIndex: Pair<Int, Int>,
    executionFinished: Boolean = false,
    minimumIndex: Int,
    sortedIndex: Int,
) {


    val list by remember {
        mutableStateOf(
            data.list.map {
                ListItem(value = mutableStateOf(it))
            }
        )
    }
    val isValidIndex: (Int) -> Boolean = {
        it >= 0 && it < list.size
    }


    if (executionFinished) {
        list.forEach {
            it.markAsSorted()
        }

    }
    //Swapping Value
    LaunchedEffect(swapIndex ){
        if (isValidIndex(swapIndex.first) && isValidIndex(swapIndex.second)) {
            Log.i("SWAPINDEX", "$swapIndex")
            val temp = list[swapIndex.first].value.value
            list[swapIndex.first].value.value = list[swapIndex.second].value.value
            list[swapIndex.second].value.value=temp
        }
    }

    val resetMinIndex = minimumIndex == -1
    if (resetMinIndex) {
        for (start in list.indices) {
            if (list[start].boolIsMarkedAsMinimum())
                list[start].removeAsMinimum()
        }
    }

    if (minimumIndex >= 0 && minimumIndex < list.size) {
        for (start in 0 until minimumIndex) {
            if (list[start].boolIsMarkedAsMinimum())
                list[start].removeAsMinimum()
        }
        list[minimumIndex].markAsMinimum()
    }
    if (sortedIndex >= 0 && sortedIndex < list.size) {
        list[sortedIndex].markAsSorted()
    }


    val padding = 8.dp
    FlowRow {
        list.forEach {
            Box(
                modifier = Modifier
                    .size(size)
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