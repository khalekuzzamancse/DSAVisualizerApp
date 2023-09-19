package com.khalekuzzamanjustcse.tree_visualization

import android.util.Log
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreeNode
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.LinkedList
import java.util.Queue

data class ChildPickerData(
    val parent: TreeNode<Int>,
    val child: List<TreeNode<Int>>
)

enum class TreeTraversalIntermediateDS {
    Stack, Queue
}

enum class BinaryTreeChildType {
    LEFT, RIGHT
}

data class TreeTraversalState(
    val processingNode: TreeNode<Int>?,
    val futureProcessingNodes: List<TreeNode<Int>?>,
    val newlyAddedNodes: List<TreeNode<Int>?>,
    val intermediateDS: TreeTraversalIntermediateDS = TreeTraversalIntermediateDS.Stack
)

fun bsfSequence(
    root: TreeNode<Int>?,
    onChildSelect: () -> BinaryTreeChildType = { BinaryTreeChildType.LEFT }
) = sequence {
    val intermediateDS = TreeTraversalIntermediateDS.Queue
    if (root == null)
        yield(true) //result
    val queue: Queue< TreeNode<Int>> = LinkedList()
    queue.add(root)
    yield(
        TreeTraversalState(
            processingNode = null, futureProcessingNodes = queue.toList(),
            newlyAddedNodes = listOf(root), intermediateDS = intermediateDS
        )
    ) //result
    while (queue.isNotEmpty()) {
        val node = queue.poll()
        //show the popup dialog which child to traverse 1st
        //so open  a dialog box so pause the execution until the next
        if (node != null && node.children.size > 1) {
            //if node has multiple children
            yield(
                ChildPickerData(parent = node, child = node.children)
            )
        }
        //resume the execution by calling iterator.next() in when pop up window dismiss
        //the result of chosen child will be received by the callback
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
    root:  TreeNode<Int>?,
) = sequence {
    if (root == null)
        yield(true) // Result
    val stack = mutableListOf< TreeNode<Int>?>()
    root?.let { stack.add(it) }
    yield(
        TreeTraversalState(
            processingNode = null,
            futureProcessingNodes = stack,
            listOf(root)
        )
    ) // Result
    while (stack.isNotEmpty()) {
        val node = stack.removeAt(stack.size - 1)
        //extra yeiding until he choose a child
        val children = node?.children ?: emptyList()
        children.reversed().forEach { child ->
            stack.add(child)
        }

        yield(TreeTraversalState(node, stack, children)) // Result
    }
}


fun inorderTraversal(root:  TreeNode<Int>?) = sequence {
    val stack = mutableListOf< TreeNode<Int>?>()
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

fun postorderTraversal(root:  TreeNode<Int>?) = sequence {
    val stack = mutableListOf< TreeNode<Int>?>()
    var currentNode = root
    var lastVisitedNode:  TreeNode<Int>? = null
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

fun preorderTraversal(root:  TreeNode<Int>?) = sequence {
    val stack = mutableListOf< TreeNode<Int>?>()
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






