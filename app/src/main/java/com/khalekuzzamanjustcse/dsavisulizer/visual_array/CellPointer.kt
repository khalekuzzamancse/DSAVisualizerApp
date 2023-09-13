package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset

sealed class PointerName {
    object NULL : PointerName()

}
sealed class SelectionSortPointerName : PointerName() {
    object I : SelectionSortPointerName()
    object J : SelectionSortPointerName()
    object MinIndex : SelectionSortPointerName()
}



data class Pointers(
    val pointerList: List<CellPointer>,
) {
    companion object {
        fun emptyPointers(): Pointers = Pointers(emptyList())
    }

    fun updatePosition(name: PointerName, position: Offset) {
        pointerList.forEach { pointer ->
            if (pointer.name == name)
                pointer.position.value = position
        }
    }


    fun getPositionOf(name: PointerName): Offset {
        pointerList.forEach {
            if (it.name == name) return it.position.value
        }
        return Offset.Infinite
    }
}

data class CellPointer(
    val position: MutableState<Offset> = mutableStateOf(INVALID_POSITION),
    val label: String,
    val name: PointerName,
    val icon: ImageVector
) {
    companion object {
        val INVALID_POSITION = Offset.Infinite
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun CellPointerPreview() {

    val pointers by remember {
        mutableStateOf(
            Pointers(
                listOf(
                    CellPointer(
                        position = mutableStateOf(Offset.Zero),
                        label = "i",
                        icon = Icons.Default.ArrowForward,
                        name = SelectionSortPointerName.I
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset.Zero),
                        label = "j",
                        icon = Icons.Default.PlayArrow,
                        name = SelectionSortPointerName.J
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset.Zero),
                        label = "min",
                        icon = Icons.Default.ArrowBack,
                        name =SelectionSortPointerName.MinIndex
                    )
                )
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            Button(onClick = {
                pointers.updatePosition(
                    SelectionSortPointerName.I, pointers
                        .getPositionOf(SelectionSortPointerName.I) + Offset(50f, 0f)
                )
            }) {
                Text(text = "Move1")
            }
            Button(onClick = {
                pointers.updatePosition(
                    SelectionSortPointerName.J, pointers
                        .getPositionOf(SelectionSortPointerName.J) + Offset(50f, 0f)
                )
            }) {
                Text(text = "Move2")
            }
            Button(onClick = {
                pointers.updatePosition(
                    SelectionSortPointerName.MinIndex, pointers
                        .getPositionOf(SelectionSortPointerName.MinIndex) + Offset(50f, 0f)
                )
            }) {
                Text(text = "Move3")
            }

        }

        pointers.pointerList.forEach {
            CellPointerComposable(
                currentPosition = it.position.value,
                label = it.label,
                icon = it.icon
            )
        }

    }

}

@Composable
fun CellPointerComposable(
    label: String,
    icon: ImageVector = Icons.Default.KeyboardArrowUp,
    currentPosition: Offset = Offset.Zero,
) {
    val offsetAnimation by animateOffsetAsState(currentPosition, label = "")
    Column(
        modifier = Modifier
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Text(
            text = label,
            modifier = Modifier
        )
    }
}

