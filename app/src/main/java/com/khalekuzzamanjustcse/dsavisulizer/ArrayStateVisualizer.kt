package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Range
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ArrayCellItem(
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
    movePointerAt:Int = -1,
    movePointerJ:Int=-1,
    markCellAsBlue: Int,
    markCellAsSort: Int,
    changeCellColor:Pair<Range<Int>,Color> = Pair(Range(-1,-1),Color.Unspecified),
    allCellSorted: Boolean = false,
) {

    val cellStateList by remember {
        mutableStateOf(
            array.list.map {
                ArrayCellItem(value = mutableStateOf(it))
            }
        )
    }

    var cellsPosition by remember {
        mutableStateOf(List(cellStateList.size) { index -> index to Offset.Zero }.toMap())
    }
    var elementPosition by remember {
        mutableStateOf(List(cellStateList.size) { index -> index to Offset.Zero }.toMap())
    }
    val swapElementPosition:(Int,Int)->Unit={i,j->
        val newPositions=elementPosition.toMutableMap()
        val temp=newPositions[i]?:Offset.Zero
        newPositions[i]=newPositions[j]?:Offset.Zero
        newPositions[j]=temp
        elementPosition=newPositions
    }

    LaunchedEffect(cellsPosition){
        elementPosition=cellsPosition.mapValues { it.value }
    }


    val isValidIndex: (Int) -> Boolean = {
        it >= 0 && it < cellStateList.size
    }


//    //changing cell color
//    if(isValidIndex(changeCellColor.first.lower)&&isValidIndex(changeCellColor.first.upper)){
//        cellStateList.
//    }


    if (allCellSorted) {
        cellStateList.forEach {
            it.markAsSorted()
        }


    }
    //Swapping Value
    LaunchedEffect(swapElements) {
        if (isValidIndex(swapElements.first) && isValidIndex(swapElements.second)) {
//            val temp = list[swapElements.first].value.value
//            list[swapElements.first].value.value = list[swapElements.second].value.value
//            list[swapElements.second].value.value = temp
            swapElementPosition(swapElements.first,swapElements.second)
        }

    }

    val resetMinIndex = markCellAsBlue == -1
    if (resetMinIndex) {
        for (start in cellStateList.indices) {
            if (cellStateList[start].boolIsMarkedAsMinimum())
                cellStateList[start].removeAsMinimum()
        }
    }

    if (markCellAsBlue >= 0 && markCellAsBlue < cellStateList.size) {
        for (start in 0 until markCellAsBlue) {
            if (cellStateList[start].boolIsMarkedAsMinimum())
                cellStateList[start].removeAsMinimum()
        }
        cellStateList[markCellAsBlue].markAsMinimum()
    }
    if (markCellAsSort >= 0 && markCellAsSort < cellStateList.size) {
        cellStateList[markCellAsSort].markAsSorted()
    }


    Box(modifier = Modifier.fillMaxSize()) {
        FlowRow(
            modifier = Modifier.border(width = 2.dp, color = Color.Black)
        ) {
            cellStateList.forEachIndexed { index, it ->
                Box(
                    modifier = Modifier
                        .size(size)
                        .border(width = 1.dp, color = Color.Black)
                        .background(it.backgroundColor.value)
                        .onGloballyPositioned { position ->
                            val newPosition = cellsPosition.toMutableMap()
                            newPosition[index] = position.positionInParent()
                            cellsPosition = newPosition
                        }
                )

            }

        }
        //Placing all element at (0,0) so that moving then become  easy
        cellStateList.forEachIndexed { index, it ->
            SwappableElement(
                currentOffset = elementPosition[index]?: Offset.Zero,
                label = "${it.value.value}",
                size = size
            )
        }
        //positioning the cell pointer at (0,0) so that easier to to move
        CellPointer(
            label = "i",
            icon = Icons.Default.ArrowForward,
            currentOffset = cellsPosition[movePointerAt]?.minus(Offset(50f,-65f)) ?: Offset(-100f,-1000f))
        CellPointer(
            label = "j",
            icon = Icons.Default.ArrowForward,
            currentOffset = cellsPosition[movePointerJ]?.minus(Offset(50f,-65f)) ?:
            Offset(-100f,-1000f))

    }

}