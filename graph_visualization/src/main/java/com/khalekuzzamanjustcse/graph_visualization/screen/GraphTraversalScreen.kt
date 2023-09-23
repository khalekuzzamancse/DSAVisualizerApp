package com.khalekuzzamanjustcse.graph_visualization.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.khalekuzzamanjustcse.common_ui.BottomNavigationItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.graph_visualization.GraphDestination

class GraphTraversalScreenState {
    val bottomDestinations = listOf(
        BottomNavigationItem(
            label = GraphDestination.UNDIRECTED_TRAVERSAL,
            icon = Icons.Filled.CompareArrows,
        ),
        BottomNavigationItem(
            label = GraphDestination.DIRECTED_TRAVERSAL,
            icon = Icons.Filled.ArrowForward,
        ),
        BottomNavigationItem(
            label ="Other",
            icon = Icons.Filled.OtherHouses,
        )
    )

}
@Composable
fun GraphTraversalScreen(
    onDestinationClick: (String) -> Unit,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    val state = remember {
        GraphTraversalScreenState()
    }
    CommonScreenLayout(
        bottomBarDestinations = state.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
        }) {
        currentScreen(it)
    }

}