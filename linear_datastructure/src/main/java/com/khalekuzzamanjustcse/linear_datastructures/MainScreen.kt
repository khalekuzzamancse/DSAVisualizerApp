package com.khalekuzzamanjustcse.linear_datastructures

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.AppbarItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout
import com.khalekuzzamanjustcse.common_ui.IconComponent
import com.khalekuzzamanjustcse.common_ui.appbar.TopAppbarData
import kotlin.concurrent.timerTask

class LinearDataStructureScreenState {
    val bottomDestinations = listOf(
        AppbarItem(label = LinearDSDestinations.LINKED_LIST_SCREEN, icon = Icons.Filled.List),
        AppbarItem(label = LinearDSDestinations.STACK_SCREEN, icon = Icons.Filled.StackedBarChart),
        AppbarItem(label = LinearDSDestinations.QUEUE_SCREEN, icon = Icons.Filled.Queue)
    )
    private var _topAppbarData by mutableStateOf(
        TopAppbarData(
            title = "Stack DataStructure",
            elevation = 8.dp,
            navigationIcon = AppbarItem(label = "Back Arrow", icon = Icons.Filled.ArrowBack),
        )
    )
    val topAppbarData
        get() = _topAppbarData

    fun onTopAppbarIconClick(clicked: IconComponent) {

    }

    fun onBottomAppbarIconClick(item: IconComponent) {
        _topAppbarData = _topAppbarData.copy(title = item.label)
        if(isNotFrontScreen(item))
            _topAppbarData=_topAppbarData.copy(navigationIcon = null)

        else
            _topAppbarData=
                _topAppbarData.copy(navigationIcon = AppbarItem(label = "Back Arrow", icon = Icons.Filled.ArrowBack))
    }

    private fun isNotFrontScreen(item: IconComponent): Boolean {
        return bottomDestinations[0] != item
    }

}

@Preview
@Composable
fun LinearDataStructureScreenPreview() {

    LinearDataStructureScreen(
        onDestinationClick = {}) {

    }
}

@Composable
fun LinearDataStructureScreen(
    onDestinationClick: (String) -> Unit,
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    val state = remember {
        LinearDataStructureScreenState()
    }
    CommonScreenLayout(
        topAppbarData = state.topAppbarData,
        onTopAppbarItemClick = state::onTopAppbarIconClick,
        bottomBarDestinations = state.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
            state.onBottomAppbarIconClick(it)
        }) {
        currentScreen(it)
    }

}