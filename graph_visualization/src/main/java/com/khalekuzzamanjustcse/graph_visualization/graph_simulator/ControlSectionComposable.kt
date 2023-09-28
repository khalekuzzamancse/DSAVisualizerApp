package com.khalekuzzamanjustcse.graph_visualization.graph_simulator

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khalekuzzamanjustcse.common_ui.DropdownMenuOption
import com.khalekuzzamanjustcse.common_ui.pop_up_window.CustomExposedDropDownMenu
import com.khalekuzzamanjustcse.common_ui.pop_up_window.ExposedDropDownMenuOption
import com.khalekuzzamanjustcse.graph_visualization.ui_layer.graph_input.ControlButton

sealed class GraphTraversalOption(private val algorithmName: String) : DropdownMenuOption {
    override val label: String
        get() = algorithmName
}

object BFS : GraphTraversalOption("BFS")
object DFS : GraphTraversalOption("DFS")

object TraversalOptions {
    val options = listOf(BFS, DFS)

    fun getOption(index: Int) = options[index]
}

@Preview
@Composable
fun GraphTraversalScreenControlSectionPreview() {
    val controls = remember {
        listOf(
            object : ControlButton {
                override val icon = Icons.Filled.NextPlan
                override val label = "Next"
                override val enabled = mutableStateOf(true)
                override fun onClick() {
                    Log.i("ControlButtonPreview : ", label)
                }
            },
            object : ControlButton {
                override val icon = Icons.Filled.Code
                override val label = "Pseudocode Execution"
                override val enabled = mutableStateOf(true)
                override fun onClick() {
                    Log.i("ControlButtonPreview : ", label)
                }
            },
            object : ControlButton {
                override val icon = Icons.Filled.Memory
                override val label = "Variables Status"
                override val enabled = mutableStateOf(true)
                override fun onClick() {
                    Log.i("ControlButtonPreview : ", label)
                }
            }
        )
    }
    val options =TraversalOptions.options
    GraphTraversalScreenControl(
        title = "GraphTraversal",
        controls = controls,
        dropdownMenuOptions = options
    ) {
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GraphTraversalScreenControl(
    modifier: Modifier = Modifier,
    title: String,
    dropdownMenuOptions: List<DropdownMenuOption>,
    controls: List<ControlButton>,
    onOptionSelected: (Int) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 17.sp)
            CustomExposedDropDownMenu(
                label = "Select One",
                options = dropdownMenuOptions,
                onItemClick = {
                    onOptionSelected(it)
                })

            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(modifier = Modifier.fillMaxWidth()) {
                controls.forEach {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        enabled = it.enabled.value,
                        onClick = it::onClick
                    ) {
                        when (it.icon) {
                            is ImageVector -> {
                                Icon(it.icon as ImageVector, null)
                            }
                        }
                        Text(text = it.label)
                    }
                }
            }
        }

    }
}