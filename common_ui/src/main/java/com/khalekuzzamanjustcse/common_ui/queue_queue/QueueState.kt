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

class QueueState(
    val cellSize: Dp,
    val density: Density,
) {

    private val _elements: MutableState<List<DynamicElement>> = mutableStateOf(emptyList())
    val element: List<DynamicElement>
        get() = _elements.value

    fun enqueue(label: String) {
        val tempList = _elements.value.toMutableList()
        tempList.add(
            DynamicElement(
                label = label,
                _size = mutableStateOf(cellSize),
                density = density,
                isHideBorder = mutableStateOf(false)
            )
        )
        _elements.value = tempList
        _elements.value.last().blinkBackground(2000)
    }

    val rearPointerPosition: Offset
        get() = if (_elements.value.isNotEmpty()) _elements.value.last().bottomLeft else Offset.Infinite
    val frontPointerPosition: Offset
        get() = if (_elements.value.isNotEmpty()) _elements.value.first().bottomLeft else Offset.Infinite

    fun dequeue() {
        //to showing that each element is shifting left
        //shift all the elements and delete last element
        val tempList = _elements.value.toMutableList()
        if (tempList.isNotEmpty()) {
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                _elements.value.first().blinkBackground(2000)
                delay(2000)
                for (i in 1 until tempList.size) {
                    tempList[i - 1] = tempList[i]
                }
                tempList.removeLast()
                _elements.value = tempList
            }
        }

    }

}