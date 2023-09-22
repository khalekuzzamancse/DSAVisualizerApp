package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.DynamicElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
Making some invisible cell so that we can keep track at which offset to insert

 */
class StackState(
    val cellSize: Dp,
    val density: Density,
) {

    private val _stackElements: MutableState<List<DynamicElement>> = mutableStateOf(emptyList())
    val stackElement: List<DynamicElement>
        get() = _stackElements.value
    val stackTopPointerPosition: Offset
        get() = if (_stackElements.value.isNotEmpty()) _stackElements.value.last().bottomLeft else Offset.Infinite

    fun push(label: String) {
        val tempList = _stackElements.value.toMutableList()
        tempList.add(
            DynamicElement(
                label = label,
                _size = mutableStateOf(cellSize),
                density = density,
                isHideBorder = mutableStateOf(false),
                _draggable = mutableStateOf(false)
            )
        )
        _stackElements.value = tempList
        _stackElements.value.last().blinkBackground(2000)


    }

    fun pop() {
        val tempList = _stackElements.value.toMutableList()
        if (tempList.isNotEmpty()) {
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                _stackElements.value.last().blinkBackground(2000)
                delay(2000)
                tempList.removeLast()
                _stackElements.value = tempList
            }
        }

    }
}

