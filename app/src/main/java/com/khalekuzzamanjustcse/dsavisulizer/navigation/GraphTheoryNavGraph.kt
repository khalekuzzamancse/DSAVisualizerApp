package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.graph_visualization.GraphBuilderPreview
import com.khalekuzzamanjustcse.graph_visualization.GraphDestination
import com.khalekuzzamanjustcse.graph_visualization.screen.GraphTraversalScreen

@Preview
@Composable
fun GraphTraversalScreenSwitch() {
    val navController = rememberNavController()
    val navigationActions = NavigationActions(navController)
    GraphTraversalScreen(onDestinationClick = navigationActions::navigateTo) {
        GraphTheoryNavHost(navController)
    }
//
}

@Composable
fun GraphTheoryNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = TopLevelDestinations.GRAPH_TRAVERSAL,
        startDestination = GraphDestination.UNDIRECTED_TRAVERSAL
    ) {
        composable(route = GraphDestination.UNDIRECTED_TRAVERSAL) {
            GraphBuilderPreview()
        }
        composable(route = GraphDestination.DIRECTED_TRAVERSAL) {
            GraphBuilderPreview()
        }

    }
}

