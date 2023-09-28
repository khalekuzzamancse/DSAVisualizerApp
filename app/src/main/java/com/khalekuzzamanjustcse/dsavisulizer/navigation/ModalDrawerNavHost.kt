package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.dsavisulizer.screens.ScreenWithDrawer
import com.khalekuzzamanjustcse.dsavisulizer.screens.UnderConstructionScreen
import kotlinx.coroutines.launch

@Preview
@Composable
fun ModalDrawerNavHostPreview() {
    ModalDrawerNavHost()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navAction = NavigationActions(navController)
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val openDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.open()
        }
    }
    val closeDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.close()
        }
    }
    NavHost(
        navController = navController,
        startDestination = TopLevelDestinations.LINEAR_DATA_STRUCTURE
    ) {
        composable(TopLevelDestinations.GRAPH_TRAVERSAL) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {

            }
        }
        composable(TopLevelDestinations.LINEAR_DATA_STRUCTURE) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {
                LinearDSScreenSwitch(openDrawer)
            }

        }

        composable(TopLevelDestinations.GRAPH_REPRESENTATION) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {
                UnderConstructionScreen(openDrawer)
            }

        }
        composable(TopLevelDestinations.GRAPH_ALGORITHMS) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {
                UnderConstructionScreen(openDrawer)
            }

        }
        composable(TopLevelDestinations.TREE_REPRESENTATION) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {
                UnderConstructionScreen(openDrawer)
            }

        }
        composable(TopLevelDestinations.TREE_TRAVERSAL) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {
                TreeTransversalSwitch(openDrawer)
            }

        }
        composable(TopLevelDestinations.TREE_ALGORITHMS) {
            ScreenWithDrawer(
                drawerState = drawerState,
                closeDrawer = closeDrawer,
                onDrawerItemClick = navAction::navigateTo
            ) {
                UnderConstructionScreen(openDrawer)
            }

        }

    }

}

