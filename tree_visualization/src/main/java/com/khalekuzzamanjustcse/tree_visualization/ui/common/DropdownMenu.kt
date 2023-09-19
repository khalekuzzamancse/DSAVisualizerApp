package com.khalekuzzamanjustcse.tree_visualization.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp


@Composable
fun Menu(
    onMenuItemClick: (itemName: String) -> Unit,
    menuItems: List<String> ,
    offset: DpOffset = DpOffset(0.dp, (-200).dp),
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .wrapContentSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            onClick = { expanded = true },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "More"
            )
        }
        DropdownMenu(
            offset = offset,
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = {
                expanded = false

            }
        ) {
            menuItems.forEach {
                DropdownMenuItem(text = {
                    Text(text = it)
                }, onClick = {
                    onMenuItemClick(it)
                    expanded = false

                })
            }
        }
    }


}

