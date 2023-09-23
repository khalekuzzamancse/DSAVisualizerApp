package com.khalekuzzamanjustcse.dsavisulizer.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawer(
    modifier: Modifier,
    drawerGroups: List<ModalDrawerGroup>,
    onDrawerItemClick: (navigateTo: String) -> Unit,
    drawerState: DrawerState,
    closeDrawer: () -> Unit,
    content: @Composable () -> Unit,
    ) {


    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            val drawerWidth = with(LocalDensity.current) {
                (LocalConfiguration.current.screenWidthDp * 0.80).dp
            }
            DrawerContent(
                modifier = Modifier
                    .width(drawerWidth)
                    .fillMaxHeight(),
                drawerGroups,
                onNavigate = onDrawerItemClick,
                closeDrawer = closeDrawer,
            )
        }) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerContent(
    modifier: Modifier = Modifier,
    drawerGroups: List<ModalDrawerGroup>,
    onNavigate: (navigateTo: String) -> Unit,
    closeDrawer: () -> Unit,
) {
    val scrollState = rememberScrollState()
    ModalDrawerSheet(
        modifier = modifier.verticalScroll(scrollState)
    ) {

        val firstGroupFirstItem = drawerGroups[0].members[0]
        val selectedItem = remember { mutableStateOf(firstGroupFirstItem) }
        drawerGroups.forEach { group ->
            val items = group.members
            DisplayGroupName(group.name)

            //placing the each drawer items
            items.forEach { drawerItem ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = drawerItem.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(drawerItem.label) },
                    selected = drawerItem == selectedItem.value,
                    onClick = {
                        selectedItem.value = drawerItem
                        onNavigate(drawerItem.label)
                        closeDrawer()
                    },
                )
            }
        }


    }

}


@Composable
private fun DisplayGroupName(groupName: String) {
    Row(
        //We want to align the start of the group name
        //with the start of the NavigationDrawer icon
        //that is why we have to use the exact padding
        //and the layout that is used by the  NavigationDrawer()
        //compose,so take the padding value and the alignmentValue
        //from the  NavigationDrawer() composable
        Modifier.padding(start = 16.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = groupName)
    }
}