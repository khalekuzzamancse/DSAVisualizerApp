package com.khalekuzzamanjustcse.common_ui.appbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.AppbarItem
import com.khalekuzzamanjustcse.common_ui.IconComponent


class TopAppbarState {
    private var _data by mutableStateOf(
        TopAppbarData(
            title = "Hello World !",
            elevation = 8.dp,
            navigationIcon = AppbarItem(label = "Menu", icon = Icons.Filled.Menu),
            actions = listOf(
                AppbarItem(label = "Person", icon = Icons.Filled.Person),
                AppbarItem(label = "Book", icon = Icons.Filled.Book),
                AppbarItem(label = "Down Arrow", icon = Icons.Filled.ArrowDropDownCircle)
            )
        )
    )
    val data: TopAppbarData
        get() = _data

    fun changeTopAppbar() {
        _data = TopAppbarData(
            title = "World Hello!",
            elevation = 8.dp,
            navigationIcon = AppbarItem(label = "Back Arrow", icon = Icons.Filled.ArrowBack),
            actions = listOf(
                AppbarItem(label = "Graph", icon = Icons.Filled.AutoGraph),
                AppbarItem(label = "Tree", icon = Icons.Filled.AccountTree),
                AppbarItem(label = "MoreVert", icon = Icons.Filled.MoreVert)
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CommonTopAppbarPreview() {
    val topAppbarState = remember {
        TopAppbarState()
    }

    Scaffold(
        topBar = {
            CommonTopAppbar(topAppbarState.data)
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = { topAppbarState.changeTopAppbar() }) {
                Text(text = "Change Top Appbar")
            }
        }
    }
}

data class TopAppbarData(
    val title: String,
    val titleFontSize: TextUnit = TextUnit.Unspecified,
    val navigationIcon: IconComponent? = null,
    val elevation: Dp,
    val actions: List<IconComponent> = emptyList()
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppbar(
    topAppbarData: TopAppbarData,
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(topAppbarData.elevation)
        ),
        title = {
            Text(
                text = topAppbarData.title,
                fontSize = topAppbarData.titleFontSize,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            if (topAppbarData.navigationIcon != null) {
                IconButton(onClick = {
                    topAppbarData.navigationIcon.onClick()
                    // onClick(topAppbarData.navigationIcon)
                }) {
                    if (topAppbarData.navigationIcon.icon != null) {
                        Icon(
                            imageVector = topAppbarData.navigationIcon.icon!!,
                            contentDescription = null
                        )
                    } else if (topAppbarData.navigationIcon.iconRes != null) {
                        Icon(
                            painter = painterResource(id = topAppbarData.navigationIcon.iconRes!!),
                            contentDescription = null
                        )
                    }
                }
            }
        },
        actions = {
            topAppbarData.actions.forEach { action ->
                TextButton(onClick = {
                    action.onClick()
                }) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (action.icon != null) {
                            Icon(
                                imageVector = action.icon!!,
                                contentDescription = null
                            )
                        } else if (action.iconRes != null) {
                            Icon(
                                painter = painterResource(id = action.iconRes!!),
                                contentDescription = null
                            )
                        }
                        if (action.showIcon) {
                            Text(text = action.label)
                        }

                    }
                }
            }
        }

    )

}