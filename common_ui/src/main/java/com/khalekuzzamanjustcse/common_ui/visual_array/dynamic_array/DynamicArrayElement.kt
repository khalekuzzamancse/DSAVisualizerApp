package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


data class DynamicArrayElement(
    val label: String,
    val size: Dp,
    val sizePx: Float,
    private val _offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    private val _color: MutableState<Color> = mutableStateOf(Color.Red),
    private val _clickable: MutableState<Boolean> = mutableStateOf(false),
    private val _draggable: MutableState<Boolean> = mutableStateOf(true)
) {

    private var blinkingJob: Job? = null
    private var isBlinking by mutableStateOf(false)
    private var originalColor by mutableStateOf(Color.Red)
    val currentOffset: Offset
        get() = _offset.value
    val color: Color
        get() = _color.value
    val clickable: Boolean
        get() = _clickable.value
    val draggable: Boolean
        get() = _draggable.value


    fun enableDrag() {
        _draggable.value = true
    }

    fun enableClick() {
        _clickable.value = true
    }

    fun disableDrag() {
        _draggable.value = false
    }

    fun disableClick() {
        _clickable.value = false
    }

    fun onDrag(dragAmount: Offset) {
        if (_draggable.value) {
            _offset.value += dragAmount
        }
    }

    fun onDragStart(offset: Offset) {

    }

    fun onDragEnd() {

    }

    fun changeColor(color: Color) {
        this._color.value = color
    }

    fun moveAt(offset: Offset) {
        _offset.value = offset
    }

    fun resetColor() {
        _color.value = Color.Red
    }

    fun resetOffset() {
        _offset.value = Offset.Zero
    }

    fun moveAtInfinite() {
        _offset.value = Offset.Infinite
    }

    fun onClick() {

    }

    fun blink() {
        if (!isBlinking) {
            isBlinking = true
            originalColor = _color.value
            blinkingJob = CoroutineScope(Dispatchers.Default).launch {
                while (isBlinking) {
                    _color.value = randomColor()
                    delay(500)
                }
            }
        }
    }

    fun stopBlink() {
        isBlinking = false
        blinkingJob?.cancel()
        _color.value = originalColor // Restore the original color
    }

    private fun randomColor(): Color {
        val red = Random.nextFloat()
        val green = Random.nextFloat()
        val blue = Random.nextFloat()
        return Color(red, green, blue)
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
    element: DynamicArrayElement
) {
    val offsetAnimation by animateOffsetAsState(element.currentOffset, label = "")
    val colorAnimation by animateColorAsState(targetValue = element.color, label = "")
    val padding = 8.dp

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


    Box(modifier = modifier) {

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