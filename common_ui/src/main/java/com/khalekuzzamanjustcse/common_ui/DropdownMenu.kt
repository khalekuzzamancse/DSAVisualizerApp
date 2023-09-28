package com.khalekuzzamanjustcse.common_ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

interface DropdownMenuOption {
    val label: String
}

data class Person(
    val name: String,
    val age: Int,
    val id: Int
) : DropdownMenuOption {
    override val label: String
        get() = "Name = $name"
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomDropDownMenuPreview() {
    val options = listOf(
        Person("Mr Charly", 23, 147500),
        Person("Mr Bean", 25, 147506),
        Person("Dr Larry", 55, 147507)
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Hello ") },
                actions = {
                    CustomDropDownMenu(
                       // icon = Icons.Default.ExpandMore,
                        options = options
                    ) { index ->
                        Log.i("ClickedItem: ", "${options[index]}")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

        }

    }

}

@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    options: List<DropdownMenuOption>,
    offset: DpOffset = DpOffset(0.dp,0.dp),
    onItemClick: (index: Int) -> Unit,
) {
    var show by remember {
        mutableStateOf(true)
    }
    Box(
        modifier = modifier,
//            .wrapContentSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        DropdownMenu(
            offset = offset,
            modifier = Modifier,
            expanded = show,
            onDismissRequest = {

            }
        ) {
            options.forEachIndexed { index, it ->
                DropdownMenuItem(text = {
                    Text(text = it.label)
                }, onClick = {
                    onItemClick(index)
                    show=false
                })
            }
        }
    }


}
