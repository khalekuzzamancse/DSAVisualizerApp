package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PPP() {
    val cellWidth = 100.dp
    val numberOfElements = 6
    var cellPosition by remember {
        mutableStateOf(mapOf<Int, Offset>())
    }
    var allCellPlaced by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    val snapUtils by remember(cellPosition) {
        mutableStateOf(
            SnapUtils(
                cellsPosition = cellPosition,
                density = density,
                cellWidth = cellWidth
            )
        )
    }
    val onDragEnd: (Offset) -> Offset = {
        var finalPosition = it
        val nearestCellId: Int =
            snapUtils.findNearestCellId(elementCurrentPosition = it)
        finalPosition = cellPosition[nearestCellId] ?: it
        finalPosition
    }
    val calculateCellPosition: (Int, LayoutCoordinates) -> Unit = { i, it ->
        val tempCell = cellPosition.toMutableMap()
        tempCell[i] = (it.positionInParent())
        cellPosition = tempCell
        allCellPlaced = cellPosition.size == numberOfElements
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        FlowRow(
            modifier = Modifier
        ) {
            for (i in 1..numberOfElements) {
                Box(modifier = Modifier
                    .size(cellWidth)
                    .border(color = Color.Black, width = 2.dp)
                    .onGloballyPositioned {
                        calculateCellPosition(i, it)
                    })
            }
        }

        if (allCellPlaced) {
            for (i in 1..numberOfElements) {
                CellE(
                    label = "$i",
                    currentOffset = cellPosition[i] ?: Offset.Zero,
                    onDragEnd = onDragEnd
                )
            }

        }

    }

}

@Composable
private fun CellE(
    modifier: Modifier = Modifier,
    label: String,
    currentOffset: Offset = Offset(0f, 0f),
    size: Dp = 100.dp,
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
                .background(Color.Red)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}