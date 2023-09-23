package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Density
import com.khalekuzzamanjustcse.common_ui.AppbarItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.common_ui.IconComponent
import com.khalekuzzamanjustcse.common_ui.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.LinkedListViewModel

sealed interface Screen {
    val route: String
}

object LinkedListScreen : Screen {
    override val route: String
        get() = "Linked List Screen"
}

object QueueScreen : Screen {
    override val route: String
        get() = "Queue Screen"
}

object StackScreen : Screen {
    override val route: String
        get() = "Stack Screen"
}

class LinearDSViewModel(
    val density: Density,
) {
    val bottomDestinations = listOf(
        AppbarItem(label = LinearDSDestinations.LINKED_LIST_SCREEN, icon = Icons.Filled.List),
        AppbarItem(label = LinearDSDestinations.STACK_SCREEN, icon = Icons.Filled.StackedBarChart),
        AppbarItem(label = LinearDSDestinations.QUEUE_SCREEN, icon = Icons.Filled.Queue)
    )

    //view models
    val queueViewModel = QueueViewModel(density)
    val stackViewModel = StackViewModel(density)
    private val linkedListViewModel = LinkedListViewModel(density)

    init {
        for(i in 5 until  10){
            queueViewModel.queueState.enqueue("$i")
            stackViewModel.stateState.push("${2 * i}")
        }

    }



    //end
    private var currentScreen: Screen = LinkedListScreen
    private var currentTopAppbar = mutableStateOf(linkedListViewModel.topAppbarData)

    val topAppbarData
        get() = currentTopAppbar.value

    fun onTopAppbarIconClick(item: IconComponent) {
        when (currentScreen) {
            StackScreen -> stackViewModel.onTopAppbarIconClick(item)
            QueueScreen -> queueViewModel.onTopAppbarIconClick(item)
            else -> {}
        }
    }

    fun onBottomAppbarIconClick(item: IconComponent) {
        if (isLinkedListScreen(item)) {
            currentScreen = LinkedListScreen
            currentTopAppbar.value = linkedListViewModel.topAppbarData
        } else if (isQueueScreen(item)) {
            currentScreen = QueueScreen
            currentTopAppbar.value = queueViewModel.topAppbarData
        } else if (isStackScreen(item)) {
            currentScreen = StackScreen
            currentTopAppbar.value = stackViewModel.topAppbarData
        }
    }

    private fun isLinkedListScreen(item: IconComponent) = bottomDestinations[0] == item
    private fun isStackScreen(item: IconComponent) = bottomDestinations[1] == item
    private fun isQueueScreen(item: IconComponent) = bottomDestinations[2] == item

}

@Composable
fun LinearDataStructureScreen(
    onDestinationClick: (String) -> Unit,
    viewModel:LinearDSViewModel,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    CommonScreenLayout(
        topAppbarData = viewModel.topAppbarData,
        onTopAppbarItemClick = viewModel::onTopAppbarIconClick,
        bottomBarDestinations = viewModel.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
            viewModel.onBottomAppbarIconClick(it)
        }) {
        currentScreen(it)
    }

}