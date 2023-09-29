package com.khalekuzzamanjustcse.graph_visualization.ui_layer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.khalekuzzamanjustcse.common_ui.queue_stack.QueueState
import com.khalekuzzamanjustcse.common_ui.queue_stack.QueueVisualizationScreen

@Composable
fun BFSVariableStatusShow(
    showQueue: Boolean,
    queueState: QueueState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        QueueVisualizationScreen(queueState)
    }
}