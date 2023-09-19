package com.khalekuzzamanjustcse.tree_visualization.laying_node

abstract class TreeNodeCoordinateCalculator<T>(
    protected open val root: TreeNode<T>
) {
    abstract fun generate(): TreeNode<T>
}

class ShanonTreeNodeCoordinateCalculator<T>(
    override val root: TreeNode<T>,
    maximumDepthOfTree: Int = 20,
) : TreeNodeCoordinateCalculator<T>(root) {

    private var next = IntArray(maximumDepthOfTree)

    private fun minimumWs(tree: TreeNode<T>, depth: Int = 0) {
        tree.x = next[depth].toFloat()
        tree.y = depth.toFloat()
        tree.updateCoordinate()
        next[depth]++
        for (c in tree.children) {
            minimumWs(c, depth + 1)
        }
    }

    private fun centerParent(root: TreeNode<T>?) {
        if (root == null) return
        for (child in root.children) {
            centerParent(child)
        }
        if (root.children.size > 0) {
            val diff = root.children.last().x - root.children.first().x
            root.x = root.children.first().x + diff / 2f
            root.updateCoordinate()
        }

    }

    override fun generate(): TreeNode<T> {
        minimumWs(root)
        centerParent(root)
        return root
    }
}