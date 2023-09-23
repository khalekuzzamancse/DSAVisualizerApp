package com.khalekuzzamanjustcse.common_ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
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
import com.khalekuzzamanjustcse.common_ui.appbar.CommonTopAppbar
import com.khalekuzzamanjustcse.common_ui.appbar.TopAppbarData

interface IconComponent {
    val label: String
    val icon: ImageVector?
    val iconRes: Int?
    val showIcon: Boolean
    fun onClick()
}


data class AppbarItem(
    override val label: String,
    override val icon: ImageVector? = null,
    override val iconRes: Int? = null,
    override val showIcon: Boolean =true,
) : IconComponent {

    override fun onClick() {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreenLayout(
    topAppbarData: TopAppbarData,
    onTopAppbarItemClick: (IconComponent) -> Unit,
    bottomBarDestinations: List<IconComponent> = emptyList(),
    onDestinationClick: (IconComponent) -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    screenContent: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppbar(topAppbarData, onClick = onTopAppbarItemClick)
        },
        containerColor = containerColor,
        snackbarHost = snackBarHost,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
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
                        },
                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ) { padding ->
        screenContent(padding)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreenLayout(
    topBar: @Composable () -> Unit = {},
    bottomBarDestinations: List<IconComponent> = emptyList(),
    onDestinationClick: (IconComponent) -> Unit = {},
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
                        },
                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ) { padding ->
        screenContent(padding)
    }

}

