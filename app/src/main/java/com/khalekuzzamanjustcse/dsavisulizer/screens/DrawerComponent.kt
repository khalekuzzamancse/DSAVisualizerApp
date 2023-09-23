package com.khalekuzzamanjustcse.dsavisulizer.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineStops
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector
import com.khalekuzzamanjustcse.dsavisulizer.navigation.TopLevelDestinations

data class ModalDrawerItem(
    val label: String,
    val icon: ImageVector,
)

data class ModalDrawerGroup(
    val name: String,
    val members: List<ModalDrawerItem>,
)

object DrawerItemsProvider {
    private val linearDS = ModalDrawerGroup(
        name = "Linear DS",
        members = listOf(
            ModalDrawerItem(TopLevelDestinations.STACK, Icons.Filled.StackedBarChart),
            ModalDrawerItem(TopLevelDestinations.QUEUE, Icons.Filled.Queue)
        )
    )
    private val graphTheory = ModalDrawerGroup(
        name = "Graph Theory",
        members = listOf(
            ModalDrawerItem(TopLevelDestinations.GRAPH_REPRESENTATION, Icons.Filled.Grain),
            ModalDrawerItem(TopLevelDestinations.GRAPH_TRAVERSAL, Icons.Filled.TravelExplore),
            ModalDrawerItem(TopLevelDestinations.GRAPH_ALGORITHMS, Icons.Filled.AirlineStops)
        )
    )
    private val tree = ModalDrawerGroup(
        name = "Tree Data structure",
        members = listOf(
            ModalDrawerItem(TopLevelDestinations.TREE_REPRESENTATION, Icons.Filled.Grain),
            ModalDrawerItem(TopLevelDestinations.TREE_TRAVERSAL, Icons.Filled.TravelExplore),
            ModalDrawerItem(TopLevelDestinations.TREE_ALGORITHMS, Icons.Filled.AirlineStops)
        )
    )
    val drawerGroups = listOf(linearDS, graphTheory, tree)


}




