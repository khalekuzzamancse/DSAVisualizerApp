package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun VisualArrayCellComposablePreview() {
    val size = 64.dp
    val density = LocalDensity.current
    val cell1 = remember {
        DynamicArrayCell(size = size, density = density)
    }
    val cell2 = remember {
        DynamicArrayCell(size = size, density = density)
    }
//iioii
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            MyButton(label = "Move(200,200)") { cell1.moveAt(Offset(200f, 200f)) }
            MyButton(label = "ChangeColor") { cell1.changeColor(Color.Blue) }
            MyButton(label = "ResetColor") { cell1.resetColor() }
            MyButton(label = "ResetOffset") { cell1.resetOffset() }
            MyButton(label = "MoveToInfinity") { cell1.moveAtInfinite() }
            MyButton(label = "Blink") { cell1.blink() }
            MyButton(label = "StopBlink") { cell1.stopBlink() }
            MyButton(label = "Show/HideBorder") {
                if (cell1.shouldShowBorder)
                    cell1.hideBorder()
                else
                    cell1.showBorder()
            }
        }

        FlowRow {
            VisualArrayCellComposable(cell = cell1)
            VisualArrayCellComposable(cell = cell2)
        }
    }

}

@Composable
fun VisualArrayCellComposable(
    cell: DynamicArrayCell
) {
    val offsetAnimation by animateOffsetAsState(cell.topLeft, label = "")
    val colorAnimation by animateColorAsState(targetValue = cell.color, label = "")
    val modifier = Modifier
        .offset {
            IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
        }

    val borderColor = if (cell.color.luminance() > 0.5) Color.White else Color.Black
    Box(
        modifier = modifier
            .size(cell.size)
            .then(
                if (cell.shouldShowBorder)
                    Modifier.border(width = 1.dp, color = borderColor)
                else
                    Modifier
            )
            .background(colorAnimation)
            .onGloballyPositioned { position ->
                cell.onPositionChanged(position.positionInParent())
            }
    )
}

@Composable
fun MyButton(
    label: String,
    enabled: Boolean = true,
    icon:ImageVector?=null,
    onClick: () -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = onClick
    ) {
        if(icon != null){
            Icon(icon, null)
        }
        Text(text = label)
    }

}