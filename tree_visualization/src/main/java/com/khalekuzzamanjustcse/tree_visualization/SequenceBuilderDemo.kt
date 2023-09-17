package com.khalekuzzamanjustcse.tree_visualization

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node

@Preview
@Composable
private fun BFSTraversalPreview() {
    val size: Dp = 45.dp
    val sizePx = size.value * LocalDensity.current.density
    val root = Node(value = 1, sizePx = sizePx)
    //level 2
    root.children.add(Node(value = 2, sizePx = sizePx))
    root.children.add(Node(value = 3, sizePx = sizePx))
    //level 3
    root.children[0].children.add(Node(value = 4, sizePx = sizePx))
    root.children[0].children.add(Node(value = 5, sizePx = sizePx))
    root.children[1].children.add(Node(value = 6, sizePx = sizePx))
    root.children[1].children.add(Node(value = 7, sizePx = sizePx))

    //
    //
    var i by remember {
        mutableStateOf(0)
    }
    var selectedChild by remember {
        mutableStateOf(BinaryTreeChildType.LEFT)
    }
    val onChildSelect: () -> BinaryTreeChildType = {
        selectedChild
    }
    val bsfIterator by remember {
        mutableStateOf(
            bsfSequence(
                root,
                onChildSelect
            ).iterator()
        )
    }

    Column {
        Button(onClick = {
            selectedChild = if (i % 2 == 0)
                BinaryTreeChildType.RIGHT
            else
                BinaryTreeChildType.LEFT
            if (bsfIterator.hasNext())
                Log.i("BFSTRAVERSALNext", "${bsfIterator.next()}")
            i++
        }) {
            Text("Next")
        }
    }

}