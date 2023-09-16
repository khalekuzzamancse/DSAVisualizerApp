package com.khalekuzzamanjustcse.dsavisulizer.graph_representation.wetherell_shannon_layout

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.dsavisulizer.SwappableElement


 data class ShanonTreeNode(
    val value: Int,
    var x: Float = 0f,
    var y: Float = 0f,
    val sizePx: Float,
    var coordinates: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val children: MutableList<ShanonTreeNode> = mutableListOf()
) {
    fun updateCoordinate() {
        coordinates.value = Offset(x * sizePx, y * sizePx)
    }
}

var maximumDepthOfTree = 4
var nexts = IntArray(maximumDepthOfTree)

fun minimumWs(tree: ShanonTreeNode, depth: Int = 0) {
    tree.x = nexts[depth].toFloat()
    tree.y = depth.toFloat()
    tree.updateCoordinate()
    nexts[depth]++
    for (c in tree.children) {
        minimumWs(c, depth + 1)
    }
}

private fun centerParent(root: ShanonTreeNode?) {
    if (root == null) return

    for (child in root.children) {
        centerParent(child)
    }
    if (root.children.size > 0) {
        val diff = root.children.last().x - root.children.first().x
        // println("Root:${root.value} ->: (${root.children.last().x}-${root.children.first().x}=$diff ->${diff/2f}")
        root.x = root.children.first().x + diff / 2f
        root.updateCoordinate()
    }

}



@Preview
@Composable
 fun LayoutShanonLayoutPreview() {
    val size = 45.dp
    val sizePx = size.value * LocalDensity.current.density

        val root =ShanonTreeNode(value = 1,sizePx=sizePx)
    //level 2
    root.children.add(ShanonTreeNode(value = 2,sizePx=sizePx))
    root.children.add(ShanonTreeNode(value = 3,sizePx=sizePx))
    //level 3
    root.children[0].children.add(ShanonTreeNode(value = 4,sizePx=sizePx))
    root.children[0].children.add(ShanonTreeNode(value = 5,sizePx=sizePx))
    root.children[1].children.add(ShanonTreeNode(value = 6,sizePx=sizePx))
    root.children[1].children.add(ShanonTreeNode(value = 7,sizePx=sizePx))
    //level 4
    root.children[0].children[0].children.add(ShanonTreeNode(value = 8,sizePx=sizePx))
    root.children[0].children[0].children.add(ShanonTreeNode(value = 9,sizePx=sizePx))
    root.children[0].children[1].children.add(ShanonTreeNode(value = 10,sizePx=sizePx))
    root.children[0].children[1].children.add(ShanonTreeNode(value = 11,sizePx=sizePx))
    root.children[1].children[0].children.add(ShanonTreeNode(value = 12,sizePx=sizePx))
    root.children[1].children[0].children.add(ShanonTreeNode(value = 13,sizePx=sizePx))
    root.children[1].children[1].children.add(ShanonTreeNode(value = 14,sizePx=sizePx))
    root.children[1].children[1].children.add(ShanonTreeNode(value = 15,sizePx=sizePx))

    minimumWs(root)
    centerParent(root)

    Box(modifier = Modifier
        .fillMaxSize()
        .size(1000.dp)
        .horizontalScroll(rememberScrollState())
    ) {
        WetherellShannonTreeLayout(node =root , size =size )
    }


}

@Composable
private fun WetherellShannonTreeLayout(
    node: ShanonTreeNode?,
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
        WetherellShannonTreeLayout(it, size)
    }

}

