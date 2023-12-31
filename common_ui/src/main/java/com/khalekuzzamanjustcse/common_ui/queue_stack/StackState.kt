package com.khalekuzzamanjustcse.common_ui.queue_stack

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.runtime.MutableState
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


class StackViewModel(
    val density: Density,
) : ViewModel() {
    val state = StackState(cellSize = 64.dp, density = density)
    val topAppbarData =
        TopAppbarData(
            title = "Stack Screen ",
            elevation = 8.dp,
            navigationIcon = null,
            actions = listOf(
                object : IconComponent(label = "Push", icon = Icons.Filled.ArrowDownward) {
                    override fun onClick() {
                        state.push("33")
                    }

                },
                object : IconComponent(label = "Pop", icon = Icons.Filled.ArrowOutward) {
                    override fun onClick() {
                        state.pop()
                    }

                },
            )
        )
}


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
     //   tempList.add(
//            DynamicElement(
//                label = label,
//                _size = mutableStateOf(cellSize),
//                density = density,
//                isHideBorder = mutableStateOf(false),
//                _draggable = mutableStateOf(false)
//            )
     //   )
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

