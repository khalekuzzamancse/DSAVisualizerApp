package com.khalekuzzamanjustcse.dsavisulizer.postion2

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khalekuzzamanjustcse.dsavisulizer.SnapUtils
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
    val numberOfElements = 6
    var cellPosition by remember {
        mutableStateOf(mapOf<Int, Offset>())
    }
    var allCellPlaced by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density


    Column(
        modifier = Modifier
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

        var currentOffset by remember {
            mutableStateOf(Offset(0f,0f))
        }

        if (allCellPlaced) {
                CellE(
                    label = "$1",
                    initialPosition = cellPosition[1] ?: Offset.Zero,
                    currentOffset =currentOffset
                ) {

                }
            }

        }

    }




@Composable
private fun CellE(
    modifier: Modifier = Modifier,
    label: String,
    initialPosition: Offset = Offset.Zero,
    currentOffset: Offset = Offset(0f, 0f),
    size: Dp = 100.dp,
    onDragEnd: (Offset) -> Unit,
) {
    var offset by remember { mutableStateOf(currentOffset) }
    var initialPositionNotSet by remember { mutableStateOf(true) }
    val padding = 8.dp

    var globalCoordinate by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val initialPositionSetup: (LayoutCoordinates) -> Unit = {
        if (initialPositionNotSet) {
            offset = initialPosition - it.positionInParent()
            initialPositionNotSet = false
        }

    }

    Box(
        modifier = modifier
            .size(size)
            .offset {
                IntOffset(
                    offset.x.roundToInt(),
                    offset.y.roundToInt()
                )
            }
            .onGloballyPositioned {
                initialPositionSetup(it)
                globalCoordinate = it

            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        globalCoordinate?.let { onDragEnd(it.positionInParent()) }

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

//
