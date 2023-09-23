package com.khalekuzzamanjustcse.dsavisulizer.screens

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LinearDataStructureScreenPreview() {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val closeDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.close()
        }
    }


//    LinearDataStructureScreen(
//        drawerState = drawerState,
//        closeDrawer = closeDrawer,
//        onDestinationClick = {},
//    ) {//screen content
//        if()
//    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinearDataStructureScreen(
    drawerState: DrawerState,
    closeDrawer: () -> Unit,
    onDestinationClick: (String) -> Unit,
    screenContent: @Composable () -> Unit = {},
) {
    CommonScreenSlot(
        topAppbar = {},
        bottomAppbar = {
          //  LinearDSScreeenBottomNavigationBar(onBottomNavItemClick = onDestinationClick)
        },
        closeDrawer = closeDrawer,
        onDrawerItemClick = onDestinationClick,
        drawerState = drawerState,
        screenContent = screenContent,
    )
}