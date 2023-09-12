package com.khalekuzzamanjustcse.dsavisulizer

import PseudocodeText
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Preview
@Composable
fun Demo() {
    DraggableElementPreview()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraggableElementPreview() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
            .fillMaxSize()
    ) {
        var text by remember {
            mutableStateOf("60 50 40 30 20 10")
        }
        var start by remember { mutableStateOf(false) }
        var list by remember {
            mutableStateOf(emptyList<Int>())
        }

        ElevatedCard(
            shape = RectangleShape,
            modifier = Modifier.padding(4.dp).
            border(width =1.dp, Color.Black)

        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = text,
                onValueChange = { text = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                val elements = text.split("[,\\s]+".toRegex())
                list = elements.mapNotNull { it.toIntOrNull() }
                start=!start
            }) {
                Text("Visualize")
            }

        }

        Spacer(modifier = Modifier.height(20.dp))
        if(start){
            DraggableElement(list)
        }
        Spacer(modifier = Modifier.height(20.dp))
        PseudocodeText(modifier = Modifier.padding(4.dp))

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DraggableElement(list: List<Int>) {
    val cellWidth = 50.dp
    val density = LocalDensity.current.density
    cellWidth.value * density
    var cellPosition by remember {
        mutableStateOf(mapOf<Int, Offset>())
    }
    val elements by remember {
        mutableStateOf(list.map { Element(value = it) })
    }
    val arrayCells by remember {
        mutableStateOf(list.map { ArrayCell(cellSize = cellWidth) })
    }
    val updatePositionOf: (Int, Offset) -> Unit = { index, newPosition ->
        elements[index].position.value = newPosition
    }
    val tempVariableCell by remember {
        mutableStateOf(ArrayCell(cellSize = cellWidth))
    }


    var cellPlaced by remember {
        mutableStateOf(false)
    }
    var snapUtils by remember(cellPosition) {
        mutableStateOf(
            SnapUtils(
                cellsPosition = cellPosition,
                density = density,
                cellWidth = cellWidth
            )
        )
    }


    val calculateCellPosition: (Int, LayoutCoordinates) -> Unit = { i, it ->
        val position = it.positionInParent()
        val tempCell = cellPosition.toMutableMap()
        tempCell[i] = position
        cellPosition = tempCell
        //
        arrayCells[i].position.value = position

        //
        cellPlaced = cellPosition.size >= list.size
    }
    val getCellCurrentElement: (Int) -> Element = { cellIndex ->
        val elementIndex = arrayCells[cellIndex].currentElementReference.value
        if (elementIndex != null) elements[elementIndex] else Element(-1)
    }
    val updateCurrentElement: (cellIndex: Int, elementIndex: Int) -> Unit =
        { cellIndex, elementIndex ->
            arrayCells[cellIndex].currentElementReference.value = elementIndex
        }
    val removeCurrentElement: (cellIndex: Int) -> Unit = { cellIndex ->
        arrayCells[cellIndex].currentElementReference.value = null
    }
    val findCellOfElement: (elementIndex: Int) -> Int = { elementIndex ->
        var ans = -1
        arrayCells.forEachIndexed { index, it ->
            if (it.currentElementReference.value == elementIndex) ans = index
        }
        ans

    }
    val coroutineScope = rememberCoroutineScope()
    val swapCellElement: (Int, Int) -> Unit = { i, j ->
        val iThCellElementRef = arrayCells[i].currentElementReference.value
        val jThCellElementRef = arrayCells[j].currentElementReference.value
        getCellCurrentElement(i).position.value = arrayCells[j].position.value
        getCellCurrentElement(j).position.value = arrayCells[i].position.value
        updateCurrentElement(i, jThCellElementRef ?: -1)
        updateCurrentElement(j, iThCellElementRef ?: -1)

//
        //new code with temporary variables ,fixed the bug later insha-allah
//        coroutineScope.launch {
//            val iThCellElementRef = arrayCells[i].currentElementReference.value
//            val jThCellElementRef = arrayCells[j].currentElementReference.value
//
//            val iThCellElement = getCellCurrentElement(iThCellElementRef ?: -1)
//            val jThCellElement = getCellCurrentElement(jThCellElementRef ?: -1)
//
//            iThCellElement.position.value = tempVariableCell.position.value
//            updateCurrentElement(j, iThCellElementRef ?: -1)
//            delay(500)
//            jThCellElement.position.value = arrayCells[i].position.value
//            delay(500)
//            iThCellElement.position.value = arrayCells[j].position.value
//            updateCurrentElement(i, jThCellElementRef ?: -1)
//
//        }

    }


    LaunchedEffect(cellPlaced) {
        elements.forEachIndexed { index, element ->
            updatePositionOf(index, cellPosition[index] ?: Offset.Zero)
            arrayCells[index].currentElementReference.value = index
        }
        snapUtils = SnapUtils(
            cellsPosition = cellPosition,
            density = density,
            cellWidth = cellWidth
        )

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FlowRow(modifier = Modifier) {
                arrayCells.forEachIndexed { index, cell ->
                    Box(modifier = Modifier
                        .size(cellWidth)
                        .border(color = Color.Black, width = 2.dp)
                        .background(cell.color.value)
                        .onGloballyPositioned { cellPosition ->
                            calculateCellPosition(index, cellPosition)
                        })
                }
            }

//            Box(modifier = Modifier
//                .size(cellWidth)
//                .border(color = Color.Black, width = 2.dp)
//                .onGloballyPositioned { cellPosition ->
//                    tempVariableCell.position.value = cellPosition.positionInParent()
//                })

        }


        elements.forEachIndexed { index, element ->
            DraggableElement(
                size=cellWidth,
                label = "${element.value}",
                currentOffset = elements[index].position.value,
                onDragStart = {
                    val cellIndex = findCellOfElement(index)
                    if (cellIndex != -1)
                        removeCurrentElement(cellIndex)
                },
                onDragEnd = {
                    val nearestCellId =
                        snapUtils.findNearestCellId(elementCurrentPosition = element.position.value)
                    val finalPosition = cellPosition[nearestCellId] ?: element.position.value
                    element.position.value = finalPosition
                    val isACell = nearestCellId != SnapUtils.NOT_A_CELL
                    if (isACell)
                        updateCurrentElement(nearestCellId, index)

                }
            ) { dragAmount ->
                element.position.value += dragAmount
            }
        }
        var pointerCurrentPosition by remember {
            mutableStateOf(-Offset(0f, 90f))
        }
        CellPointer(label="i",currentOffset = pointerCurrentPosition)
        val runPointer: @Composable () -> Unit = {
            var previousMinIndex by remember { mutableStateOf(0) }
            SelectionSort(
                list = list,
                onMinimumIndexChange = { index ->
                    arrayCells[index].color.value = Color.Blue
                    arrayCells[previousMinIndex].color.value = Color.Unspecified
                    previousMinIndex = index

                },
                onMinimumFindFinished = { i, j ->
                    if (i != j)
                        swapCellElement(i, j)
                },
                onSortedPartitionUpdate = { i ->
                    for (index in 0..i)
                        arrayCells[index].color.value = Color.Yellow
                },
                onPointerPosition = {
                    pointerCurrentPosition = cellPosition[it]!! - Offset(0f, 90f)
                })
        }
        runPointer()


    }


}


