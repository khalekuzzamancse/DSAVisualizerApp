package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.queue_queue.QueueComposablePreview
import com.khalekuzzamanjustcse.common_ui.queue_queue.StackComposablePreview
import com.khalekuzzamanjustcse.dsavisulizer.screens.UnderConstructionScreen
import com.khalekuzzamanjustcse.linear_datastructures.LinearDataStructureScreen


@Composable
fun LinearDSScreenSwitch(
    onNavigationIconClick: () -> Unit,
) {
    val navController = rememberNavController()
    val navigationActions = NavigationActions(navController)
    LinearDataStructureScreen(onDestinationClick = navigationActions::navigateTo) {
        LinearDSNavGraph(
            navController,
            onNavigationIconClick,
            scaffoldPadding = it
        )
    }

}

@Preview
@Composable
private fun LinearDSScreenNavigationPreview() {
    var cnt by remember {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    Column {
        Button(onClick = {
            if (cnt % 2 == 0)
                navController.navigate(LinearDSDestinations.STACK_SCREEN)
            else
                navController.navigate(LinearDSDestinations.QUEUE_SCREEN)
            cnt++
        }) {
            Text(text = "ChangeScreen")
        }
        LinearDSNavGraph(navController, {})
    }

}

@Composable
fun LinearDSNavGraph(
    navController: NavHostController,
    onNavigationIconClick: () -> Unit,
    scaffoldPadding:PaddingValues = PaddingValues(0.dp),
) {
    NavHost(
        navController = navController,
        route = TopLevelDestinations.LINEAR_DATA_STRUCTURE,
        startDestination = LinearDSDestinations.LINKED_LIST_SCREEN
    ) {
        composable(route = LinearDSDestinations.LINKED_LIST_SCREEN) {
            UnderConstructionScreen(
                onNavigationIconClick = onNavigationIconClick)
        }
        composable(route = LinearDSDestinations.STACK_SCREEN) {
            StackComposablePreview(scaffoldPadding)
        }
        composable(route = LinearDSDestinations.QUEUE_SCREEN) {
            QueueComposablePreview(scaffoldPadding)
        }

    }
}