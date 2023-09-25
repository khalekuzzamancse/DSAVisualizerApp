package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.dsavisulizer.screens.UnderConstructionScreen
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.UndirectedGraphTraversalScreen
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.GraphDestination
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.screen.GraphTraversalScreen
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.screen.GraphTraversalScreensViewModel


@Composable
fun GraphTraversalScreenSwitch(
    onNavigationIconClick: () -> Unit,
) {
    val nodeSizePX = 64.dp.value * LocalDensity.current.density
    val viewModel = remember {
        GraphTraversalScreensViewModel(nodeSizePX)
    }
    val navController = rememberNavController()
    val navigationActions = NavigationActions(navController)
    GraphTraversalScreen(viewModel) { scaffoldPadding ->
        GraphTraversalScreensContent(
            viewModel,
            navController,
            scaffoldPadding,
            onNavigationIconClick
        )
    }


}

@Composable
fun GraphTraversalScreensContent(
    viewModel: GraphTraversalScreensViewModel,
    navController: NavHostController,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp),
    onNavigationIconClick: () -> Unit,
) {
    val state= remember {
        viewModel.state
    }
    NavHost(
        navController = navController,
        route = TopLevelDestinations.GRAPH_TRAVERSAL,
        startDestination = GraphDestination.UNDIRECTED_TRAVERSAL
    ) {
        composable(route = GraphDestination.UNDIRECTED_TRAVERSAL) {
            UndirectedGraphTraversalScreen(
                list=state._list.value,
                edgesRef = state.graph.edges,
                graph = state.graph,
                scaffoldPadding = scaffoldPadding
            )
        }
        composable(route = GraphDestination.DIRECTED_TRAVERSAL) {
            UnderConstructionScreen(onNavigationIconClick = onNavigationIconClick)
        }

    }
}

