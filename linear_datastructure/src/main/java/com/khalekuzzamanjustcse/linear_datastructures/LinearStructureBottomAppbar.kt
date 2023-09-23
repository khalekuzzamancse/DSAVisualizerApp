package com.khalekuzzamanjustcse.linear_datastructures

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.LinearDSDestinations
import com.khalekuzzamanjustcse.common_ui.BottomNavigationItem
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout

class LinearDataStructureScreenState {
    val bottomDestinations = listOf(
        BottomNavigationItem(
            label = LinearDSDestinations.LINKED_LIST_SCREEN,
            icon = Icons.Filled.List,
        ),
        BottomNavigationItem(
            label = LinearDSDestinations.STACK_SCREEN,
            icon = Icons.Filled.StackedBarChart,
        ),
        BottomNavigationItem(
            label = LinearDSDestinations.QUEUE_SCREEN,
            icon = Icons.Filled.Queue,
        )
    )
    
}

@Preview
@Composable
fun LinearDataStructureScreenPreview() {
    LinearDataStructureScreen(onDestinationClick = {

    }) {

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
        bottomBarDestinations = state.bottomDestinations,
        onDestinationClick = {
            onDestinationClick(it.label)
        }) {
        currentScreen(it)
    }

}