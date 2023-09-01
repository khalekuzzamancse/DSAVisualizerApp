package com.khalekuzzamanjustcse.auth.ui.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun P() {
    val list = mutableListOf(1, 2, 3, 4, 5)
    val cells = remember { mutableStateListOf<Rect>() }
    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            list.forEachIndexed{index, it ->
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black, shape = RectangleShape)
                        .padding(4.dp)
                        .onGloballyPositioned { coordinates ->
                            if (cells.size <= index) {
                                cells.add(coordinates.boundsInWindow())
                            }
                        }

                ) {
                    Element(text = it.toString())
                }

            }
        }
        cells.forEach {
            Log.i("Positinoded","$it")
        }
        Spacer(modifier = Modifier.height(48.dp))
        FlowRow {
            list.forEach {
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .border(width = 2.dp, color = Color.Black, shape = RectangleShape)
                        .padding(4.dp)

                ) {

                }

            }
        }
    }


}

@Composable
private fun Element(text: String) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)

            .graphicsLayer {
                translationX = offsetX
                translationY = offsetY
                clip = true
                shape = CircleShape

            }
            .background(Color(0xFF4DB6AC))
            .pointerInput(Unit) {
                detectDragGestures (
                ){ change, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y

                }


            }
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInParent()
                Log.i("PositinodedCurret","$position")
            }
    ) {
        Text(
            text = text,
            style = TextStyle(color = Color.Black, fontSize = 46.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
