package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.StateFlow


interface GraphEditor {
    val nodes: Set<GraphBasicNode>
    val edges: Set<Edge>
    fun addNode(node: GraphBasicNode)
    fun resetGraph(nodes: Set<GraphBasicNode>)
    fun addEdge(u: GraphBasicNode, v: GraphBasicNode)
    fun updateExistingNode(node: GraphBasicNode)
    fun makeHomogenous()
    fun removeEdge(u: GraphBasicNode, v: GraphBasicNode)
    var onEdgeUpdated: (Set<Edge>) -> Unit
    var onNodesUpdated: (Set<GraphBasicNode>) -> Unit
}
interface GraphEditorCanvas {
    val visualEdges: StateFlow<List<DrawingEdge>>
    val visualNodes: StateFlow<Set<GraphEditorVisualNode>>

    val disableUndo: Boolean
    val disableRedo: Boolean

    fun undo()
    fun redo()

    fun onCanvasTap(offset: Offset)
    fun onNodeInputRequest()
    fun onInputComplete(label: String, size: Dp, sizePx: Float)
    fun addEdge()
    fun makeAllNodeSameSize()
    fun onInputComplete()
    val onInputDone: (GraphEditorResult) -> Unit
    // Add any additional methods or properties needed for callbacks here
}
abstract class GraphEditorResult(
    open val nodes: List<GraphBasicNode> = emptyList(),
    open val edges: List<Edge> = emptyList(),
    open val isDirected: Boolean = false
) {

    abstract val canvasHeightPx: Float
    abstract val adjacencyList: Map<Int, List<Int>>

    abstract val isTree: Boolean
    abstract val isBinaryTree: Boolean
}

abstract class AbstractGraphAnalyzer(private val adjacencyList: Map<Int, List<Int>>) {

    fun isTree(): Boolean {
        val visited = mutableSetOf<Int>()
        val startNode = adjacencyList.keys.firstOrNull() ?: return false
        if (!isConnected(adjacencyList, startNode, -1, visited)) {
            return false
        }

        visited.clear()
        if (hasCycle(adjacencyList, startNode, -1, visited)) {
            return false
        }
        for (node in adjacencyList.keys) {
            if (node !in visited) {
                return false
            }
        }

        return true
    }

    private fun isConnected(
        adjacencyList: Map<Int, List<Int>>,
        node: Int,
        parent: Int,
        visited: MutableSet<Int>
    ): Boolean {
        visited.add(node)
        val neighbors = adjacencyList[node] ?: emptyList()
        for (neighbor in neighbors) {
            if (neighbor != parent) {
                if (neighbor !in visited) {
                    isConnected(adjacencyList, neighbor, node, visited)
                }
            }
        }
        return visited.size == adjacencyList.keys.size
    }

    private fun hasCycle(
        adjacencyList: Map<Int, List<Int>>,
        node: Int,
        parent: Int,
        visited: MutableSet<Int>
    ): Boolean {
        visited.add(node)
        val neighbors = adjacencyList[node] ?: emptyList()
        for (neighbor in neighbors) {
            if (neighbor != parent) {
                if (neighbor in visited || hasCycle(adjacencyList, neighbor, node, visited)) {
                    return true
                }
            }
        }
        return false
    }

    fun isBinaryTree(): Boolean {
        val visited = mutableSetOf<Int>()
        val startNode = adjacencyList.keys.firstOrNull() ?: return false
        if (!isConnected(adjacencyList, startNode, -1, visited)) {
            return false
        }
        visited.clear()
        if (!isBinaryTreeProperties(adjacencyList, startNode, -1, visited)) {
            return false
        }

        return true
    }

    private fun isBinaryTreeProperties(
        adjacencyList: Map<Int, List<Int>>,
        node: Int,
        parent: Int,
        visited: MutableSet<Int>
    ): Boolean {
        visited.add(node)
        val neighbors = adjacencyList[node] ?: emptyList()
        var childCount = 0

        for (neighbor in neighbors) {
            if (neighbor != parent) {
                if (neighbor in visited) {
                    return false
                }

                if (!isBinaryTreeProperties(adjacencyList, neighbor, node, visited)) {
                    return false
                }

                childCount++
                if (childCount > 2) {
                    return false
                }
            }
        }

        return true
    }
}

