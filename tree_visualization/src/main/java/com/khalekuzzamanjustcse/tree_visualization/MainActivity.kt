package com.khalekuzzamanjustcse.tree_visualization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreePreview
import com.khalekuzzamanjustcse.tree_visualization.ui.theme.DSAVisulizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DSAVisulizerTheme {
                TreePreview()
            }
        }
    }
}

