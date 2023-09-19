package com.khalekuzzamanjustcse.tree_visualization.laying_node

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.khalekuzzamanjustcse.tree_visualization.ui.common.MyDropdownMenu
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

data class Node(
    val value: Int,
    val sizePx: Float,
    var x: Float = 0f,
    var y: Float = 0f,
    val children: MutableList<Node> = mutableListOf(),
    val coordinates: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val color: MutableState<Color> = mutableStateOf(Color.Red)
) {
    companion object {
        const val NULL_NODE = -111111
    }

    fun updateCoordinate() {
        coordinates.value = Offset(x * sizePx, y * sizePx)
    }

    fun getCenterOffset() = coordinates.value + Offset(sizePx / 2, sizePx / 2)
    fun resetColor() {
        color.value = Color.Red
    }

    override fun toString(): String {
        return "$value:(${children.map { it.value }} )"

    }

}

class CoordinateGenerator(
    private val root: Node,
    maximumDepthOfTree: Int = 20,

    ) {

    private var next = IntArray(maximumDepthOfTree)

    private fun minimumWs(tree: Node, depth: Int = 0) {
        tree.x = next[depth].toFloat()
        tree.y = depth.toFloat()
        tree.updateCoordinate()
        next[depth]++
        for (c in tree.children) {
            minimumWs(c, depth + 1)
        }
    }

    private fun centerParent(root: Node?) {
        if (root == null) return
        for (child in root.children) {
            centerParent(child)
        }
        if (root.children.size > 0) {
            val diff = root.children.last().x - root.children.first().x
            root.x = root.children.first().x + diff / 2f
            root.updateCoordinate()
        }

    }

    fun generateCoordinate(): Node {
        minimumWs(root)
        centerParent(root)
        return root
    }


}


fun resetTreeColor(node: Node?) {
    if (node == null) return
    resetTreeColor(node.children.firstOrNull())
    node.resetColor()
    node.children.drop(1).forEach { child ->
        resetTreeColor(child)
    }
}

fun getTree(sizePx: Float): Node {
    var root = Node(value = 1, sizePx = sizePx)
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
    root = CoordinateGenerator(root).generateCoordinate()
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
    root: Node,
    size: Dp,
    onLongClick: (Node) -> Unit = {}
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
    node: Node?,
) {
    if (node == null)
        return
    node.children.forEach { child ->
        val parentCenter = node.getCenterOffset()
        val childCenter = child.getCenterOffset()
        drawLine(
            color = Color.Black,
            start = Offset(parentCenter.x, parentCenter.y),
            end = Offset(childCenter.x, childCenter.y),
            strokeWidth = 2f
        )
        drawTreeLines(child)
    }
}

@Composable
private fun LayoutNode(
    node: Node?,
    size: Dp,
    onLongClick: (Node) -> Unit,
) {
    if (node == null)
        return
    MovableTreeNode(
        size = size,
        label = "${node.value}",
        currentOffset = node.coordinates.value,
        color = node.color.value,
        onLongPress = {onLongClick(node)},
    )
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

