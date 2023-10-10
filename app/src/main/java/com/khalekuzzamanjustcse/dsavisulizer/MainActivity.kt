package com.khalekuzzamanjustcse.dsavisulizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.khalekuzzamanjustcse.dsavisulizer.ui.theme.DSAVisulizerTheme
import com.khalekuzzamanjustcse.graph_editor.ui.ui.edge.GraphEditor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DSAVisulizerTheme {
                GraphEditor()
            }
        }
    }
}
