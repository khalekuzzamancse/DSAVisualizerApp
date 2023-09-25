package com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/*
View model GraphDrawer Composable
ViewModel should be independent of UI,
so do not hold any UI references so dependency on UI components
it should just request the data layer to modify the data
and then update the the GraphDrawer states
and handle the request of GraphDrawer state composable directly  or by delegating
use flow instead of composable MutableState so that
we can use the flow both data layer and composable
and also can manipulate it
***Host the state to it lowest common ancestor
ViewModel does not depends how the UI looks
 */
data class GraphDrawerViewModel(
    private val size: Dp,
    private val sizePx: Float,
) : ViewModel(
) {
    private var _nodes = MutableStateFlow(emptyList<NodeComposableState>())
    private var _edges = MutableStateFlow(emptyList<EdgeComposableState>())
    val nodes = _nodes.asStateFlow()
    val edges = _edges.asStateFlow()

    //
    private val recentlyClickedNodes = Array(2) { 0 }
    private var clickCount = 0
    private var lastTappedLocation = Offset.Zero

    //event handlers
    fun onDrag(nodeIndex: Int, offset: Offset) {
        val oldState = _nodes.value[nodeIndex]
        val updatedNode = oldState.copy(offset = offset + oldState.offset)
        _nodes.update { it.toMutableList().apply { set(nodeIndex, updatedNode) } }
    }

    fun onNodeClick(nodeIndex: Int) {
        recentlyClickedNodes[clickCount % 2] = nodeIndex
        clickCount++
    }

    fun onCanvasTapped(offset: Offset) {
        lastTappedLocation = offset
    }


    fun addNode(value: String) {
        _nodes.value += NodeComposableState(
            size = size, sizePx = sizePx, label = value, offset = lastTappedLocation
        )
    }

    fun addEdge() {
        _edges.value += EdgeComposableState(
            startPoint = _nodes.value[recentlyClickedNodes[0]].center,
            endPoint = _nodes.value[recentlyClickedNodes[1]].center,
        )
    }


    init {
        _nodes.value = _nodes.value + NodeComposableState(
            size = size, sizePx = sizePx, label = "10",
            offset = Offset(10f, 10f)
        )
        _nodes.value += NodeComposableState(
            size = size, sizePx = sizePx, label = "20",
            offset = Offset(2 * sizePx + 10f, 10f)
        )
    }
}