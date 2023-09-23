package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.queue_queue.QueueComposablePreview
import com.khalekuzzamanjustcse.common_ui.queue_queue.StackComposablePreview
import com.khalekuzzamanjustcse.linear_datastructures.LinearDataStructureScreen

@Preview
@Composable
fun LinearDSScreenSwitch() {
    val navController = rememberNavController()
    val navigationActions = NavigationActions(navController)
    LinearDataStructureScreen(onDestinationClick = navigationActions::navigateTo) {
        LinearDSNavGraph(navController)
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
        LinearDSNavGraph(navController)
    }

}

@Composable
fun LinearDSNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = TopLevelDestinations.LINEAR_DATA_STRUCTURE,
        startDestination = LinearDSDestinations.STACK_SCREEN
    ) {
        composable(route = LinearDSDestinations.STACK_SCREEN) {
            StackComposablePreview()
        }
        composable(route = LinearDSDestinations.QUEUE_SCREEN) {
            QueueComposablePreview()
        }
        composable(route = LinearDSDestinations.LINKED_LIST_SCREEN) {
            StackComposablePreview()
        }
    }
}