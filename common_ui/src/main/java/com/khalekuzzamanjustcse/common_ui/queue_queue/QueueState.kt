package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwipeLeft
import androidx.compose.material.icons.filled.SwipeRight
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.common_ui.IconComponent
import com.khalekuzzamanjustcse.common_ui.appbar.TopAppbarData
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.DynamicElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class QueueViewModel(
    val density: Density,
) : ViewModel() {
    val queue = QueueState(cellSize = 64.dp, density = density)
    private val _topAppbarData by mutableStateOf(
        TopAppbarData(
            title = "Queue Screen",
            elevation = 8.dp,
            navigationIcon = null,
            actions = listOf(
                object : IconComponent(label = "Enqueue", icon = Icons.Filled.SwipeLeft) {
                    override fun onClick() {
                        queue.enqueue("11")
                    }
                },
                object : IconComponent(label = "Dequeue", icon = Icons.Filled.SwipeRight) {
                    override fun onClick() {
                        queue.dequeue()
                    }
                }
            )
        )
    )
    val topAppbarData
        get() = _topAppbarData


}

class QueueState(
    val cellSize: Dp,
    val density: Density,
) {

    private val _elements: MutableState<List<DynamicElement>> =
        mutableStateOf(emptyList())
    val element: List<DynamicElement>
        get() = _elements.value


    fun enqueue(label: String) {
        val tempList = _elements.value.toMutableList()
        tempList.add(
            DynamicElement(
                label = label,
                _size = mutableStateOf(cellSize),
                density = density,
                isHideBorder = mutableStateOf(false),
                _draggable = mutableStateOf(false)
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
        val tempList = _elements.value.map { it }.toMutableList()
        if (tempList.isNotEmpty()) {
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                _elements.value.first().blinkBackground(2000)
                delay(2000)
                tempList.removeFirst()
                _elements.value = tempList
            }
        }

    }


}