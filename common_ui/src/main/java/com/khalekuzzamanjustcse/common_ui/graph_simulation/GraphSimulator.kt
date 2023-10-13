//package com.khalekuzzamanjustcse.common_ui.graph_simulation
//
//import androidx.compose.ui.graphics.Color
//import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphBasicNode
//import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphSimulationNode
//import com.khalekuzzamanjustcse.common_ui.graph_editor_2.edge.GraphEditorVisualEdge
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//interface GraphSimulator {
//    val nodes: StateFlow<Set<GraphSimulationNode>>
//    val edges: StateFlow<Set<GraphEditorVisualEdge>>
//    fun changeColor(nodeId: Int)
//    fun resetColors()
//    fun resetGraph(nodes: Set<GraphBasicNode>, edges: Set<GraphEditorVisualEdge>)
//}
//
//data class GraphSimulatorImp(
//    private val basicNodes: Set<GraphBasicNode> = emptySet(),
//    private val basicEdge: Set<GraphEditorVisualEdge> = emptySet(),
//) : GraphSimulator {
//
//    private val _nodes: MutableStateFlow<Set<GraphSimulationNode>> = MutableStateFlow(
//        basicNodes.map { basicNode -> GraphSimulationNode(basicNode) }.toSet()
//    )
//    override val nodes: StateFlow<Set<GraphSimulationNode>>
//        get() = _nodes.asStateFlow()
//
//    private val _edges: MutableStateFlow<Set<GraphEditorVisualEdge>> = MutableStateFlow(
//        basicEdge.map { it }.toSet()
//    )
//    override val edges: StateFlow<Set<GraphEditorVisualEdge>>
//        get() = _edges.asStateFlow()
//
//    override fun changeColor(nodeId: Int) {
//        _nodes.update { set ->
//            set.map { if (it.id == nodeId) it.copy(color = Color.Red) else it }.toSet()
//        }
//    }
//
//    override fun resetColors() {
//        _nodes.update { set ->
//            set.map { it.copy(color = Color.Green) }.toSet()
//        }
//    }
//
//    override fun resetGraph(nodes: Set<GraphBasicNode>, edges: Set<GraphEditorVisualEdge>) {
//        _nodes.update {
//            nodes.map { basicNode -> GraphSimulationNode(basicNode) }.toSet()
//        }
//        _edges.update { edges }
//    }
//}