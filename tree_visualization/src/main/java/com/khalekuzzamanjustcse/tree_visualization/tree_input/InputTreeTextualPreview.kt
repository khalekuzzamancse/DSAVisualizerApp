package com.khalekuzzamanjustcse.tree_visualization.tree_input

import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node

data class BinaryTreeInputNode(
    val node:Int,
    val leftChild:Int=Node.NULL_NODE,
    val rightChild:Int=Node.NULL_NODE
)
fun buildTreeFromList(nodeList: List<BinaryTreeInputNode>): Node? {
    val nodeMap = mutableMapOf<Int, Node>()
    for (inputNode in nodeList) {
        val node = Node(inputNode.node, sizePx = 0f) // Set sizePx to the appropriate value
        nodeMap[inputNode.node] = node
    }

    for (inputNode in nodeList) {
        val parentNode = nodeMap[inputNode.node]
        val leftChildNode = if (inputNode.leftChild != Node.NULL_NODE) nodeMap[inputNode.leftChild] else null
        val rightChildNode = if (inputNode.rightChild != Node.NULL_NODE) nodeMap[inputNode.rightChild] else null

        parentNode?.children?.apply {
            leftChildNode?.let { add(it) }
            rightChildNode?.let { add(it) }
        }
    }

    // Find the root node
    val root = nodeMap.values.firstOrNull { it.value == nodeList.first().node }

    return root
}
fun printInorder(root: Node?) {
    if (root == null) return

    println(root)
    root.children.forEach{
        printInorder(it)
    }
}

fun main() {
    val nodeList = listOf(
        BinaryTreeInputNode(1, 2, 3),  // Node 1 with left child 2 and right child 3
        BinaryTreeInputNode(2, 4, 5),  // Node 2 with left child 4 and right child 5
        BinaryTreeInputNode(3, 6, Node.NULL_NODE),  // Node 3 with left child 6 and no right child
        BinaryTreeInputNode(4, Node.NULL_NODE, Node.NULL_NODE),  // Node 4 with no left or right child
        BinaryTreeInputNode(5, Node.NULL_NODE, Node.NULL_NODE),  // Node 5 with no left or right child
        BinaryTreeInputNode(6, Node.NULL_NODE, Node.NULL_NODE)  // Node 6 with no left or right child
    )
    val root = buildTreeFromList(nodeList)
    printInorder(root)

}