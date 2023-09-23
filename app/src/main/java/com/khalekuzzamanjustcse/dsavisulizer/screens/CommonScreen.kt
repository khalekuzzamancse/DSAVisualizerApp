package com.khalekuzzamanjustcse.dsavisulizer.screens

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.khalekuzzamanjustcse.dsavisulizer.screens.DrawerItemsProvider.drawerGroups

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWithDrawer(
    drawerState: DrawerState,
    closeDrawer: () -> Unit,
    onDrawerItemClick: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    ModalDrawer(
        modifier = Modifier,
        drawerGroups = drawerGroups,
        onDrawerItemClick = onDrawerItemClick,
        closeDrawer = closeDrawer,
        drawerState = drawerState,
    ) {
        content()
    }


}