package com.khalekuzzamanjustcse.graph_visualization

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable
import com.khalekuzzamanjustcse.graph_visualization.swap_able_array.ArrayComposable
import com.khalekuzzamanjustcse.graph_visualization.swap_able_array.ArrayComposableState

@Preview
@Composable
fun PopupWithRadioButtonsPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PopupWithRadioButtons(
            isOpen = true,
        )

    }
}

@Composable
fun PopupWithRadioButtons(
    isOpen: Boolean,
) {

    val cellSize = 64.dp
    val cellSizePx = cellSize.value * LocalDensity.current.density
    val list = listOf(1, 2, 3, 4, 5)
    val state = remember {
        ArrayComposableState(list = list, cellSizePx = cellSizePx)
    }
    if (isOpen) {
        Dialog(
            onDismissRequest = { }
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Button(onClick = {
                        Log.i("CurrentArrayState", "${state.cellsCurrentElements}")
                    }) {
                        Text(text = "State")
                    }
                    ArrayComposable(
                        modifier=Modifier,
                        cellSize = cellSize,
                        onCellPositionChanged = state::onCellPositionChanged,
                        state = state,
                        onDragEnd = state::onDragEnd,
                        onDragStart = state::onDragStart
                    )
                }
            }
        }
    }
}
