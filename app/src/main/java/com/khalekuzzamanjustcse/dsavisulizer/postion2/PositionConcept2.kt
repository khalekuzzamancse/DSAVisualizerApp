package com.khalekuzzamanjustcse.dsavisulizer.postion2

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khalekuzzamanjustcse.dsavisulizer.CellElement
import com.khalekuzzamanjustcse.dsavisulizer.CellManager
import com.khalekuzzamanjustcse.dsavisulizer.Element
import com.khalekuzzamanjustcse.dsavisulizer.ElementManager
import com.khalekuzzamanjustcse.dsavisulizer.SnapUtils

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
    val context = LocalContext.current


    for (i in 1..n) {
        val element = Element(value = i, id = i)
        cellManager = cellManager.addCell(cellId = i, currentElement = element)
        elementManager = elementManager.addElement(element)
    }

    //lambdas
    val updateElementPosition: (Int, Offset) -> Unit = { i, positionInRoot ->
        val positionInParent = positionInRoot - cellManager.getCellPositionInRoot(i)
        elementManager = elementManager.updatePositionInParent(i, positionInParent)
        elementManager = elementManager.updatePositionInRoot(i, positionInRoot)
        currentPositionRelativeToParent =
            elementManager.elements.mapValues { (_, element) -> element.positionInParent }
        //

    }


    val onElementAddedInCell: (Int, Offset) -> Unit = { elementId, cellPosition ->
        val addAtId = cellManager.findCellIdByPositionInRoot(cellPosition)
        if (cellManager.isNotCellEmpty(addAtId)) {
            Toast.makeText(context, "Cell is not Empty\nValue will be replace", Toast.LENGTH_LONG)
                .show()
            //   removeElement(addAtId)
        }
        val element = elementManager.getElement(elementId)
        if (element != null)
            cellManager.updateCurrentElement(addAtId, element)

    }

    val onElementRemovedFromCell: (Offset) -> Unit = { position ->
        val removedFrom = cellManager.findCellIdByPositionInRoot(position)
        cellManager.removeCurrentElement(removedFrom)
    }
    val onPreviousPositionChanged: (Int, Offset) -> Unit = { elementId, previousPosition ->
        elementManager = elementManager.updatePreviousPositionInRoot(
            elementId,
            previousPosition
        )
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
            onElementAddedInCell(elementId, cellPosition)
        }


    }


    //UI

    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
        ) {
            for (i in 1..n) {
                val cellId = i
                Box(modifier = Modifier
                    .size(cellWidth)
                    .border(color = Color.Black, width = 2.dp)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        cellManager =
                            cellManager.updateCellPositionInRoot(
                                cellId = cellId,
                                position = it.positionInRoot()
                            )
                    })
            }

        }


        val offset = cellManager.cells[1]?.positionInRoot ?: Offset.Zero
        Log.i("Position1", "$cellManager")
        Box(
            modifier = Modifier
                .size(cellWidth)
                .offset {
                    IntOffset(
                        offset.x.toInt(), offset.y.toInt()
                    )
                }
                .background(color = Color.Blue)
                .onGloballyPositioned {
                    Log.i("Position1", "${it.positionInRoot()}")
                }
        ) {
            Text(
                text = "1",
                style = TextStyle(color = Color.White, fontSize = 16.sp),
                modifier = Modifier.align(Alignment.Center)
            )
        }


        //
        TemporaryVariable(
            modifier = Modifier.padding(16.dp),
            cellWidth = cellWidth
        ) {
            cellManager =
                cellManager.addCell(cellId = n + 1, positionInRoot = it.positionInRoot())

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


