package com.khalekuzzamanjustcse.graph_editor.data_layer.repoisitory

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.data_layer.data.EditorEdgeEntity
import com.khalekuzzamanjustcse.graph_editor.data_layer.data.EditorNodeEntity
import com.khalekuzzamanjustcse.graph_editor.data_layer.data.createDao
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorVisualEdge
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditorVisualEdgeImp
import com.khalekuzzamanjustcse.graph_editor.ui.ui.node.GraphEditorNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class EditDatabase(
    context: Context,
    val density: Float,
) {
    private val dao = createDao(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    fun insert(graphEditorNodes: List<GraphEditorNode>) {
        scope.launch {
            //before insert delete the previous nodes
            dao.deleteAllNode()
            dao.insertNode(graphEditorNodes.map { createEntity(it) })
        }

    }

    fun insertEdge(edges: List<GraphEditorVisualEdge>) {
        scope.launch {
            //before insert delete the previous nodes
            dao.deleteAllEdge()
            dao.insertEdge(edges.map { createEntity(it) })
        }

    }

    fun getNodes(): Flow<List<GraphEditorNode>> =
        dao
            .getEditorNode()
            .map { list -> list.map { createNode(it) } }

    fun getEdges(): Flow<List<GraphEditorVisualEdgeImp>> =
        dao.getEditorEdge()
            .map { list -> list.map { createEdge(it) } }


    private fun createEntity(graphEditorNode: GraphEditorNode) =
        EditorNodeEntity(
            id = graphEditorNode.id,
            label = graphEditorNode.label,
            topLeftX = graphEditorNode.topLeft.x,
            topLeftY = graphEditorNode.topLeft.y,
            minNodeSizeDp = graphEditorNode.minNodeSize.value.toInt(),
            radiusDp = graphEditorNode.halfWidth.value.toInt()
        )

    private fun createEntity(edge: GraphEditorVisualEdge) =
        EditorEdgeEntity(
            cost = edge.cost,
            hasDirection = edge.isDirected,
            startX = edge.start.x,
            startY = edge.start.y,
            endX = edge.end.x,
            endY = edge.end.y,
            controlX = edge.control.x,
            controlY = edge.control.y
        )

    private fun createNode(entity: EditorNodeEntity) =
        GraphEditorNode(
            id = entity.id,
            label = entity.label,
            topLeft = Offset(entity.topLeftX, entity.topLeftY),
            minNodeSize = entity.minNodeSizeDp.dp,
            halfWidth = entity.radiusDp.dp
        )

    private fun createEdge(entity: EditorEdgeEntity) =
        GraphEditorVisualEdgeImp(
            id = entity.id,
            start = Offset(entity.startX, entity.startY),
            end = Offset(entity.endX, entity.endY),
            control = Offset(entity.controlX, entity.controlY),
            cost = entity.cost,
            minTouchTargetPx = 30.dp.value * density,
            isDirected = entity.hasDirection
        )

}


