package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class DynamicArrayCell(
    val size: Dp,
    val density: Density,
    private val _offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    private val _color: MutableState<Color> = mutableStateOf(Color.Unspecified),
    var presentElementIndexRef: Int? = null,
    private val isHideBorder: MutableState<Boolean> = mutableStateOf(false)
) {


    val _currentPosition: MutableState<Offset> = mutableStateOf(Offset.Zero)
    val currentPosition: Offset
        get() = _currentPosition.value

    val topLeft: Offset
        get() = _offset.value
    val center: Offset
        get() = _offset.value + Offset(sizePx / 2, sizePx / 2)
    private val bottomRight: Offset
        get() = _offset.value + Offset(sizePx, sizePx)
    val boundingRectangle: BoundingRectangle
        get() = BoundingRectangle(topLeft, bottomRight)

    private val sizePx: Float
        get() = size.value * density.density
    val shouldShowBorder: Boolean
        get() = !isHideBorder.value


    private var blinkingJob: Job? = null
    private var isBlinking by mutableStateOf(false)
    private var originalColor by mutableStateOf(_color.value)
    private var movingJob: Job? = null

    val color: Color
        get() = _color.value


    fun changeColor(color: Color) {
        this._color.value = color
    }

    fun hideBorder() {
        isHideBorder.value = true
    }

    fun showBorder() {
        isHideBorder.value = false
    }

    fun moveAt(offset: Offset) {
        _offset.value = offset
    }

    fun resetColor() {
        _color.value = Color.Unspecified
    }

    fun resetOffset() {
        _offset.value = Offset.Zero
    }

    fun onPositionChanged(offset: Offset) {

        _currentPosition.value = offset

    }

    fun moveAtInfinite() {
        movingJob = CoroutineScope(Dispatchers.Default).launch {
            _offset.value = Offset(2000.toFloat(), 2000.toFloat())
            delay(500)
            _offset.value = Offset.Infinite
            movingJob?.cancel()
        }
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

