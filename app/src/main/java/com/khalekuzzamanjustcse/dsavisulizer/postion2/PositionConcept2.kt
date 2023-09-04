package com.khalekuzzamanjustcse.dsavisulizer.postion2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/*
Concept Used:
*Relation among;Position In Root, Position in Parent,Position in Window
*Transformation of origin(Coordinate geometry)
*find the same position coordinate   with respect two different origin

 */
@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PPP() {
    val cellWidth = 100.dp
    val numberOfElements = 25
    var cellPosition by remember {
        mutableStateOf(mapOf<Int, Offset>())
    }
    var allCellPlaced by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
        ) {
            for (i in 1..numberOfElements) {
                Box(modifier = Modifier
                    .size(cellWidth)
                    .border(color = Color.Black, width = 2.dp)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        val tempCell = cellPosition.toMutableMap()
                        tempCell[i] = (it.positionInParent())
                        cellPosition = tempCell
                        allCellPlaced = cellPosition.size == numberOfElements
                    })

            }


        }


        if (allCellPlaced) {
            for(i in 1..numberOfElements){
                CellE(
                    label = "$i",
                    initialPosition = cellPosition[i] ?: Offset.Zero,
                )
            }

        }

    }


}

@Composable
private fun CellE(
    modifier: Modifier = Modifier,
    label: String,
    initialPosition: Offset = Offset.Zero,
    initialOffset: Offset = Offset(0f, 0f),
    size: Dp = 100.dp
) {
    var offset by remember { mutableStateOf(initialOffset) }
    var initialPositionNotSet by remember { mutableStateOf(true) }
    val density = LocalDensity.current.density
    val padding = 8.dp
    val paddingPx = (padding.value * density)
    val paddingOffset = Offset(paddingPx, paddingPx)

    val initialPositionSetup: (LayoutCoordinates) -> Unit = {
        if (initialPositionNotSet) {
            offset = initialPosition - it.positionInParent()+paddingOffset
            initialPositionNotSet = false
        }

    }

    Box(
        modifier = modifier
            .padding(padding)
            .size(size - padding - padding)
            .offset {
                IntOffset(
                    offset.x.roundToInt(),
                    offset.y.roundToInt()
                )
            }
            .onGloballyPositioned {
                initialPositionSetup(it)
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    offset = offset.plus(dragAmount)
                    change.consume()
                }
            }
            .background(color = Color.Blue)
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

//
