package com.khalekuzzamanjustcse.graph_visualization.screen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.common_ui.IconComponent
import com.khalekuzzamanjustcse.common_ui.queue_queue.Screen
import com.khalekuzzamanjustcse.graph_visualization.GraphState
import com.khalekuzzamanjustcse.graph_visualization.UndirectedGraphTraversalViewModel

class GraphTraversalScreensViewModel(
    nodeSizePX: Float,
) : ViewModel() {

    val state=GraphState(nodeSizePX)

    val undirectedGraphTraversalViewModel = UndirectedGraphTraversalViewModel(nodeSizePX)
    private var currentScreen: Screen = UndirectedTraversalScreen
    private var currentTopAppbar =
        mutableStateOf(undirectedGraphTraversalViewModel.inputModeTopAppbar)
    val topAppbarData
        get() = currentTopAppbar.value
    val bottomDestinations = listOf(
        object : IconComponent(label = "Un Traversal", icon = Icons.Filled.CompareArrows) {
            override fun onClick() {
                currentScreen = UndirectedTraversalScreen
                currentTopAppbar.value = undirectedGraphTraversalViewModel.inputModeTopAppbar
            }
        },
        object : IconComponent(label = "Dir Traversal", icon = Icons.Filled.ArrowForward) {
            override fun onClick() {
                currentScreen = DirectedTraversalScreen
                currentTopAppbar.value = undirectedGraphTraversalViewModel.inputModeTopAppbar
            }
        },
        object : IconComponent(label = "Other", icon = Icons.Filled.OtherHouses) {
            override fun onClick() {
            }
        },
    )
}

@Composable
fun GraphTraversalScreen(
    viewModel:  GraphTraversalScreensViewModel,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    Log.i("RecompositionScreen", ":MainScreen")
    CommonScreenLayout(
        topAppbarItems = viewModel.topAppbarData,
        bottomBarDestinations = viewModel.bottomDestinations
    ) {
        currentScreen(it)
    }

}