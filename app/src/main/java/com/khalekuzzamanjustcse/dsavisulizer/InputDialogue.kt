package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PageInputPreview() {
    PageInput{cellNo,list->
        Log.i("PageInputPreview","$cellNo\n$list")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageInput(
    modifier: Modifier = Modifier,
    onInputCompleted: (Int,List<Int>) -> Unit,
) {
    val showDialog = remember { mutableStateOf(true) }
    var text by remember {
        mutableStateOf("7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1")
    }
    var cellNo by remember { mutableStateOf("3")}

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
                Column {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    )
                    TextField(
                        value = cellNo,
                        onValueChange = { cellNo = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    )
                }

            },
            confirmButton = {
                Button(
                    onClick = {
                        val elements = text.split("[,\\s]+".toRegex())
                        val list = elements.mapNotNull { it.toIntOrNull() }
                        onInputCompleted(cellNo.toInt(),list)
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