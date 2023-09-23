package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.dsavisulizer.screens.UnderConstructionScreen
import com.khalekuzzamanjustcse.tree_visualization.TreeDestinations
import com.khalekuzzamanjustcse.tree_visualization.visualizer.TreeTraversalScreen
import com.khalekuzzamanjustcse.tree_visualization.visualizer.TreeVisualizerPreview


@Composable
fun TreeTransversalSwitch(
    onNavigationIconClick: () -> Unit,
) {
    val navController = rememberNavController()
    val navigationActions = NavigationActions(navController)
    TreeTraversalScreen(onDestinationClick = navigationActions::navigateTo) {
        TreeDSNavHost(navController,onNavigationIconClick)
    }

}

@Composable
fun TreeDSNavHost(
    navController: NavHostController,
    onNavigationIconClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        route = TopLevelDestinations.TREE_TRAVERSAL,
        startDestination = TreeDestinations.BINARY_TREE
    ) {
        composable(route = TreeDestinations.BINARY_TREE) {
            TreeVisualizerPreview()
        }
        composable(route = TreeDestinations.BINARY_SEARCH_TREE) {
            UnderConstructionScreen(onNavigationIconClick = onNavigationIconClick)
        }
        composable(route = TreeDestinations.NON_BINARY_TREE) {
            UnderConstructionScreen(onNavigationIconClick = onNavigationIconClick)
        }


    }
}

