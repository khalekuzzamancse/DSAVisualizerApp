package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
// Import the necessary packages

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ArrayInputPreview() {
    ArrayInput{
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrayInput(
    modifier: Modifier =Modifier,
    onInputCompleted: (List<Int>) -> Unit,
) {
    val showDialog = remember { mutableStateOf(true) }
    var text by remember {
        mutableStateOf("50 40 30 20 10")
    }

    if (showDialog.value) {
        AlertDialog(
            modifier=modifier,
            onDismissRequest = {
//                showDialog.value = false
            },
            title = {
                Text("Enter the array")
            },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val elements = text.split("[,\\s]+".toRegex())
                        val list = elements.mapNotNull { it.toIntOrNull() }
                        onInputCompleted(list)
                        showDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

}