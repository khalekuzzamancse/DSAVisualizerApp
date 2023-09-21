package com.khalekuzzamanjustcse.graph_visualization

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.khalekuzzamanjustcse.graph_visualization.graph_input.GraphNodeComposable

@Preview
@Composable
fun PopupWithRadioButtonsPreview() {

    var selectedOption by remember { mutableStateOf("Option 1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PopupWithRadioButtons(
            text = "Select",
            isOpen = true,
            options = listOf("1", "2", "3"),
            onOptionSelected = {
                selectedOption = it
            }
        )
        Text("Selected Option: $selectedOption")
    }
}

@Composable
fun PopupWithRadioButtons(
    text: String,
    isOpen: Boolean,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    var selectedOption by remember { mutableStateOf(options.first()) }
    if (isOpen) {
        Dialog(
            onDismissRequest = { }
        ) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = text,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    options.forEach { option ->
                        GraphNodeComposable(
                            label = option,
                            size = 64.dp,
                            currentOffset = offset,
                            onDrag = {offset+=it  }
                        )
                    }
                }
            }
        }
    }
}
