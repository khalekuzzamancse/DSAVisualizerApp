package com.khalekuzzamanjustcse.tree_visualization

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import com.khalekuzzamanjustcse.tree_visualization.ui.common.PopupWithRadioButtons

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
    val tree = """
    1
  2      3
4   5  6   7
"""

    var selectedChild by remember {
        mutableStateOf(BinaryTreeChildType.LEFT)
    }
    var openDialog by remember { mutableStateOf(false) }
    var availableChild by remember {
        mutableStateOf(listOf(""))
    }
    val onChildSelect: () -> BinaryTreeChildType = {
        selectedChild
    }


    var traversal by remember { mutableStateOf("") }

    val bsfIterator by remember {
        mutableStateOf(
            bsfSequence(
                root,
                onChildSelect
            ).iterator()
        )
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Text(
            text = tree,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = traversal,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = {
            if (bsfIterator.hasNext()) {
                val res = bsfIterator.next()
                Log.i("ShowDialougeFor:process","${res}")
                if(res is TreeTraversalState){
                    traversal += "${res.processingNode?.value} ,"
                }

                if (res is ChildPickerData) {
                    Log.i("ShowDialougeFor","${res.parent}")
                    availableChild = res.child.map{ "${it.value}" }
                    openDialog = true
                }
            }

        }) {
            Text("Next")
        }
        PopupWithRadioButtons(
            text="SElct",
            isOpen = openDialog,
            options = availableChild,
            onOptionSelected = {
                selectedChild = if (it == availableChild.first())
                    BinaryTreeChildType.LEFT
                else
                    BinaryTreeChildType.RIGHT
                openDialog = false
                if(bsfIterator.hasNext()){
                   val res=bsfIterator.next()
                    if(res is TreeTraversalState){
                        traversal += "${res.processingNode?.value} ,"
                    }
                }

            }
        )
    }

}