package com.khalekuzzamanjustcse.common_ui.graph_editor

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Preview
@Composable
fun NodeDataInputPreview() {
    var takeInput by remember {
        mutableStateOf(false)
    }
    Column (modifier=Modifier.fillMaxSize()){
        Button(onClick = {
            takeInput=true
        }) {
            Text(text = "Take Input")
        }
        NodeDataInput(takeInput){
            takeInput=false
            Log.i("NodeDataInput", it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeDataInput(
    isOpen: Boolean = false,
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
                    TextField(
                        value = text,
                        onValueChange = { text = it },
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
