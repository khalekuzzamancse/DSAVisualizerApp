package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun DraggableElementPreview() {
    DraggableElement()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DraggableElement() {
    val list = listOf(40, 60, 30, 20, 10, 50)
    val cellWidth = 100.dp
    val numberOfElements = 6
    val density = LocalDensity.current.density
    cellWidth.value * density
    var cellPosition by remember {
        mutableStateOf(mapOf<Int, Offset>())
    }
    var minIndexVariable by remember {
        mutableStateOf(0)
    }
    var cellManager by remember {
        mutableStateOf(CellManager(cellSize = cellWidth))
    }
    var elementManager by remember {
        mutableStateOf(ElementManager())
    }
    val context = LocalContext.current
    var allCellPlaced by remember { mutableStateOf(false) }
    var updateUI by remember(elementManager.elements.size)
    {
        mutableStateOf(false)
    }

    var pointerCurrentPosition by remember {
        mutableStateOf(-Offset(0f, 90f))
    }


    val snapUtils by remember(cellPosition) {
        mutableStateOf(
            SnapUtils(
                cellsPosition = cellPosition,
                density = density,
                cellWidth = cellWidth
            )
        )
    }

    var updateUIKey by remember {
        mutableStateOf(0)
    }


    val runPointer: @Composable () -> Unit = {
        Sort(
            list = list,
            onMinimumIndexChange = {
                minIndexVariable = it
            },
            onMinimumFindFinished = { i, j ->
                if (i != j) {

//                    val temp = arr[i]
//                    arr[i] = arr[minIndex]
//                    arr[minIndex] = temp
                    val a = cellManager.getElementAt(i)
                    val b = cellManager.getElementAt(j)
                    if (a != null && b != null) {
                        cellManager = cellManager.updateCurrentElementOf(i, b)
                        elementManager =
                            elementManager.updatePosition(b.id, cellPosition[i] ?: Offset.Zero)
                        cellManager = cellManager.updateCurrentElementOf(j, a)
                        elementManager =
                            elementManager.updatePosition(a.id, cellPosition[j] ?: Offset.Zero)
                        Log.i("MINIMUM:Finished", "${
                            cellManager.cells.mapValues { it.value.currentElement?.value ?: -1 }
                        }")
                        updateUIKey++
                    }

                }
            },
            onPointerPosition = {
                pointerCurrentPosition = cellPosition[it]!! - Offset(0f, 90f)
            })
    }

    val insertionOnNonEmptyCell: (Int) -> Unit = { cellId ->
        Toast.makeText(
            context,
            "Cell is not Empty\nValue will be replace",
            Toast.LENGTH_LONG
        ).show()
        val existingElement = cellManager.getElementAt(cellId)
        if (existingElement != null) {
            elementManager = elementManager.removeElement(existingElement.id)
        }
        cellManager = cellManager.removeCurrentElement(cellId)
        updateUI = false
    }
    val onDragStart: (Element, Offset) -> Unit = { _, position ->
        val removedCellId = cellManager.findCellIdByPosition(position)
        val isACell = removedCellId != CellManager.NOT_A_CELL
        if (isACell) {
            cellManager = cellManager.removeCurrentElement(removedCellId)
        }
    }
    val onDragEnd: (Element, Offset) -> Offset = { element, position ->
        val nearestCellId: Int =
            snapUtils.findNearestCellId(elementCurrentPosition = position)
        val finalPosition = cellPosition[nearestCellId] ?: position
        val isACell = nearestCellId != SnapUtils.NOT_A_CELL
        if (isACell) {
            if (cellManager.isNotCellEmpty(nearestCellId))
                insertionOnNonEmptyCell(nearestCellId)
            cellManager =
                cellManager.updateCurrentElementOf(cellId = nearestCellId, element = element)

        }

        elementManager = elementManager.updatePosition(element.id, finalPosition)

        finalPosition
    }


    val calculateCellPosition: (Int, LayoutCoordinates) -> Unit = { i, it ->
        val tempCell = cellPosition.toMutableMap()
        tempCell[i] = (it.positionInParent())
        cellPosition = tempCell
        allCellPlaced = cellPosition.size >= numberOfElements
        updateUI = allCellPlaced

    }

    val initializeManagers: () -> Unit = {
        list.forEachIndexed { index, value ->
            val position = cellPosition[index] ?: Offset.Zero
            val element = Element(
                position = position,
                value = value,
                id = index
            )
            elementManager = elementManager.addElement(element)
            cellManager = cellManager
                .addCell(
                    cellId = index,
                    currentElement = element,
                    position = position
                )
        }

    }

    LaunchedEffect(allCellPlaced) {
        initializeManagers()
        updateUI = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
            .padding(top = 50.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FlowRow(modifier = Modifier) {
                for (i in 0 until numberOfElements) {
                    Box(modifier = Modifier
                        .size(cellWidth)
                        .border(color = Color.Black, width = 2.dp)
                        .onGloballyPositioned {
                            calculateCellPosition(i, it)
                        })
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                //temp variable
                //CurrentCellPointingValue(value = currentPointingValue)
                Spacer(modifier = Modifier.width(50.dp))
                Variable(name = "minIndex", value = minIndexVariable.toString())
            }

        }


        if (updateUI) {
            elementManager.elements.forEach {
                val element = it.value
                CellE(
                    label = "${element.value}",
                    currentOffset = element.position,
                    onDragStart = { position ->
                        onDragStart(element, position)
                    },
                    onDragEnd = { position -> onDragEnd(element, position) }
                )

            }
        }
        DisposableEffect(updateUIKey) {
            Log.i("updateUIKey", "$updateUIKey")
            updateUI = false
            onDispose {
            }
        }


        CellPointer(currentOffset = pointerCurrentPosition)
        runPointer()


    }

}

@Composable
fun Sort(
    list: List<Int>,
    onMinimumIndexChange: (Int) -> Unit,
    onMinimumFindFinished: (i: Int, j: Int) -> Unit,
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
                for (j in i until sortedList.size) {
                    delay(1000)
                    onPointerPosition(j)
                    if (sortedList[j] < sortedList[minIndexVariable]) {
                        minIndexVariable = j
                        onMinimumIndexChange(minIndexVariable)
                    }
                }

                onMinimumFindFinished(i, minIndexVariable)
                swap(i, minIndexVariable)
                delay(2000)
            }

        }
        onDispose {
            job.cancel()
        }
    }

}

