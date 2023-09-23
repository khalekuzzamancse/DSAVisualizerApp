package com.khalekuzzamanjustcse.dsavisulizer.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khalekuzzaman.just.cse.gmailclone.ui.navigation.NavigationActions
import com.khalekuzzamanjustcse.common_ui.queue_queue.QueueComposable
import com.khalekuzzamanjustcse.common_ui.queue_queue.QueueState
import com.khalekuzzamanjustcse.common_ui.queue_queue.StackComposable
import com.khalekuzzamanjustcse.common_ui.queue_queue.StackState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
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
    val size = 64.dp
    val density = LocalDensity.current
    val queueState = remember { QueueState(cellSize = size, density = density) }
    val stackState = remember { StackState(cellSize = size, density = density) }

    NavHost(
        navController = navController,
        startDestination = TopLevelDestinations.LINEAR_DATA_STRUCTURE
    ) {

    }

}

