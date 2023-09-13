package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

data class MultiplePointer(
    val list: List<CellPointer>,
) {
    fun updatePosition(pointerIndex: Int, position: Offset) {
        if (pointerIndex >= 0 && pointerIndex < list.size) {
            list[pointerIndex].position.value = position
        }
    }

    fun getPositionOf(pointerIndex: Int): Offset {
        return if (pointerIndex >= 0 && pointerIndex < list.size)
            list[pointerIndex].position.value
        else
            Offset.Zero
    }
}

data class CellPointer(
    val position: MutableState<Offset> = mutableStateOf(INVALID_POSITION),
    val label: String = " ",
    val icon: ImageVector = Icons.Default.KeyboardArrowUp
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
            MultiplePointer(
                listOf(
                    CellPointer(
                        position = mutableStateOf(Offset.Zero),
                        label = "i",
                        icon = Icons.Default.ArrowForward
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset.Zero),
                        label = "j",
                        icon = Icons.Default.PlayArrow
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset.Zero),
                        label = "min",
                        icon = Icons.Default.ArrowBack
                    )
                )
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            Button(onClick = {
                pointers.updatePosition(
                    0, pointers
                        .getPositionOf(0) + Offset(50f, 0f)
                )
            }) {
                Text(text = "Move1")
            }
            Button(onClick = {
                pointers.updatePosition(
                    1, pointers
                        .getPositionOf(1) + Offset(50f, 0f)
                )
            }) {
                Text(text = "Move2")
            }
            Button(onClick = {
                pointers.updatePosition(
                    2, pointers
                        .getPositionOf(2) + Offset(50f, 0f)
                )
            }) {
                Text(text = "Move3")
            }

        }

        pointers.list.forEach {
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
    size: Dp = 100.dp,
    icon: ImageVector = Icons.Default.KeyboardArrowUp,
    currentPosition: Offset = Offset.Zero,
) {
    val offsetAnimation by animateOffsetAsState(currentPosition, label = "")
    Column(
        modifier = Modifier
            .size(size)
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
                .wrapContentSize(Alignment.Center)
        )
    }
}

