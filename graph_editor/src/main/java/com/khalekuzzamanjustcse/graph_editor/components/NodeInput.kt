package com.khalekuzzamanjustcse.graph_editor.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeDataInput(
    isOpen: Boolean = false,
    message:String="",
    onInputComplete: (String) -> Unit,
) {

    var text by remember {
        mutableStateOf("")
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
                    Text(text = message)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        singleLine = true
                    )
                    TextButton(onClick = {
                        onInputComplete(text)
                        text=""
                    }) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}