@Composable
fun SelectionSort(
    list: List<Int>,
    onMinimumIndexChange: (Int) -> Unit,
    onMinimumFindFinished: (i: Int, j: Int) -> Unit,
    onSortedPartitionUpdate: (Int) -> Unit,
    onPointerPosition: (Int) -> Unit,
) {
    var sortedList by remember {
        mutableStateOf(list)
    }
    val swap: (Int, Int) -> Unit = { i, minIndexVariable ->
        val tempList = sortedList.toMutableList()
        val temp = tempList[i]
        tempList[i] = tempList[minIndexVariable]
        tempList[minIndexVariable] = temp
        sortedList = tempList
    }
    DisposableEffect(Unit) {
        val scope = CoroutineScope(Dispatchers.Default)
        val job = scope.launch {
            for (i in sortedList.indices) {
                var minIndexVariable = i
                onMinimumIndexChange(i)
                for (j in i + 1 until sortedList.size) {
                    delay(1000)
                    onPointerPosition(j)
                    if (sortedList[j] < sortedList[minIndexVariable]) {
                        minIndexVariable = j
                        onMinimumIndexChange(minIndexVariable)
                    }
                }
                onMinimumFindFinished(i, minIndexVariable)
                swap(i, minIndexVariable)
                onSortedPartitionUpdate(i)
                delay(3000)

            }

        }
        onDispose {
            job.cancel()
        }
    }

}

