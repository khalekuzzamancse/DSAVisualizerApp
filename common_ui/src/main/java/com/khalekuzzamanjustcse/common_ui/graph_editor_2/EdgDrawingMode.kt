package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DrawingCanvas() {

    var path by remember { mutableStateOf(Path()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { (x, y) ->

                       //     Log.i("TappedOnCanvas")
                            path = if (path.isEmpty) {
                                Path().apply {
                                    addPath(path)
                                    moveTo(x, y)
                                }
                            } else {
                                Path().apply {
                                    addPath(path)
                                    path.lineTo(x, y)
                                }

                            }
                        }
                    )
                }

        ) {
            Canvas(modifier = Modifier.fillMaxSize()){
                drawPath(
                    path = path, color = Color.Black, style = Stroke(3f)
                )
            }

        }

    }


}