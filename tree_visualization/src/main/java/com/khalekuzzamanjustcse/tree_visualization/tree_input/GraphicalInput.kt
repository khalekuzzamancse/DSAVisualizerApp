package com.khalekuzzamanjustcse.tree_visualization.tree_input

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.khalekuzzamanjustcse.tree_visualization.Tree
import com.khalekuzzamanjustcse.tree_visualization.laying_node.ShanonTreeNodeCoordinateCalculator
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreeVisualizer


@Preview
@Composable
fun TreeInput() {
    val size = 64.dp
    val sizePx = size.value * LocalDensity.current.density
    val tree by remember {
        mutableStateOf(Tree(Node(value = 1, sizePx = sizePx)))
    }
    tree.createTree()
    var showInput by remember {
        mutableStateOf(false)
    }

    var clickedNode by remember {
        mutableStateOf(tree.getRoot())
    }

    Column {
        TreeVisualizer(
            root = tree.getRoot(),
            size = size
        ) {
            clickedNode = it
            showInput = true
            Log.i("NodePressed", "${it.value}")
        }
        TreeChildInput(takeInput = showInput) {
            showInput = false
            it.forEach { value ->
                clickedNode.addChild((Node(value = value, sizePx = sizePx)))
                tree.rePosition()
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeChildInput(
    takeInput: Boolean,
    onInputDone: (List<Int>) -> Unit,
) {
    var children by remember {
        mutableStateOf("")
    }
    PopupWindow(isOpen = takeInput) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                value = children,
                onValueChange = { children = it },
                label = { },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    val elements = children.split("[,\\s]+".toRegex())
                    val list = elements.mapNotNull { it.toIntOrNull() }
                    onInputDone(list)
                }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        }

    }
}

@Composable
fun PopupWindow(
    isOpen: Boolean,
    content: @Composable () -> Unit,
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = { }
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                content()
            }
        }
    }
}

