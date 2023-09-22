package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable

fun StackQueuePreview() {
    Column {
        StackComposablePreview()
        Spacer(modifier = Modifier.height(50.dp))
        QueueComposablePreview()
    }
}
