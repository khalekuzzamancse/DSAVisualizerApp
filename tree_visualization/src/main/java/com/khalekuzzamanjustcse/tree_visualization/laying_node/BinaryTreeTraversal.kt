package com.khalekuzzamanjustcse.tree_visualization.laying_node

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.tree_visualization.BinaryTreeChildType
import com.khalekuzzamanjustcse.tree_visualization.ChildPickerData
import com.khalekuzzamanjustcse.tree_visualization.ui.common.MovableTreeNode
import com.khalekuzzamanjustcse.tree_visualization.TreeTraversalState
import com.khalekuzzamanjustcse.tree_visualization.bsfSequence
import com.khalekuzzamanjustcse.tree_visualization.dfsSequence
import com.khalekuzzamanjustcse.tree_visualization.inorderTraversal
import com.khalekuzzamanjustcse.tree_visualization.postorderTraversal
import com.khalekuzzamanjustcse.tree_visualization.preorderTraversal
import com.khalekuzzamanjustcse.tree_visualization.ui.common.PopupWithRadioButtons


/*

This layout algorithm(Charles Wetherell and Alfred Shannon) has a limitation so we have to use full binary tree.
is user want to a child become null then we replace the child with empty node value
and while showing UI we just ignore the value of empty node


 */

object TreeTraversalType {
    const val BFS = "BFS"
    const val DFS = "DFS"
    const val INORDER = "Inorder"
    const val PREORDER = "Preorder"
    const val POSTORDER = "Postorder"
}



fun resetTreeColor(node:  TreeNode<Int>?) {
    if (node == null) return
    resetTreeColor(node.children.firstOrNull())
    node.resetColor()
    node.children.drop(1).forEach { child ->
        resetTreeColor(child)
    }
}

