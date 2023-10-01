package com.khalekuzzamanjustcse.common_ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.ControlButton


@Preview
@Composable
fun ControlSectionPreview() {
    val enabled = remember {
        mutableStateOf(true)
    }
    val controls= listOf(
        object : ControlButton {
            override val icon=Icons.Filled.Grain
            override val label="Add Node"
            override val enabled=enabled
            override fun onClick() {
                Log.i("ControlButtonPreview : ", label)
            }
        },
        object :ControlButton{
            override val icon=Icons.Filled.Remove
            override val label="Remove Node"
            override val enabled=enabled
            override fun onClick() {
                Log.i("ControlButtonPreview : ", label)
            }
        },
        object :ControlButton{
            override val icon=Icons.Filled.LinearScale
            override val label="Add Edge"
            override val enabled=enabled
            override fun onClick() {
                Log.i("ControlButtonPreview : ", label)
            }
        },
        object :ControlButton{
            override val icon=Icons.Filled.Undo
            override val label="Undo"
            override val enabled=enabled
            override fun onClick() {
                Log.i("ControlButtonPreview : ", label)
            }
        },
        object :ControlButton{
            override val icon=Icons.Filled.Redo
            override val label="Redo"
            override val enabled=enabled
            override fun onClick() {
                Log.i("ControlButtonPreview : ", label)
            }
        },
        object :ControlButton{
            override val icon=Icons.Filled.DoneOutline
            override val label="Done"
            override val enabled=enabled
            override fun onClick() {
                Log.i("ControlButtonPreview : ", label)
            }
        },
    )
    Box(modifier=Modifier) {
        ControlsComposable(title = "Graph Editor", controls = controls)
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ControlsComposable(
    modifier: Modifier = Modifier,
    title: String,
    controls: List<ControlButton>
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(50.dp))
            FlowRow {
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
