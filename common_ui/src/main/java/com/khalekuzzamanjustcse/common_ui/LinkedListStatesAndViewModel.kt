package com.khalekuzzamanjustcse.common_ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.common_ui.appbar.TopAppbarData
import com.khalekuzzamanjustcse.common_ui.queue_queue.QueueState

class LinkedListViewModel(
    val density: Density,
) : ViewModel() {
    private val queueState = QueueState(cellSize = 64.dp, density = density)
    val topAppbarData =
        TopAppbarData(
            title = "Linked List Screen ",
            elevation = 8.dp,
            navigationIcon = null,
            actions = listOf(
                AppbarItem(label = "Add", icon = Icons.Filled.Add),
                AppbarItem(label = "Remove", icon = Icons.Filled.RemoveCircle),
            )
        )


}