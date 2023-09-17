package com.khalekuzzamanjustcse.tree_visualization.ui.common

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
            text="Select",
            isOpen = true,
            options = listOf("Option 1", "Option 2", "Option 3"),
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    selectedOption=option
                                    onOptionSelected(option)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = option == selectedOption,
                                onClick = { onOptionSelected(option) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.Black, // Customize selected color
                                    unselectedColor = Color.Gray // Customize unselected color
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option)
                        }
                    }
                }
            }
        }
    }
}
