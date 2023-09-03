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

    //adding elements
    for (i in 1..n) {
        elementManager = elementManager.addElement(id = i, Element(value = i))
    }
    //lambdas
    val updateCurrentPositionRelativeParent: (Int, Offset) -> Unit = { i, positionInRoot ->
        val position = positionInRoot - cellManager.getCellPositionInRoot(i)
        elementManager = elementManager.updatePositionInParent(i, position)
        currentPositionRelativeToParent =
            elementManager.elements.mapValues { (_, element) -> element.positionInParent }

    }
    val updateCurrentPositionRelativeRoot: (Int, Offset) -> Unit = { i, positionInRoot ->
        elementManager = elementManager.updatePositionInRoot(i, positionInRoot)
    }


    val onGlobalPositionChange: (Int, LayoutCoordinates) -> Unit = { i, it ->
        updateCurrentPositionRelativeParent(i, it.positionInRoot())
        updateCurrentPositionRelativeRoot(i, it.positionInRoot())
        val cellPositionInRoot =
            cellManager.cells.mapValues { (_, cell) -> cell.positionInRoot }.toMap()
        val currentPositionInRoot = elementManager.getElement(i)?.positionInRoot ?: Offset.Zero
        //removed from cell
        cellManager = cellManager.removeCurrentElement(i)
        //find nearest cell to snap
        val nearestCellId: Int? = SnapUtils(
            cellsPositionRelativeToRoot = cellPositionInRoot,
            currentPositionRelativeToRoot = currentPositionInRoot,
            density = density,
            cellWidth = cellWidth
        ).findNearestCellId()
        //

        if (nearestCellId != null) {
            val cellPosition = cellManager.getCellPositionInRoot(nearestCellId);
            updateCurrentPositionRelativeParent(i, cellPosition)

            val element = elementManager.getElement(i)
            if (element != null) {
                cellManager = cellManager.updateCurrentElement(nearestCellId, element)

            }


        }

        //printing
       val values= cellManager.cells.mapValues {
           it.value.currentElement?.value ?: 0
        }
        Log.i("CellStatus","$values")

    }

    //UI

    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
        ) {
            elementManager.elements.forEach {
                val i = it.key
                Box(modifier = Modifier
                    .size(cellWidth)
                    .border(color = Color.Black, width = 2.dp)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        cellManager =
                            cellManager.addCell(id = i, positionInRoot = it.positionInRoot())

                    }) {


                    CellElement(
                        modifier = Modifier
                            .size(cellWidth),
                        offset = currentPositionRelativeToParent[i] ?: Offset.Zero,

                        label = "$i"
                    ) { globalPosition -> onGlobalPositionChange(i, globalPosition) }
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
    onGlobalPositionChange: (LayoutCoordinates) -> Unit
) {
    var accumulatedDrag by remember { mutableStateOf(offset) }
    var globalPosition by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    offset.x.toInt() + accumulatedDrag.x.toInt(),
                    offset.y.toInt() + accumulatedDrag.y.toInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        globalPosition?.let { onGlobalPositionChange(it) }
                        accumulatedDrag = offset
                    }
                ) { change, dragAmount ->
                    accumulatedDrag = accumulatedDrag.plus(dragAmount)
                    change.consume()
                }
            }
            .background(color = Color.Blue)
            .onGloballyPositioned {
                globalPosition = it
            }
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
