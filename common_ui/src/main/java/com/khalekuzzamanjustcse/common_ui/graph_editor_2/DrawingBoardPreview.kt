package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun DrawingBoardPreview() {
    WhiteBoard(onDismissButtonClick = {})
}


@Composable
fun WhiteBoard(modifier: Modifier = Modifier, onDismissButtonClick: () -> Unit) {
    var path by remember { mutableStateOf(Path()) }
    var paths by remember { mutableStateOf(mutableListOf<Path>()) }
    var previousPosition: Offset = Offset.Zero
    var currentPosition: Offset


    val updatePathOnDragStart: (Offset) -> Unit = { offset ->
        path = Path().apply {
            addPath(path)
            moveTo(offset.x, offset.y)
        }
        currentPosition = offset
        previousPosition = currentPosition
    }
    val updatePathOnDragging: (Offset) -> Unit = { offset ->
        currentPosition = offset
        path = Path().apply {
            addPath(path)
            quadraticBezierTo(
                previousPosition.x,
                previousPosition.y,
                (previousPosition.x + currentPosition.x) / 2,
                (previousPosition.y + currentPosition.y) / 2

            )
            previousPosition = currentPosition
        }
    }
    val undo: () -> Unit = {
        if (paths.isNotEmpty()) {
            paths.removeAt(paths.size - 1)
            path = if (paths.isNotEmpty()) paths.last() else Path()
        }
    }
    val cleanBoard: () -> Unit = {
        paths = mutableListOf()
        previousPosition = Offset.Zero
        currentPosition = Offset.Zero
        path = Path()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        updatePathOnDragStart(offset)
                    },
                    onDrag = { change, _ ->
                        updatePathOnDragging(change.position)
                    },
                    onDragEnd = {
                        paths.add(path)
                        currentPosition = Offset.Unspecified
                        previousPosition = currentPosition
                    }
                )
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(shadowElevation = 5.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {

                    Icon(
                        modifier = Modifier.clickable { cleanBoard() },
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        modifier = Modifier.clickable { undo() },
                        imageVector = Icons.Default.Undo,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        modifier = Modifier.clickable { onDismissButtonClick() },
                        imageVector = Icons.Default.Done,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        modifier = Modifier.clickable {
                            paths.forEach {


                                                      }
                        },
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )

                }
            }

            Canvas(modifier = Modifier.weight(1f)) {
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 4.dp.toPx())
                )
            }


        }

    }
}
