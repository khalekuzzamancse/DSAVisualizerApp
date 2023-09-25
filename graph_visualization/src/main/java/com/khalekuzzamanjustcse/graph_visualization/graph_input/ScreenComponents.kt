package com.khalekuzzamanjustcse.graph_visualization.graph_input

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirlineStops
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NextPlan
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
import com.khalekuzzamanjustcse.common_ui.CustomDropDownMenu
import com.khalekuzzamanjustcse.graph_visualization.GraphTraversalOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeValueInputer(
    expanded: Boolean = false,
    offset: DpOffset = DpOffset(0.dp, (0).dp),
    onInputDone: (String) -> Unit,
) {
Log.i("UnTraversal",":Dropdown->$expanded")

    var text by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .wrapContentSize(),
        contentAlignment = Alignment.TopEnd
    ) {

        DropdownMenu(
            offset = offset,
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = {}
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            TextButton(onClick = {
                onInputDone(text)
                text = ""
            }) {
                Text(text = "Done")

            }

        }
    }


}
