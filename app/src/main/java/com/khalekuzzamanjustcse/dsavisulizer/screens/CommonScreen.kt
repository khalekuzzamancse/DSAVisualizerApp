package com.khalekuzzamanjustcse.dsavisulizer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.khalekuzzamanjustcse.dsavisulizer.screens.DrawerItemsProvider.drawerGroups

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreenSlot(
    drawerState: DrawerState,
    closeDrawer: () -> Unit,
    onDrawerItemClick: (String) -> Unit,
    topAppbar: @Composable () -> Unit,
    bottomAppbar: @Composable () -> Unit,
    screenContent: @Composable () -> Unit,
) {
    ModalDrawer(
        modifier = Modifier,
        drawerGroups = drawerGroups,
        onDrawerItemClick = onDrawerItemClick,
        closeDrawer = closeDrawer,
        drawerState = drawerState,
    ) {
        Box{
            Scaffold(
                topBar = topAppbar, bottomBar = bottomAppbar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    screenContent()
                }
            }
        }

    }


}