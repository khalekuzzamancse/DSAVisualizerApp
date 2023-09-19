package com.khalekuzzamanjustcse.tree_visualization.visualizer

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.tree_visualization.TreeTraversalIntermediateDS
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun NextProcessingNodesPreview() {
    var popped by remember {
        mutableStateOf(false)
    }
    var newlyAdded by remember { mutableStateOf(listOf(10, 20, 30, 40)) }
    var type by remember { mutableStateOf(TreeTraversalIntermediateDS.Queue) }

    Column {
        FlowRow {
            Button(onClick = {
                popped = true
            }) {
                Text(text = "Pop")
            }
            Button(onClick = {
                newlyAdded = listOf(20, 30, 40)
            }) {
                Text(text = "Add(20,30,40)")
            }
            Button(onClick = {
                if (type == TreeTraversalIntermediateDS.Queue)
                    type = TreeTraversalIntermediateDS.Stack
                else
                    type = TreeTraversalIntermediateDS.Queue

            }) {
                Text(text = "Stack/Queue")
            }


        }


        NextProcessingNodes(
            newlyAdded = newlyAdded,
            removed = popped,
            onRemovedFinished = { popped = false },
            onAddedFinished = { newlyAdded = emptyList() }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NextProcessingNodes(
    modifier: Modifier=Modifier,
    types: TreeTraversalIntermediateDS = TreeTraversalIntermediateDS.Queue,
    newlyAdded: List<Int>,
    onRemovedFinished: () -> Unit,
    processing:String="",
    onAddedFinished: () -> Unit,
    removed: Boolean = false,
) {
    val cellSize = 100.dp
    var array by remember {
        mutableStateOf(emptyList<Int>())
    }
    LaunchedEffect(newlyAdded) {
        newlyAdded.forEach {
            val tempList = array.toMutableList()
            tempList.add(it)
            delay(500)
            array = tempList
        }
        onAddedFinished()
    }
    LaunchedEffect(removed) {
        if (removed) {
            if (types == TreeTraversalIntermediateDS.Queue) {
                val tempList = array.toMutableList()
                tempList.removeFirst()
                array = tempList
                onRemovedFinished()
            } else {
                val tempList = array.toMutableList()
                tempList.removeLast()
                array = tempList
                onRemovedFinished()
            }

        }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Box {
            Text(text = "Processing Node:$processing")
        }
        FlowRow(
            modifier = Modifier
                .padding(16.dp)
                .border(width = 2.dp, color = Color.Black)
        ) {
            array.forEachIndexed { i, it ->

                LinkedListElement(
                    data = "$it",
                    cellSize = cellSize,
                )
            }
        }
    }

}

@Composable
fun LinkedListElement(
    data: String,
    cellSize: Dp,
    currentOffset: Offset = Offset.Zero,
) {
    val offsetAnimation by animateOffsetAsState(currentOffset, label = "")
    Box(
        modifier = Modifier
            .size(cellSize)
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            }
            .border(width = 1.dp, color = Color.Black)
            .onGloballyPositioned {
            }
    ) {
        Text(
            text = data,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }


}