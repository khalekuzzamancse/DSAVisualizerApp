package com.khalekuzzamanjustcse.tree_visualization.visualizer

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreeNode
import com.khalekuzzamanjustcse.tree_visualization.ui.common.Menu
import com.khalekuzzamanjustcse.tree_visualization.ui.common.MovableTreeNode

object TreeTraversalType {
    const val BFS = "BFS"
    const val DFS = "DFS"
    const val INORDER = "Inorder"
    const val PREORDER = "Preorder"
    const val POSTORDER = "Postorder"
}

@Composable
fun TreeVisualizer(
    modifier: Modifier=Modifier,
    root: TreeNode<Int>,
    size: Dp,
    onLongClick: (TreeNode<Int>) -> Unit = {}
) {

    Box(
        modifier = modifier
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


fun DrawScope.drawTreeLines(
    node: TreeNode<Int>?,
) {
    if (node == null)
        return
    node.children.forEach { child ->
        val parentCenter = node.centerOffset()
        val childCenter = child.centerOffset()
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
fun LayoutNode(
    node: TreeNode<Int>?,
    size: Dp,
    onLongClick: (TreeNode<Int>) -> Unit,
) {
    if (node == null)
        return
    MovableTreeNode(
        size = size,
        label = "${node.value}",
        currentOffset = node.coordinates.value,
        color = node.color.value,
        onLongPress = { onLongClick(node) },
    )


    node.children.forEach {
        LayoutNode(it, size, onLongClick)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraversalScreenTopAppbar(
    title: String,
    isOnInputMode: Boolean = true,
    onInputComplete: () -> Unit,
    onNextClick: () -> Unit,
    childSelectionModeOn: Boolean = false,
    onChildSelectModeChange: () -> Unit,
    onTraversalSelected: (String) -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
        ),
        title = {
            Text(
                text = if (isOnInputMode) "Input Tree" else "$title Traversal",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        actions = {
            if (isOnInputMode) {
                IconButton(onClick = onInputComplete) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = null
                    )
                }
            } else {
                IconButton(onClick = onNextClick) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = null
                    )

                }
                Menu(
                    menuItems = listOf(
                        TreeTraversalType.BFS,
                        TreeTraversalType.DFS,
                        TreeTraversalType.INORDER,
                        TreeTraversalType.POSTORDER,
                        TreeTraversalType.PREORDER
                    ), onMenuItemClick = onTraversalSelected
                )
                IconButton(onClick = onChildSelectModeChange) {
                    Icon(
                        imageVector = if (childSelectionModeOn) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                        contentDescription = null
                    )

                }
            }


        }
    )
}

