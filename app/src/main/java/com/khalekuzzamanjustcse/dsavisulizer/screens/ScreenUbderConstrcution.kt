package com.khalekuzzamanjustcse.dsavisulizer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.Fence
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.material.icons.filled.PlayDisabled
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnderConstructionScreen(
    onNavigationIconClick: () -> Unit
) {
    var color by remember {
        mutableStateOf(randomColor())
    }
    val list = listOf(
        Pair("Data Structure", Icons.Filled.DataArray),
        Pair("Algorithms", Icons.Filled.Lightbulb),
        Pair("Time Complexity", Icons.Filled.Timeline),
    )

    LaunchedEffect(Unit) {
        while (true) {
            color = randomColor()
            delay(300)
        }
    }
    val textColor = if (color.luminance() > 0.5) Color.Black else Color.White
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                ),
                title = {
                    Text(
                        text = "Coming Soon",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.NextPlan,
                            contentDescription = "next"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.PlayDisabled,
                            contentDescription = "next"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Fence,
                            contentDescription = "next"
                        )
                    }
                }

            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
            ) {
                var selected by remember {
                    mutableStateOf(list[0])
                }
                list.forEach { item ->
                    NavigationBarItem(
                        selected = selected == item,
                        onClick = {
                            selected = item
                        },
                        icon = {
                            Icon(imageVector = item.second, contentDescription = null)
                        },
                        label = {
                            Text(item.first)
                        }

                    )
                }
            }
        }

    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Under Construction",
                fontSize = 20.sp,
                color = textColor
            )
        }
    }
}

private fun randomColor(): Color {
    val red = Random.nextFloat()
    val green = Random.nextFloat()
    val blue = Random.nextFloat()
    return Color(red, green, blue)
}