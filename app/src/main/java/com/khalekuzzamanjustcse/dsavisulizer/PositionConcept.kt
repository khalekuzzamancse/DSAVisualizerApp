package com.khalekuzzamanjustcse.dsavisulizer

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
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val density = LocalDensity.current.density
    val n = 5
    //states
    var cellManager by remember { mutableStateOf(CellManager(cellSize = cellWidth)) }
    var currentPositionRelativeToParent by remember { mutableStateOf(emptyMap<Int, Offset>()) }
    var elementManager by remember { mutableStateOf(ElementManager()) }


    //creating cells
    for (i in 1..n) {
        cellManager = cellManager.addCell(id = i)
    }
    //adding elements
    for (i in 1..n) {
        val element = Element(value = i)
        cellManager = cellManager.updateCurrentElement(cellId = i, element = element)
        elementManager = elementManager.addElement(id = i, element)
    }
    val values = cellManager.cells.mapValues {
        it.value.currentElement?.value ?: 0
    }
    Log.i("CellStatus:Initial", "$values")
    //lambdas
    val updateElementPosition: (Int, Offset) -> Unit = { i, positionInRoot ->
        val positionInParent = positionInRoot - cellManager.getCellPositionInRoot(i)
        elementManager = elementManager.updatePositionInParent(i, positionInParent)
        elementManager = elementManager.updatePositionInRoot(i, positionInRoot)
        currentPositionRelativeToParent =
            elementManager.elements.mapValues { (_, element) -> element.positionInParent }
        //

    }


    val onDragEnd: (Int, LayoutCoordinates) -> Unit = { elementId, coordinateAfterDrag ->

        val nearestCellId: Int? = SnapUtils(
            cellsPositionRelativeToRoot = cellManager.cells.mapValues { (_, cell) -> cell.positionInRoot }
                .toMap(),
            currentPositionRelativeToRoot = coordinateAfterDrag.positionInRoot(),
            density = density,
            cellWidth = cellWidth
        ).findNearestCellId()

        //Snap to nearest cell
        if (nearestCellId != null) {
            val cellPosition = cellManager.getCellPositionInRoot(nearestCellId);
            updateElementPosition(elementId, cellPosition)

            val addedAt = cellManager.findCellByPositionInRoot(cellPosition)
            val element=elementManager.getElement(elementId)
            if(element != null)
            cellManager.updateCurrentElement(addedAt, element)
        }

        //finally
        val v = cellManager.cells.mapValues {
            it.value.currentElement?.value ?: 0
        }
        Log.i("CellStatus:Dragged", "$v")

    }

    //UI

    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
        ) {
            elementManager.elements.forEach {
                val elementId = it.key
                Box(modifier = Modifier
                    .size(cellWidth)
                    .border(color = Color.Black, width = 2.dp)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        cellManager =
                            cellManager.updateCellPositionInRoot(
                                cellId = elementId,
                                position = it.positionInRoot()
                            )
                    }) {

                    CellElement(
                        modifier = Modifier
                            .size(cellWidth),
                        offset = currentPositionRelativeToParent[elementId] ?: Offset.Zero,
                        label = "$elementId",
                        onDragStart = {
                            val previousPosition = it.positionInRoot()

                            elementManager = elementManager.updatePreviousPositionInRoot(
                                elementId,
                                previousPosition
                            )
                            val removedFrom = cellManager.findCellByPositionInRoot(previousPosition)
                            cellManager.removeCurrentElement(removedFrom)

                        },
                        onDragEnd = { globalPosition -> onDragEnd(elementId, globalPosition) }
                    )
                }
            }
        }
        //
        TemporaryVariable(
            modifier = Modifier.padding(16.dp),
            cellWidth = cellWidth
        ) {
            cellManager =
                cellManager.addCell(id = n + 1, positionInRoot = it.positionInRoot())

        }

    }


}

@Composable
private fun TemporaryVariable(
    modifier: Modifier = Modifier,
    cellWidth: Dp,
    onGlobalPositionChange: (LayoutCoordinates) -> Unit
) {
    //temporary variable
    Column(modifier = modifier) {
        Text(text = "Temp")
        Box(
            modifier = Modifier
                .size(cellWidth)
                .border(color = Color.Black, width = 2.dp)
                .padding(8.dp)
                .onGloballyPositioned(onGlobalPositionChange)
        )
    }

}

@Composable
private fun CellElement(
    modifier: Modifier = Modifier,
    offset: Offset = Offset.Zero,
    label: String,
    onDragStart: (LayoutCoordinates) -> Unit,
    onDragEnd: (LayoutCoordinates) -> Unit,
) {
    var currentPosition by remember { mutableStateOf(offset) }
    var globalCoordinate by remember { mutableStateOf<LayoutCoordinates?>(null) }


    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    offset.x.toInt() + currentPosition.x.toInt(),
                    offset.y.toInt() + currentPosition.y.toInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        globalCoordinate?.let {
                            onDragStart(it)
                        }
                    },
                    onDragEnd = {
                        globalCoordinate?.let { onDragEnd(it) }
                        currentPosition = offset

                    },
                    onDrag = { change, dragAmount ->
                        currentPosition = currentPosition.plus(dragAmount)
                        change.consume()
                    }
                )
            }
            .background(color = Color.Blue)
            .onGloballyPositioned {
                globalCoordinate = it
            }
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
