package com.khalekuzzamanjustcse.dsavisulizer.graph_representation.knuth_layout

class TreeNode(
    var value: Int, var x: Int = 0, var y: Int = 0,
    var leftChild: TreeNode? = null, var rightChild: TreeNode? = null
)

private var i = 0
private fun traverseAndAssignCoordinates(node: TreeNode?, depth: Int) {
    if (node == null) {
        return
    }
    traverseAndAssignCoordinates(node.leftChild, depth + 1)
    node.x = i++
    node.y = depth
    traverseAndAssignCoordinates(node.rightChild, depth + 1)
}

fun main() {
    val root = TreeNode(1)
    root.leftChild = TreeNode(2)
    root.rightChild = TreeNode(3)

//    root.leftChild?.leftChild = TreeNode(4)
//    root.leftChild?.rightChild = TreeNode(5)
//    root.rightChild?.leftChild = TreeNode(6)
//    root.rightChild?.rightChild = TreeNode(7)
//
//    root.leftChild?.leftChild?.leftChild = TreeNode(8)
//    root.leftChild?.leftChild?.rightChild = TreeNode(9)
//    root.leftChild?.rightChild?.leftChild = TreeNode(10)
//    root.leftChild?.rightChild?.rightChild = TreeNode(11)
//    root.rightChild?.leftChild?.leftChild = TreeNode(12)
//    root.rightChild?.leftChild?.rightChild = TreeNode(13)
//    root.rightChild?.rightChild?.leftChild = TreeNode(14)
//    root.rightChild?.rightChild?.rightChild = TreeNode(15)
    traverseAndAssignCoordinates(root, 0)
    printCoordinates(root)
}

private fun printCoordinates(node: TreeNode?) {
    if (node == null) {
        return
    }
    println("Node ${node.value}: (x=${node.x}, y=${node.y})")
    printCoordinates(node.leftChild)
    printCoordinates(node.rightChild)
}
