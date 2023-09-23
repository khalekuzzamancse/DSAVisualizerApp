package com.khalekuzzamanjustcse.tree_visualization.visualizer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.NetworkPing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.khalekuzzamanjustcse.common_ui.BottomNavigationItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.tree_visualization.TreeDestinations




class TreeTraversalScreenState {
    val bottomDestinations = listOf(
        BottomNavigationItem(
            label = TreeDestinations.BINARY_TREE,
            icon = Icons.Filled.NetworkPing,
        ),
        BottomNavigationItem(
            label = TreeDestinations.BINARY_SEARCH_TREE,
            icon = Icons.Filled.Grain,
        ),
        BottomNavigationItem(
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