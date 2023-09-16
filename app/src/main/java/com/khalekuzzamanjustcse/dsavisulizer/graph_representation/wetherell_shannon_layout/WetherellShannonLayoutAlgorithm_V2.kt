package com.khalekuzzamanjustcse.dsavisulizer.graph_representation.wetherell_shannon_layout

/*

This code not may not correct because it takes much space later try
read the documentation and original python code then try to use the right code
 */
class TreeNode(
    val value: Int,
    var x: Int = -1,
    var y: Int = 0,
    val children: MutableList<TreeNode> = mutableListOf()
)


//class DrawTree(
//    val tree: TreeNode,
//    val depth: Int = 0,
//    val children: List<DrawTree> = tree.children.map { DrawTree(it, depth + 1) }
//)


fun layout(tree: TreeNode): TreeNode {
    setup(tree)
    addMods(tree, 0)
    return tree
}


fun setup(tree: TreeNode, depth: Int = 0, next: MutableMap<Int, Int> = HashMap(), offset: MutableMap<Int, Int> = HashMap()) {
    if (!next.containsKey(depth)) next[depth] = 0
    if (!offset.containsKey(depth)) offset[depth] = 0


    for (c in tree.children) {
        setup(c, depth + 1, next, offset)
    }


    tree.y = depth


    if (tree.children.isEmpty()) {
        val place = next[depth]!!
        tree.x = place
    } else if (tree.children.size == 1) {
        val place = tree.children[0].x - 1
        tree.x = place
    } else {
        val s = (tree.children[0].x + tree.children[1].x).toFloat()
        val place = (s / 2).toInt()
        offset[depth] = maxOf(offset[depth]!!, next[depth]!! - place)
        tree.x = place + offset[depth]!!
    }


    next[depth] = next[depth]!! + 2
}


fun addMods(tree: TreeNode, modSum: Int) {
    tree.x += modSum


    for (t in tree.children) {
        addMods(t, modSum)
    }
}


fun main() {
    val root = TreeNode(1)
    // Level 2
    root.children.add(TreeNode(2))
    root.children.add(TreeNode(3))
    // Level 3
    root.children[0].children.add(TreeNode(4))
    root.children[0].children.add(TreeNode(5))
    root.children[1].children.add(TreeNode(6))
    root.children[1].children.add(TreeNode(7))
    layout(root)
    printTree(root)
}


fun printTree(node: TreeNode) {
    println("Value: ${node.value}, X: ${node.x}, Y: ${node.y}")
    for (child in node.children) {
        printTree(child)
    }
}
