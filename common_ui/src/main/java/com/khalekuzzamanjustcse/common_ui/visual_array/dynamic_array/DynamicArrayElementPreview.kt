package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun DynamicArrayElementPreview() {
    val size = 64.dp
    val density = LocalDensity.current
    val element1 = remember {
        DynamicElement(
            label = "10",
            _size = mutableStateOf(size),
            density = density,
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
            MyButton(label = "BlinkBG") { element1.blinkBackground() }
            MyButton(label = "StopBlinkBG") { element1.stopBlinkBackground() }
            MyButton(label = "Show/HideBorder") { if (element1.shouldShowBorder) element1.hideBorder() else element1.showBorder() }
            MyButton(label = "ChangeRectColor") { element1.changeBoundingRectColor(Color.Yellow) }
        }

        VisualElementComposable(
            label = element1.label,
            size = element1.size,
            offset = element1.topLeft,
            color = element1.color,
            clickable = element1.clickable,
            onDragStart = element1::onDragStart,
            onDrag = element1::onDrag,
            onDragEnd = element1::onDragEnd,
            draggable = element1.draggable,
            onClick = element1::onClick,
        )

        VisualElementComposable(element1)
    }

}


@Composable
fun VisualElementComposable(
    label: String,
    size: Dp,
    offset: Offset,
    color: Color,
    clickable: Boolean,
    onDragStart: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onClick: () -> Unit,
    draggable: Boolean,
    onDrag: (Offset) -> Unit,


    ) {
    val offsetAnimation by animateOffsetAsState(offset, label = "")
    val colorAnimation by animateColorAsState(targetValue = color, label = "")
    val padding = 8.dp

    val modifier = Modifier
        .size(size)
        .offset {
            IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
        }
        .then(
            if (draggable) {
                Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = onDragStart,
                        onDrag = { change, dragAmount ->
                            onDrag(dragAmount)
                            change.consume()
                        },
                        onDragEnd = onDragEnd
                    )
                }
            } else {
                Modifier
            }
        )
        .then(
            if (clickable) {
                Modifier.clickable { onClick() }
            } else {
                Modifier
            }
        )


    Box(modifier = modifier) {

        val textColor = if (color.luminance() > 0.5) Color.Black else Color.White
        Text(
            text = label,
            color = textColor,
            modifier = Modifier
                .padding(padding)
                .clip(CircleShape)
                .background(colorAnimation)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun VisualElementComposable(
    element: DynamicElement
) {
    val offsetAnimation by animateOffsetAsState(element.topLeft, label = "")
    val colorAnimation by animateColorAsState(targetValue = element.color, label = "")
    val padding = 8.dp
//    val borderColor = if (element.color.luminance() > 0.5) Color.Black else Color.White

    val modifier = Modifier
        .size(element.size)
        .offset {
            IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
        }
        .then(
            if (element.draggable) {
                Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = element::onDragStart,
                        onDrag = { change, dragAmount ->
                            element.onDrag(dragAmount)
                            change.consume()
                        },
                        onDragEnd = element::onDragEnd
                    )
                }
            } else {
                Modifier
            }
        )
        .then(
            if (element.clickable) {
                Modifier.clickable { element.onClick() }
            } else {
                Modifier
            }
        )


    Box(
        modifier = modifier
            .background(element.backgroundColor)
            .then(
                if (element.shouldShowBorder)
                    Modifier
                        .border(width = 1.dp, color = Color.Black)
                else
                    Modifier
            )
            .onGloballyPositioned { position ->
            element.onPositionChanged(position.positionInParent())
        }
    ) {

        val textColor = if (element.color.luminance() > 0.5) Color.Black else Color.White
        Text(
            text = element.label,
            color = textColor,
            modifier = Modifier
                .padding(padding)
                .clip(CircleShape)
                .background(colorAnimation)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}