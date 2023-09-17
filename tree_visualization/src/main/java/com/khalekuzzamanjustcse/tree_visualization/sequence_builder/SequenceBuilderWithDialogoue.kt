package com.khalekuzzamanjustcse.tree_visualization.sequence_builder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.tree_visualization.ui.common.PopupWithRadioButtons

fun searchSequence()= sequence {
    val list= listOf(10,20,30,40)
    yield(1)
}

@Preview
@Composable
fun SequenceBuilderDialogue() {
    var selectedOption by remember { mutableStateOf("Option 1") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PopupWithRadioButtons(
            text = "",
            isOpen = true,
            options = listOf("Option 1", "Option 2", "Option 3"),
            onOptionSelected = {
                selectedOption = it
            }
        )
        Text("Selected Option: $selectedOption")
    }
}
