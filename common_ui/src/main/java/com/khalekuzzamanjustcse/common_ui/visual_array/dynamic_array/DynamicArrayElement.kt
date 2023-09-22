package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import android.util.Range
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
import androidx.compose.ui.unit.Density
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
    private val _size: MutableState<Dp>,
    val density: Density,
    private val _offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    private val _color: MutableState<Color> = mutableStateOf(Color.Red),
    private val _clickable: MutableState<Boolean> = mutableStateOf(false),
    private val _draggable: MutableState<Boolean> = mutableStateOf(true),

    ) {

    val size: Dp
        get() = _size.value
    val topLeft: Offset
        get() = _offset.value
    val center: Offset
        get() = _offset.value + Offset(sizePx / 2, sizePx / 2)
    private val bottomRight: Offset
        get() = _offset.value + Offset(sizePx, sizePx)
    val boundingRectangle:BoundingRectangle
    get() = BoundingRectangle(topLeft,bottomRight)

    private val sizePx: Float
        get() = _size.value.value * density.density



    private var blinkingJob: Job? = null
    private var isBlinking by mutableStateOf(false)
    private var originalColor by mutableStateOf(Color.Red)

    val color: Color
        get() = _color.value
    val clickable: Boolean
        get() = _clickable.value
    val draggable: Boolean
        get() = _draggable.value
    private var _dragStartFrom: Offset = _offset.value
    val dragStartFrom: Offset
        get() = _dragStartFrom


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
        _dragStartFrom = offset
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

