package com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random


data class PositionHolder(
    val numberOfCells: Int,
    val numberOfElements: Int
) {
    val cellsPosition: List<MutableState<Offset>> = List(numberOfCells) {
        mutableStateOf(Offset.Zero)
    }
    val elementInitialPosition: List<MutableState<Offset>> = List(numberOfElements) {
        mutableStateOf((Offset.Zero))
    }
    val elementCurrentOffset: List<MutableState<Offset>> = List(numberOfElements) {
        mutableStateOf((Offset.Zero))
    }

    private fun isValidCell(cellNo: Int) = cellNo in 0 until numberOfCells
    private fun isValidElement(elementNo: Int) = elementNo in 0 until numberOfElements
    fun updateElementCurrentPosition(index: Int, position: Offset) {
        if (index in 0 until numberOfElements)
            elementInitialPosition[index].value = position
    }

    fun updateElementCurrentOffset(index: Int, offset: Offset) {
        if (index in 0 until numberOfElements)
            elementCurrentOffset[index].value = offset
    }

    fun distanceBetweenCellAndElementCurrentPosition(cellNo: Int, elementNo: Int): Offset {
        if (isValidCell(cellNo) && isValidElement(elementNo)) {
            return cellsPosition[cellNo].value - elementInitialPosition[elementNo].value
        }
        return Offset.Zero
    }

    fun moveToCell(cellNo: Int, elementNo: Int) {
        if (isValidCell(cellNo) && isValidElement(elementNo)) {
            val moveBy = distanceBetweenCellAndElementCurrentPosition(cellNo, elementNo)
            updateElementCurrentOffset(elementNo, moveBy)
        }
    }

    fun vanishElement(elementNo: Int) {
        if (isValidElement(elementNo))
            updateElementCurrentOffset(elementNo, Offset.Infinite)

    }

    fun updateCellPosition(index: Int, position: Offset) {
        if (index in 0 until numberOfCells)
            cellsPosition[index].value = position
    }

}

data class PageLoad(
    val memoryCellNo: Int,
    val pageRequestNo: Int,
)

@Preview
@Composable
fun VisualMemoryPreview() {

    var loadPage by remember {
        mutableStateOf(PageLoad(-1, -1))
    }
    val pageRequests = listOf(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 1, 3, 2, 0)
    val memoryCapacity = 3
    Column {
        Button(onClick = {
            val requestNo = Random.nextInt(pageRequests.size - 1)
            val cellNo = Random.nextInt(memoryCapacity - 1)
            loadPage = PageLoad(cellNo, requestNo)
        }) {
            Text(text = "Load")
        }

        VisualMemory(
            memoryCapacity = memoryCapacity,
            pageRequests = pageRequests,
            loadPage = loadPage,
            vanishElement = 2
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualMemory(
    memoryCellSize: Dp = 64.dp,
    memoryCapacity: Int,
    pageRequests: List<Int>,
    vanishElement: Int = -1,
    loadPage: PageLoad = PageLoad(-1, -1),
) {


    val positions by remember {
        mutableStateOf(
            PositionHolder(
                numberOfCells = memoryCapacity,
                numberOfElements = pageRequests.size
            )
        )
    }

    LaunchedEffect(loadPage) {
        positions.moveToCell(loadPage.memoryCellNo, loadPage.pageRequestNo)

    }
    LaunchedEffect(vanishElement){
        positions.vanishElement(vanishElement)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            for (i in 0 until memoryCapacity) {
                ArrayCell(cellSize = memoryCellSize) {
                    positions.updateCellPosition(i, it.positionInRoot())
                }
            }
        }
        //

        FlowRow {
            pageRequests.forEachIndexed { index, value ->
                Box(modifier =
                Modifier
                    .size(memoryCellSize)
                    .onGloballyPositioned {
                        positions.updateElementCurrentPosition(index, it.positionInRoot())
                    }) {
                    SwappableElement2(
                        label = "$value",
                        size = memoryCellSize,
                        currentOffset = positions.elementCurrentOffset[index].value
                    )
                }
            }
        }


    }
}


@Composable
private fun ArrayCell(
    modifier: Modifier = Modifier,
    cellSize: Dp,
    backgroundColor: Color = Color.Unspecified,
    onPositionChanged: (LayoutCoordinates) -> Unit,
) {
    Box(
        modifier = modifier
            .size(cellSize)
            .border(width = 1.dp, color = Color.Black)
            .background(backgroundColor)
            .onGloballyPositioned { position ->
                onPositionChanged(position)
            }
    )

}

@Composable
fun SwappableElement2(
    label: String,
    size: Dp,
    currentOffset: Offset = Offset.Zero,
) {
    val offsetAnimation by animateOffsetAsState(currentOffset, label = "")
    val padding = 8.dp
    Box(
        modifier = Modifier
            .size(size)
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
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

