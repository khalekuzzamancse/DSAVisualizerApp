package com.khalekuzzamanjustcse.common_ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.common_ui.appbar.TopAppbarData

class LinkedListViewModel(
    val density: Density,
) : ViewModel() {

    val topAppbarData =
        TopAppbarData(
            title = "Linked List Screen ",
            elevation = 8.dp,
            navigationIcon = AppbarItem(label = "Menu", icon = Icons.Filled.Menu),
            actions = listOf(
                AppbarItem(label = "Add", icon = Icons.Filled.Add),
                AppbarItem(label = "Remove", icon = Icons.Filled.RemoveCircle),
            )
        )


}