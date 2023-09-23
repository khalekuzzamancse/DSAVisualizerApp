package com.khalekuzzamanjustcse.linear_datastructures

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.khalekuzzamanjustcse.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.AppbarItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout

class LinearDataStructureScreenState {
    val bottomDestinations = listOf(
        AppbarItem(
            label = LinearDSDestinations.LINKED_LIST_SCREEN,
            icon = Icons.Filled.List,
        ),
        AppbarItem(
            label = LinearDSDestinations.STACK_SCREEN,
            icon = Icons.Filled.StackedBarChart,
        ),
        AppbarItem(
            label = LinearDSDestinations.QUEUE_SCREEN,
            icon = Icons.Filled.Queue,
        )
    )
    
}

@Preview
@Composable
fun LinearDataStructureScreenPreview() {
    LinearDataStructureScreen(onDestinationClick = {

    }) {

    }
}
@Composable
fun LinearDataStructureScreen(
    onDestinationClick: (String) -> Unit,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    val state = remember {
        LinearDataStructureScreenState()
    }
    CommonScreenLayout(
        bottomBarDestinations = state.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
        }) {
        currentScreen(it)
    }

}