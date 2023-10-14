package com.khalekuzzamanjustcse.graph_editor.ui.ui.node

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/*

We used here some extension method on nodes because these are only
used by the node manager so other going  to use it,so we do not keep these
method inside the Node class itself so that the node class become lightweight
 */
data class GraphEditorNodeManager(
    private val deviceDensity: Float,
) {

    private val _nodes = MutableStateFlow(emptySet<GraphEditorNode>())
    val nodes = _nodes.asStateFlow()

    /*
    Observe the canvas tapping so that:
    If node is tapped we have to change it color to indicate that it is tapped.
    User may want to remove the tapped node.
    User may want to drag the tapped node.
     */
    private var _selectedGraphEditorNode = MutableStateFlow<GraphEditorNode?>(null)
    val selectedNode=_selectedGraphEditorNode.asStateFlow()
    fun observeCanvasTap(offset: Offset) {
        _selectedGraphEditorNode.value = _nodes.value.find { it.isInsideCircle(offset) }
        val aNodeIsTapped = _selectedGraphEditorNode.value != null
        if (aNodeIsTapped){
//            deactivateAllNodes()//remove if already some node highlighted
            highlightTappedNode()
        }
        else
            deactivateAllNodes()
    }

    private fun highlightTappedNode() {
        //if a node is tapped
        _selectedGraphEditorNode.value?.let {
            val selectedNode = it.makeActive()
            //replace the tapped node with active node.
            _nodes.value = _nodes.value - it
            _nodes.value = _nodes.value + selectedNode
        }
    }

    private fun deactivateAllNodes() {
        _selectedGraphEditorNode.value=null
        val selectedNodeColor = Color.Blue
        _nodes.update { nodeSet ->
            nodeSet.map {
                if (it.color == selectedNodeColor) it.copy(color = Color.Red)
                else it
            }.toSet()
        }
    }

    private fun GraphEditorNode.makeActive(): GraphEditorNode {
        val activeNodeColor = Color.Blue
        return copy(color = activeNodeColor)
    }


    //

    fun removeNode() {
        _selectedGraphEditorNode.value?.let { activeNode ->
            _nodes.update { nodeSet ->
                nodeSet.filter { it.id != activeNode.id }.toSet()
            }
        }

    }

    fun add(graphEditorNode: GraphEditorNode) {
        _nodes.value = _nodes.value + graphEditorNode
    }

    fun setNode(set: Set<GraphEditorNode>) {
        _nodes.update { set }
    }


    fun onDragging(dragAmount: Offset) {
        _selectedGraphEditorNode.value?.let { activeNode ->
            _nodes.update { set ->
                set.map {
                    if(it.id==activeNode.id)
                    it.updateTopLeft(dragAmount)
                    else it

                }.toSet()
            }
        }
    }

    private fun GraphEditorNode.isInsideCircle(touchPosition: Offset): Boolean {
        val minX = topLeft.x
        val minY = topLeft.y
        val shapeSize = minNodeSize
        val maxX = minX + shapeSize.value * deviceDensity
        val maxY = minY + shapeSize.value * deviceDensity
        return touchPosition.x in minX..maxX && touchPosition.y in minY..maxY
    }

    private fun GraphEditorNode.updateTopLeft(amount: Offset): GraphEditorNode {
        var (x, y) = topLeft + amount
        if (x.isBeyondCanvas()) x = 0f
        if (y.isBeyondCanvas()) y = 0f
        return this.copy(topLeft = Offset(x, y))
    }
    private fun Float.isBeyondCanvas() = this < 0f
}