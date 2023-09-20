package com.khalekuzzamanjustcse.graph_visualization.graph_input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphBuilderScreenTopAppbar(
    title: String,
    isOnInputMode: Boolean = true,
    onNodeAdded: (String) -> Unit,
    onAddEdgeClick: () -> Unit
) {


    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
        ),
        title = {
            Text(
                text = if (isOnInputMode) "Input Tree" else "$title Traversal",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        actions = {
            NodeValueInputer() { onNodeAdded(it) }
                IconButton(onClick = onAddEdgeClick) {
                    Icon(
                        imageVector = Icons.Filled.Timeline,
                        contentDescription = "add Edge"
                    )
                }



        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeValueInputer(
    offset: DpOffset = DpOffset(0.dp, (0).dp),
    onInputDone: (String) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
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
                imageVector = Icons.Default.Add,
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
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            TextButton(onClick = {
                onInputDone(text)
                expanded = false
                text = ""
            }) {
                Text(text = "Done")

            }

        }
    }


}
