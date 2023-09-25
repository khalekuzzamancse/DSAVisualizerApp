package com.khalekuzzamanjustcse.graph_visualization.ui_layer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.khalekuzzamanjustcse.common_ui.visual_array.static_array.ArrayComposable
import com.khalekuzzamanjustcse.common_ui.visual_array.static_array.ArrayComposableState

@Preview
@Composable
fun PopupWithRadioButtonsPreview() {
    val cellSize = 64.dp
    val cellSizePx = cellSize.value * LocalDensity.current.density

    val state = remember {
        ArrayComposableState(list = listOf(1, 2, 3, 4, 5), cellSizePx = cellSizePx)
    }
//    PopupWithRadioButtons(
//        state = state,
//        isOpen = true,
//    ) {
//        Log.i("OnListReOrdered", "${state.cellsCurrentElements.filterNotNull()}")
//    }

}

@Composable
fun <T> PopupWithRadioButtons(
    state: ArrayComposableState<T>,
    isOpen: Boolean,
    onListReOrdered: () -> Unit,
) {
    val cellSize = 64.dp

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
                  ArrayComposable(
                        modifier = Modifier,
                        cellSize = cellSize,
                        onCellPositionChanged = state::onCellPositionChanged,
                        state = state,
                        onDragEnd = state::onDragEnd,
                        onDragStart = state::onDragStart
                    )
                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick =onListReOrdered) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}
