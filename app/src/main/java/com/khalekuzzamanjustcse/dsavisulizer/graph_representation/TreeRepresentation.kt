package com.khalekuzzamanjustcse.dsavisulizer.graph_representation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@Composable
fun TreeRepresentationPreview() {
    val binaryTree = TreeNode(
        value = 1,
        left = TreeNode(2, TreeNode(4), TreeNode(5)),
        right = TreeNode(3, TreeNode(6), TreeNode(7))
    )
    BinaryTreeVisualizer(binaryTree)

}
data class TreeNode(val value: Int, val left: TreeNode? = null, val right: TreeNode? = null)

@Composable
fun BinaryTreeVisualizer(root: TreeNode) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        val coroutineScope = rememberCoroutineScope()
        val density = LocalDensity.current.density
        val treeWidth = (getWidth(root) * 100 * density).dp
        val treeHeight = (getHeight(root) * 100 * density).dp

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            onDraw = {
                coroutineScope.launch {
                    drawTree(root, treeWidth.toPx(), treeHeight.toPx())
                }
            }
        )
    }
}

fun getWidth(node: TreeNode?): Int {
    if (node == null) return 0
    return 1 + maxOf(getWidth(node.left), getWidth(node.right))
}

fun getHeight(node: TreeNode?): Int {
    if (node == null) return 0
    return 1 + maxOf(getHeight(node.left), getHeight(node.right))
}

suspend fun DrawScope.drawTree(node: TreeNode?, width: Float, height: Float, x: Float = width / 2, y: Float = 50f) {
    if (node != null) {
        val radius = 20f
        val offsetY = 100f
        val spacerX = width / (getWidth(node) + 1)
        val spacerY = height / (getHeight(node) + 1)

        drawCircle(Color.Blue, radius, Offset(x, y))

        node.left?.let {
            val childX = x - spacerX
            val childY = y + offsetY
            drawLine(Color.Black, start = Offset(x, y + radius), end = Offset(childX, childY - radius))
            drawTree(it, width, height, childX, childY)
        }

        node.right?.let {
            val childX = x + spacerX
            val childY = y + offsetY
            drawLine(Color.Black, start = Offset(x, y + radius), end = Offset(childX, childY - radius))
            drawTree(it, width, height, childX, childY)
        }
    }
}

