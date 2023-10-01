package com.khalekuzzamanjustcse.common_ui.graph_editor

data class GraphEditorResultImp(
    override val nodes: List<GraphBasicNode> = emptyList(),
    override val edges: List<Edge> = emptyList(),
    override val isDirected: Boolean = false,
) : GraphEditorResult() {

    override val canvasHeightPx: Float
        get() {
            val maxSizeNode = nodes.maxBy { it.size }
            return (nodes.maxBy { it.position.y }.position.y) + maxSizeNode.sizePx
        }
    override val adjacencyList: Map<Int, List<Int>>
        get() {
            val adjacency = mutableMapOf<Int, List<Int>>()
            nodes.forEach {
                adjacency[it.id] = emptyList()
            }
            edges.forEach { (u, v) ->
                val neighborsOfU = adjacency[u] ?: emptyList()
                adjacency[u] = neighborsOfU + v
                if (!isDirected) {
                    val neighborsOfV = adjacency[v] ?: emptyList()
                    adjacency[v] = neighborsOfV + u
                }
            }
            return adjacency
        }

    override val isTree: Boolean
        get() = GraphAnalyzer(adjacencyList).isTree()
    override val isBinaryTree: Boolean
        get() = GraphAnalyzer(adjacencyList).isBinaryTree()
}


class GraphAnalyzer(
    private val adjacencyList: Map<Int, List<Int>>
)  {
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

