package com.khalekuzzamanjustcse.common_ui.graph_editor


class GraphEditorGraphHolder(
    private val onEdgeUpdated: (Set<Edge>) -> Unit = {},
    private val onNodesUpdated: (Set<GraphBasicNode>) -> Unit = {}
) {
    private var nodes = setOf<GraphBasicNode>()
    private var edges = setOf<Edge>()
    fun addNode(node: GraphBasicNode) {
        nodes = nodes + node
        onNodesUpdated(nodes)
    }

    fun addEdge(u: GraphBasicNode, v: GraphBasicNode) {
        edges = edges + Edge(u.id, v.id)
        onEdgeUpdated(edges)
    }
    fun updateExistingNode(node: GraphBasicNode) {
        nodes = nodes.map {
            if (it.id == node.id)
                object : GraphBasicNode {
                    override val id = it.id
                    override val label = it.label
                    override val position = node.position
                    override val sizePx = it.sizePx
                    override val size = it.size
                }
            else it
        }.toSet()
        onNodesUpdated(nodes)
        onEdgeUpdated(edges)
    }

    fun makeHomogenous() {
        val maxSizeNode = nodes.maxBy { it.size }
        nodes = nodes.map {
            object : GraphBasicNode {
                override val id = it.id
                override val label = it.label
                override val position = it.position
                override val sizePx = maxSizeNode.sizePx
                override val size = maxSizeNode.size
            }

        }.toSet()
        onNodesUpdated(nodes)
        onEdgeUpdated(edges)
    }


}
