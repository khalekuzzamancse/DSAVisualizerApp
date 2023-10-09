package com.khalekuzzamanjustcse.graph_editor.components

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyButton(
    label: String,
    enabled: Boolean = true,
    icon: ImageVector?=null,
    onClick: () -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = onClick
    ) {
        if(icon != null){
            Icon(icon, null)
        }
        Text(text = label)
    }

}