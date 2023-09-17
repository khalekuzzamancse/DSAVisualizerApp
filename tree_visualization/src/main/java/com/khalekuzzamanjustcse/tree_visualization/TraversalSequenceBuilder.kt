package com.khalekuzzamanjustcse.tree_visualization

import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import java.util.LinkedList
import java.util.Queue

enum class TreeTraversalIntermediateDS {
    Stack, Queue
}

enum class BinaryTreeChildType {
    LEFT, RIGHT
}

data class TreeTraversalState(
    val processingNode: Node?,
    val futureProcessingNodes: List<Node?>,
    val newlyAddedNodes: List<Node?>,
    val intermediateDS: TreeTraversalIntermediateDS = TreeTraversalIntermediateDS.Stack
)

fun bsfSequence(
    root: Node?,
    onChildSelect: () -> BinaryTreeChildType = { BinaryTreeChildType.LEFT }
) = sequence {
    val intermediateDS = TreeTraversalIntermediateDS.Queue

    if (root == null)
        yield(true) //result
    val queue: Queue<Node> = LinkedList()
    queue.add(root)
    yield(
        TreeTraversalState(
            processingNode = null, futureProcessingNodes = queue.toList(),
            newlyAddedNodes = listOf(root), intermediateDS = intermediateDS
        )

    ) //result
    while (queue.isNotEmpty()) {
        val node = queue.poll()
        val selected = onChildSelect()
        val children = node?.children ?: emptyList()

        if (selected == BinaryTreeChildType.LEFT)
            children.forEach { child ->
                queue.add(child)
            }
        else {
            children.reversed().forEach { child ->
                queue.add(child)
            }
        }

        yield(
            TreeTraversalState(
                processingNode = node, futureProcessingNodes = queue.toList(),
                newlyAddedNodes = children, intermediateDS = intermediateDS
            )
        ) //result
    }
}


fun dfsSequence(
    root: Node?,
    onChildSelect: () -> BinaryTreeChildType = { BinaryTreeChildType.LEFT }
) = sequence {
    if (root == null)
        yield(true) // Result
    val stack = mutableListOf<Node?>()
    root?.let { stack.add(it) }
    yield(TreeTraversalState(processingNode = null, futureProcessingNodes = stack, listOf(root))) // Result
    while (stack.isNotEmpty()) {
        val node = stack.removeAt(stack.size - 1)
        val selected = onChildSelect()
        val children = node?.children ?: emptyList()

        if (selected == BinaryTreeChildType.LEFT)
            children.reversed().forEach { child ->
                stack.add(child)
            }
        else {
            children.forEach { child ->
                stack.add(child)
            }
        }
        yield(TreeTraversalState(node, stack, children)) // Result
    }
}


fun inorderTraversal(root: Node?) = sequence {
    val stack = mutableListOf<Node?>()
    var currentNode = root

    while (currentNode != null || stack.isNotEmpty()) {
        while (currentNode != null) {
            stack.add(currentNode)
            currentNode = currentNode.children.firstOrNull()
        }
        currentNode = stack.removeAt(stack.size - 1)
        yield(TreeTraversalState(currentNode, stack.toList(), emptyList()))
        if (currentNode != null) {
            currentNode = currentNode.children.lastOrNull()
        }
    }
}

fun postorderTraversal(root: Node?) = sequence {
    val stack = mutableListOf<Node?>()
    var currentNode = root
    var lastVisitedNode: Node? = null
    while (currentNode != null) {
        while (currentNode != null) {
            stack.add(currentNode)
            currentNode = currentNode.children.firstOrNull()
        }

        while (currentNode == null && stack.isNotEmpty()) {
            val peekNode = stack.last()
            if (peekNode?.children?.lastOrNull() == null || peekNode.children.lastOrNull() == lastVisitedNode) {
                yield(TreeTraversalState(peekNode, stack.toList(), emptyList()))
                lastVisitedNode = stack.removeAt(stack.size - 1)
            } else {
                currentNode = peekNode.children.lastOrNull()
            }
        }
    }
}

fun preorderTraversal(root: Node?) = sequence {
    val stack = mutableListOf<Node?>()
    var currentNode = root
    while (currentNode != null || stack.isNotEmpty()) {
        while (currentNode != null) {
            yield(TreeTraversalState(currentNode, stack.toList(), emptyList()))
            stack.add(currentNode)
            currentNode = currentNode.children.firstOrNull()
        }
        val peekNode = stack.removeAt(stack.size - 1)
        currentNode = peekNode?.children?.lastOrNull()
    }
}






