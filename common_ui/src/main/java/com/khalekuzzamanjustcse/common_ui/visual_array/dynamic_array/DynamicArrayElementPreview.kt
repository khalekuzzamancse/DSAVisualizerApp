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
    val state by remember {
        mutableStateOf(
            DynamicArrayElement(
                label = "10",
                size = 64.dp,
                sizePx = sizePx
            )
        )
    }

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            MyButton(label = "Move(200,200)") { state.moveAt(Offset(200f, 200f)) }
            MyButton(label = "ChangeColor") { state.changeColor(Color.Blue) }
            MyButton(label = "ResetColor") { state.resetColor() }
            MyButton(label = "ResetOffset") { state.resetOffset() }
            MyButton(label = "MoveToInfinity") { state.moveAtInfinite() }
            MyButton(label = "FlipClickable") { if (state.clickable) state.disableClick() else state.enableClick() }
            MyButton(label = "FlipDraggable") { if (state.draggable) state.disableDrag() else state.enableDrag() }
        }

        DynamicArrayElementComposable(
            label = state.label,
            size = state.size,
            offset = state.currentOffset,
            color = state.color,
            clickable = state.clickable,
            onDragStart = state::onDragStart,
            onDrag = state::onDrag,
            onDragEnd = state::onDragEnd,
            draggable = state.draggable,
            onClick = state::onClick
        )
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