fun getTree(sizePx: Float): TreeNode<Int> {
    var root:TreeNode<Int> = Node(value = 1, sizePx = sizePx)
    //level 2
    root.children.add(Node(value = 2, sizePx = sizePx))
    root.children.add(Node(value = 3, sizePx = sizePx))
    //level 3
    root.children[0].children.add(Node(value = 4, sizePx = sizePx))
    root.children[0].children.add(Node(value = 5, sizePx = sizePx))
    root.children[1].children.add(Node(value = 6, sizePx = sizePx))
    root.children[1].children.add(Node(value = 7, sizePx = sizePx))
    //level 4
    root.children[0].children[0].children.add(Node(value = 8, sizePx = sizePx))
    root.children[0].children[0].children.add(Node(value = 9, sizePx = sizePx))
    root.children[0].children[1].children.add(Node(value = 10, sizePx = sizePx))
    root.children[0].children[1].children.add(Node(value = 11, sizePx = sizePx))
    root.children[1].children[0].children.add(Node(value = 12, sizePx = sizePx))
    root.children[1].children[0].children.add(Node(value = 13, sizePx = sizePx))
    root.children[1].children[1].children.add(Node(value = 14, sizePx = sizePx))
    root.children[1].children[1].children.add(Node(value = 15, sizePx = sizePx))
    root = ShanonTreeNodeCoordinateCalculator(root).generate()
    return root
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TreeVisualizerPreview() {
    val size: Dp = 45.dp
    val sizePx = size.value * LocalDensity.current.density

    var openDialog by remember {
        mutableStateOf(false)
    }
    val root by remember {
        mutableStateOf(getTree(sizePx))
    }

    var selectedChild by
    remember {
        mutableStateOf(BinaryTreeChildType.RIGHT)
    }

//lambdas
    val onChildSelect: () -> BinaryTreeChildType = {
        selectedChild
    }
    var traversalIterator by
    remember {
        mutableStateOf(bsfSequence(root, onChildSelect).iterator())
    }

    val onNodeProcess: (TreeTraversalState) -> Unit = { currentState ->
        val node = currentState.processingNode
        if (node != null)
            node.color.value = Color.Blue
    }
    var availableChild by remember {
        mutableStateOf(listOf(""))
    }
    var dialogText by remember {
        mutableStateOf("")
    }

    val jumpNextStep: () -> Unit = {
        if (traversalIterator.hasNext()) {
            val state = traversalIterator.next()
            if (state is TreeTraversalState) {
                onNodeProcess(state)
            } else if (state is ChildPickerData) {
                state.parent.color.value = Color.Blue
                dialogText = "Processing :${state.parent.value}\n it has two children " +
                        "choose which is enqueued 1st"
                availableChild = state.child.map { "${it.value}" }
                openDialog = true
            }
        }
    }

//
    var selectedTraversal by remember {
        mutableStateOf(TreeTraversalType.BFS)
    }


    val onTraversalTypeChanged: (String) -> Unit = {
        resetTreeColor(root)
        selectedTraversal = it
        traversalIterator = when (it) {
            TreeTraversalType.INORDER -> inorderTraversal(root).iterator()
            TreeTraversalType.PREORDER -> preorderTraversal(root).iterator()
            TreeTraversalType.POSTORDER -> postorderTraversal(root).iterator()
            TreeTraversalType.BFS -> bsfSequence(root, onChildSelect).iterator()
            TreeTraversalType.DFS -> dfsSequence(root).iterator()
            else -> inorderTraversal(root).iterator()
        }

    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "TreeTraversal",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            //two different pop up dialogs causing problems on composition
//            MyDropdownMenu(
//                label = "Traversal Type",
//                options = listOf(
//                    TreeTraversalType.BFS,
//                    TreeTraversalType.DFS,
//                    TreeTraversalType.INORDER,
//                    TreeTraversalType.POSTORDER,
//                    TreeTraversalType.PREORDER
//                ),
//                onOptionSelected = onTraversalTypeChanged
//            )
            MyButton("Next") {
                jumpNextStep()
            }
            TreeVisualizer(root = root, size = size)
            PopupWithRadioButtons(
                text = dialogText,
                isOpen = openDialog,
                options = availableChild,
                onOptionSelected = {
                    selectedChild = if (it == availableChild.first())
                        BinaryTreeChildType.LEFT
                    else
                        BinaryTreeChildType.RIGHT
                    openDialog = false
                    jumpNextStep()
                }
            )
        }

    }


}


@Composable
fun TreeVisualizer(
    root:  TreeNode<Int>,
    size: Dp,
    onLongClick: ( TreeNode<Int>) -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(1000.dp)
            .horizontalScroll(rememberScrollState())
            .drawBehind {
                drawTreeLines(root)
            }
    ) {
        LayoutNode(
            node = root,
            size = size,
            onLongClick = onLongClick,
        )
    }

}


private fun DrawScope.drawTreeLines(
    node:  TreeNode<Int>?,
) {
    if (node == null)
        return
    node.children.forEach { child ->
        val parentCenter = node.centerOffset()
        val childCenter = child.centerOffset()
        //draw line between the node that are visible only
        val isNotNullChild=child.value != Node.NULL_NODE
        if (isNotNullChild){
            drawLine(
                color = Color.Black,
                start = Offset(parentCenter.x, parentCenter.y),
                end = Offset(childCenter.x, childCenter.y),
                strokeWidth = 2f
            )
        }

        drawTreeLines(child)
    }
}

@Composable
private fun LayoutNode(
    node:  TreeNode<Int>?,
    size: Dp,
    onLongClick: ( TreeNode<Int>) -> Unit,
) {
    if (node == null)
        return
    //On show the non null node in UI
    if (node.value != Node.NULL_NODE) {
        MovableTreeNode(
            size = size,
            label = "${node.value}",
            currentOffset = node.coordinates.value,
            color = node.color.value,
            onLongPress = { onLongClick(node) },
        )
    }

    node.children.forEach {
        LayoutNode(it, size, onLongClick)
    }

}

@Composable
private fun MyButton(
    label: String,
    onClick: () -> Unit = {}
) {
    Button(onClick = onClick) {
        Text(text = label)
    }
}

