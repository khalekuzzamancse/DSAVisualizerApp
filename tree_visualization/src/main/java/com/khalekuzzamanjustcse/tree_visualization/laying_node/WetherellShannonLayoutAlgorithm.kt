package com.khalekuzzamanjustcse.tree_visualization.laying_node

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.tree_visualization.SwappableElement

/*

This layout algorithm(Charles Wetherell and Alfred Shannon) has a limitation so we have to use full binary tree.
is user want to a child become null then we replace the child with empty node value
and while showing UI we just ignore the value of empty node

 */
data class Node(
    val value: Int,
    val sizePx: Float,
    var x: Float = 0f,
    var y: Float = 0f,
    var coordinates: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val children: MutableList<Node> = mutableListOf()
) {
//    companion object {
//        const val NULL_NODE = -111111
//    }

    fun updateCoordinate() {
        coordinates.value = Offset(x * sizePx, y * sizePx)
    }

    fun getCenterOffset() = coordinates.value + Offset(sizePx / 2, sizePx / 2)

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



@Preview
@Composable
fun TreePreview() {
    val size = 45.dp
    val sizePx = size.value * LocalDensity.current.density

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .size(1000.dp)
            .horizontalScroll(rememberScrollState())
            .drawBehind {
                drawTreeLines(root)
            }
    ) {
        LayoutNode(node = root, size = size)
    }

}


private fun DrawScope.drawTreeLines(node: Node?) {
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
) {
    if (node == null)
        return
    SwappableElement(
        size = size,
        label = "${node.value}",
        currentOffset = node.coordinates.value
    )
    node.children.forEach {
        LayoutNode(it, size)
    }

}

