package com.khalekuzzamanjustcse.common_ui.queue_queue

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Density
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.common_ui.IconComponent
import com.khalekuzzamanjustcse.common_ui.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.LinkedListViewModel

interface Screen {
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

    //view models
    val queueViewModel = QueueViewModel(density)
    val stackViewModel = StackViewModel(density)
    private val linkedListViewModel = LinkedListViewModel(density)

    //end
    private var currentScreen: Screen = LinkedListScreen
    private var currentTopAppbar = mutableStateOf(linkedListViewModel.topAppbarData)
    val topAppbarData
        get() = currentTopAppbar.value
    //
    val bottomDestinations = listOf(
        object : IconComponent(
            label = LinearDSDestinations.LINKED_LIST_SCREEN,
            icon = Icons.Filled.List
        ) {
            override fun onClick() {
                currentScreen = LinkedListScreen
                currentTopAppbar.value = linkedListViewModel.topAppbarData
            }
        },
        object : IconComponent(
            label = LinearDSDestinations.STACK_SCREEN,
            icon = Icons.Filled.StackedBarChart
        ) {
            override fun onClick() {
                currentScreen = StackScreen
                currentTopAppbar.value = stackViewModel.topAppbarData
                if (stackViewModel.state.stackElement.isEmpty())
                {
                    for (i in 1 until 6)
                        stackViewModel.state.push("${5 * i}")
                }
            }
        },
        object :
            IconComponent(label = LinearDSDestinations.QUEUE_SCREEN, icon = Icons.Filled.Queue) {
            override fun onClick() {
                currentScreen = QueueScreen
                currentTopAppbar.value = queueViewModel.topAppbarData
                if (queueViewModel.queue.element.isEmpty()){
                    for (i in 1 until 4)
                        queueViewModel.queue.enqueue("$i")
                }

            }
        }
    )

}

@Composable
fun LinearDataStructureScreen(
    onDestinationClick: (String) -> Unit,
    viewModel: LinearDSViewModel,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    CommonScreenLayout(
        topAppbarItems = viewModel.topAppbarData,
        bottomBarDestinations = viewModel.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
        }) {
        currentScreen(it)
    }

}