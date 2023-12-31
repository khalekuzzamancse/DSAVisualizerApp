package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.common_ui.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.queue_stack.LinearDSViewModel
import com.khalekuzzamanjustcse.common_ui.queue_stack.LinearDataStructureScreen
import com.khalekuzzamanjustcse.common_ui.queue_stack.QueueVisualizationScreen
import com.khalekuzzamanjustcse.common_ui.queue_stack.StackVisualizationScreen
import com.khalekuzzamanjustcse.dsavisulizer.screens.UnderConstructionScreen


@Composable
fun LinearDSScreenSwitch(
    onNavigationIconClick: () -> Unit,
) {
    val navController = rememberNavController()
    val navigationActions = NavigationActions(navController)

    val density = LocalDensity.current
    val viewModels = LinearDSViewModel(density)

    LinearDataStructureScreen(
        viewModel = viewModels,
        onDestinationClick = navigationActions::navigateTo,
        currentScreen = {
            LinearDataStructureScreenContent(
                viewModels,
                navController,
                scaffoldPadding = it
            )
        }
    )

}

@Composable
fun LinearDataStructureScreenContent(
    viewModel: LinearDSViewModel,
    navController: NavHostController,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp),
) {


    NavHost(
        navController = navController,
        route = TopLevelDestinations.LINEAR_DATA_STRUCTURE,
        startDestination = LinearDSDestinations.LINKED_LIST_SCREEN
    ) {
        composable(route = LinearDSDestinations.LINKED_LIST_SCREEN) {
            UnderConstructionScreen()
        }
        composable(route = LinearDSDestinations.STACK_SCREEN) {
            StackVisualizationScreen(
                viewModel.stackViewModel.state,
                scaffoldPadding
            )
        }
        composable(route = LinearDSDestinations.QUEUE_SCREEN) {
//            QueueVisualizationScreen(
//                state = viewModel.queueViewModel.queue,
//                scaffoldPadding = scaffoldPadding
//            )
        }

    }
}