@Composable
private fun Variable(
    name: String,
    size: Dp = 100.dp,
    value: String,
) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .border(color = Color.Black, width = 2.dp)
        ) {
            Text(
                text = value,
                style = TextStyle(color = Color.Black, fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        Text(
            text = name,
            style = TextStyle(color = Color.Black, fontSize = 12.sp),
        )

    }
}

@Composable
private fun CellE(
    modifier: Modifier = Modifier,
    label: String,
    currentOffset: Offset = Offset(0f, 0f),
    size: Dp = 100.dp,
    onDragStart: (Offset) -> Unit,
    onDragEnd: (Offset) -> Offset,
) {
    var offset by remember { mutableStateOf(currentOffset) }
    val padding = 8.dp
    var globalCoordinate by remember { mutableStateOf<LayoutCoordinates?>(null) }
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
            }
            .onGloballyPositioned {
                globalCoordinate = it
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        globalCoordinate?.let { it1 -> onDragStart(it1.positionInParent()) }
                    },
                    onDragEnd = {
                        globalCoordinate?.let {
                            offset = onDragEnd(it.positionInParent())
                        }
                    },
                    onDrag = { change, dragAmount ->
                        offset = offset.plus(dragAmount)
                        change.consume()
                    }
                )
            }

    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier
                .padding(padding)
                .clip(CircleShape)
                .background(Color.Red)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
private fun CellPointer(
    modifier: Modifier = Modifier,
    currentOffset: Offset = Offset(0f, 0f),
    size: Dp = 100.dp,
) {
    val padding = 8.dp
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                translationX = currentOffset.x
                translationY = currentOffset.y
            }

    ) {
        Icon(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )
    }
}

@Composable
private fun CurrentCellPointingValue(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    value: Int,
) {
    Box(
        modifier = modifier
            .size(size)
            .border(color = Color.Black, width = 2.dp)
    ) {
        Text(
            text = "$value",
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}