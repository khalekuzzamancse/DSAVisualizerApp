package com.khalekuzzamanjustcse.common_ui.graph_editor


/*
Instead of using flow or other observables types we used manually obverse design pattern.
because this the simple case.
we when a item is dragged nodes list will not change though we have to update the edge.
I in this moment i don't know about flow such as where to use and when it use.
since this is simple case we implement manually observer pattern.

we try to follow the single source of truth that means directly form outside the other class
should not modify this class data,but since there is a bug for removing node,and this  bug is may be
in the compose framework ,so that is why to give the node remove(undo) option we
give a setter method for the node.

We want that class has single responsibility that is why we directly do not
provide any undo and redo options inside this class.
the client will decide he will undo or redo of a operation
 */



class GraphEditorGraph(
) : GraphEditor {
    override var onEdgeUpdated: (Set<Edge>) -> Unit = {}
    override var onNodesUpdated: (Set<GraphBasicNode>) -> Unit = {}
    override var nodes = setOf<GraphBasicNode>()
        private set
    override var edges = setOf<Edge>()
        private set

    override fun addNode(node: GraphBasicNode) {
        nodes = nodes + node
        onNodesUpdated(nodes)
    }

    override fun resetGraph(nodes: Set<GraphBasicNode>) {
        this.nodes = nodes
        onNodesUpdated(this.nodes)
        edges = edges.filter { edge ->
            this.nodes.any { it.id != edge.startNodeId } || this.nodes.any { it.id != edge.endNodeId }
        }.toSet()
        onEdgeUpdated(edges)
    }

    override fun addEdge(u: GraphBasicNode, v: GraphBasicNode) {
        edges = edges + Edge(u.id, v.id)
        onEdgeUpdated(edges)
    }

    override fun updateExistingNode(node: GraphBasicNode) {
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

    override fun makeHomogenous() {
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

    override fun removeEdge(u: GraphBasicNode, v: GraphBasicNode) {
        edges = edges - Edge(u.id, v.id)
        edges = edges - Edge(v.id, u.id)
        onEdgeUpdated(edges)
    }

}