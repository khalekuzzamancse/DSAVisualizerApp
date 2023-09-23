package com.khalekuzzamanjustcse.common_ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

interface NavigationDestinationItem {
    val label: String
    val icon: ImageVector?
    val iconRes: Int?
}


data class BottomNavigationItem(
    override val label: String,
    override val icon: ImageVector?=null,
    override val iconRes: Int?=null,
) : NavigationDestinationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreenLayout(
    topBar: @Composable () -> Unit = {},
    bottomBarDestinations: List<NavigationDestinationItem> = emptyList(),
    onDestinationClick: (NavigationDestinationItem) -> Unit ={},
    snackBarHost: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    screenContent: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = topBar,
        containerColor = containerColor,
        snackbarHost = snackBarHost,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                var selected by remember {
                    mutableStateOf(bottomBarDestinations[0])
                }
                bottomBarDestinations.forEach { item ->
                    NavigationBarItem(
                        selected = selected == item,
                        onClick = {
                            onDestinationClick(item)
                            selected = item
                        },
                        icon = {
                            item.icon?.let { Icon(imageVector = it, contentDescription = null) }
                            item.iconRes?.let {
                                Icon(
                                    painter = painterResource(id = it),
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        screenContent(padding)
    }

}