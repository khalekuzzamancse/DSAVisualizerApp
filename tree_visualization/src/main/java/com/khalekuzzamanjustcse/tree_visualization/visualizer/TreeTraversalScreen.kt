package com.khalekuzzamanjustcse.tree_visualization.visualizer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.NetworkPing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.khalekuzzamanjustcse.common_ui.AppbarItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.tree_visualization.TreeDestinations




class TreeTraversalScreenState {
    val bottomDestinations = listOf(
        AppbarItem(
            label = TreeDestinations.BINARY_TREE,
            icon = Icons.Filled.NetworkPing,
        ),
        AppbarItem(
            label = TreeDestinations.BINARY_SEARCH_TREE,
            icon = Icons.Filled.Grain,
        ),
        AppbarItem(
            label = TreeDestinations.NON_BINARY_TREE,
            icon = Icons.Filled.AccountTree,
        )
    )

}

@Composable
fun TreeTraversalScreen(
    onDestinationClick: (String) -> Unit,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    val state = remember {
        TreeTraversalScreenState()
    }
    CommonScreenLayout(
        bottomBarDestinations = state.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
        }) {
        currentScreen(it)
    }

}