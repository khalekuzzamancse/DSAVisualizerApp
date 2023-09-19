package com.khalekuzzamanjustcse.tree_visualization.tree_input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.khalekuzzamanjustcse.tree_visualization.Tree
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreeVisualizer

@Preview
@Composable
fun InputTreePreview() {
    val size = 64.dp
    val sizePx = size.value * LocalDensity.current.density
    val tree by remember {
        mutableStateOf(Tree(Node(value = 1, sizePx = sizePx)))
    }
    TreeInput(tree,size)


}

@Composable
fun TreeInput(
    tree: Tree<Int>,
    nodeSize: Dp,
) {
    val sizePx = nodeSize.value * LocalDensity.current.density
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
            size = nodeSize
        ) {
            clickedNode = it
            showInput = true
        }
        TreeChildInput(
            takeInput = showInput,
            onDismiss = { showInput = false }
        ) {
            showInput = false
            it.forEach { value ->
                tree.addChild(clickedNode, (Node(value = value, sizePx = sizePx)))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeChildInput(
    takeInput: Boolean,
    onDismiss: () -> Unit = {},
    onInputDone: (List<Int>) -> Unit,
) {
    var children by remember {
        mutableStateOf("")
    }

    if (takeInput) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = children,
                        onValueChange = { children = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    TextButton(
                        onClick = {
                            val elements = children.split("[,\\s]+".toRegex())
                            val list = elements.mapNotNull { it.toIntOrNull() }
                            onInputDone(list)
                            children = ""
                        }) {
                        Text(text = "Done")
                    }
                }
            }


        }
    }

}



