package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun DynamicArrayElementPreview() {
    val size = 64.dp
    val sizePx = size.value * LocalDensity.current.density
    val element1 = remember {
        DynamicArrayElement(
            label = "10",
            size = 64.dp,
            sizePx = sizePx
        )
    }

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            MyButton(label = "Move(200,200)") { element1.moveAt(Offset(200f, 200f)) }
            MyButton(label = "ChangeColor") { element1.changeColor(Color.Blue) }
            MyButton(label = "ResetColor") { element1.resetColor() }
            MyButton(label = "ResetOffset") { element1.resetOffset() }
            MyButton(label = "MoveToInfinity") { element1.moveAtInfinite() }
            MyButton(label = "FlipClickable") { if (element1.clickable) element1.disableClick() else element1.enableClick() }
            MyButton(label = "FlipDraggable") { if (element1.draggable) element1.disableDrag() else element1.enableDrag() }
            MyButton(label = "Blink") { element1.blink() }
            MyButton(label = "StopBlink") { element1.stopBlink() }
        }

        VisualElementComposable(
            label = element1.label,
            size = element1.size,
            offset = element1.currentOffset,
            color = element1.color,
            clickable = element1.clickable,
            onDragStart = element1::onDragStart,
            onDrag = element1::onDrag,
            onDragEnd = element1::onDragEnd,
            draggable = element1.draggable,
            onClick = element1::onClick
        )

        VisualElementComposable(element1)
    }

}

@Composable
private fun MyButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text(text = label)
    }

}