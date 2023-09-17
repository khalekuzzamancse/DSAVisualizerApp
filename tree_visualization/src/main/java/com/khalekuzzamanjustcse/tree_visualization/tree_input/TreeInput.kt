package com.khalekuzzamanjustcse.tree_visualization.tree_input

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TreeInputPreview() {
    MultipleTreeNodeInputs()
}

@Composable
fun MultipleTreeNodeInputs() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TreeNodeInput(
            onNodeSubmitted = { left, node, right ->
                Log.i("Submitted", "$node,$left,$right")
            }
        )

    }
}




@Composable
fun TreeNodeInput(
    onNodeSubmitted: (String, String, String) -> Unit
) {
    var nodeValue by remember { mutableStateOf("") }
    var leftChildNodeText by remember { mutableStateOf("") }
    var rightChildNodeText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NodeInputField(value = leftChildNodeText, hints = "") {
            leftChildNodeText = it
        }
        NodeInputField(value = nodeValue, hints = "") {
            nodeValue = it
        }
        NodeInputField(value = rightChildNodeText, hints = "") {
            rightChildNodeText = it
        }


        IconButton(
            onClick = {
                onNodeSubmitted(leftChildNodeText, nodeValue, rightChildNodeText)
                nodeValue=""
                leftChildNodeText=""
                rightChildNodeText=""
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Done, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.NodeInputField(
    value: String,
    hints: String,
    onInputChanged: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onInputChanged,
        placeholder = { Text(hints) },
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}