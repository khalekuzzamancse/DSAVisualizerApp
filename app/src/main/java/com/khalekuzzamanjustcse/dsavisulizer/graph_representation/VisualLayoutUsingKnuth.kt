package com.khalekuzzamanjustcse.dsavisulizer.graph_representation

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


data class VisualTreeNode(
    var value: Int,
    var x: Int = 0,
    var y: Int = 0,
    val sizePx: Float,
    var leftChild: VisualTreeNode? = null,
    var rightChild: VisualTreeNode? = null,
    val coordinates: MutableState<Offset> = mutableStateOf(Offset(x * sizePx, y * sizePx)),
) {
    fun updateCoordinates() {
        coordinates.value = Offset(x * sizePx, y * sizePx)
    }
}

private var i = 0

private fun calculateCoordinate(node: VisualTreeNode?, depth: Int) {
    if (node == null) {
        return
    }
    calculateCoordinate(node.leftChild, depth + 1)
    node.x = i++
    node.y = depth
    node.updateCoordinates()
    calculateCoordinate(node.rightChild, depth + 1)
}

@Preview
@Composable
 fun LayoutTreeKnuthPreview() {
    val size = 45.dp
    val sizePx = size.value * LocalDensity.current.density
    val root = VisualTreeNode(value = 1, sizePx = sizePx)
    root.leftChild = VisualTreeNode(value = 2, sizePx = sizePx)
    root.rightChild = VisualTreeNode(value = 3, sizePx = sizePx)
//level 2
    root.leftChild?.leftChild = VisualTreeNode(value = 4, sizePx = sizePx)
    root.leftChild?.rightChild =  VisualTreeNode(value = 5, sizePx = sizePx)
    root.rightChild?.leftChild =  VisualTreeNode(value = 6, sizePx = sizePx)
    root.rightChild?.rightChild = VisualTreeNode(value = 7, sizePx = sizePx)
    //level 3
    root.leftChild?.leftChild?.leftChild = VisualTreeNode(value = 8, sizePx = sizePx)
    root.leftChild?.leftChild?.rightChild = VisualTreeNode(value = 9, sizePx = sizePx)
    root.leftChild?.rightChild?.leftChild = VisualTreeNode(value = 10, sizePx = sizePx)
    root.leftChild?.rightChild?.rightChild = VisualTreeNode(value = 11, sizePx = sizePx)
    root.rightChild?.leftChild?.leftChild =VisualTreeNode(value = 12, sizePx = sizePx)
    root.rightChild?.leftChild?.rightChild = VisualTreeNode(value = 13, sizePx = sizePx)
    root.rightChild?.rightChild?.leftChild =VisualTreeNode(value = 14, sizePx = sizePx)
    root.rightChild?.rightChild?.rightChild =VisualTreeNode(value = 15, sizePx = sizePx)




    calculateCoordinate(root, 0)

    Box(modifier = Modifier
        .fillMaxSize()
        .size(1000.dp)
        .horizontalScroll(rememberScrollState())
    ) {
        LayoutTreeKnuth(root, size)
    }


}

@Composable
private fun LayoutTreeKnuth(
    node: VisualTreeNode?,
    size: Dp,
) {
    if (node == null)
        return
    SwappableElement(
        size = size,
        label = "${node.value}",
        currentOffset = node.coordinates.value
    )
    LayoutTreeKnuth(node.leftChild, size)
    LayoutTreeKnuth(node.rightChild, size)


}