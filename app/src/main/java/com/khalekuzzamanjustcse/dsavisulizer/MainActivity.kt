package com.khalekuzzamanjustcse.dsavisulizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.khalekuzzamanjustcse.dsavisulizer.ui.theme.DSAVisulizerTheme
import com.khalekuzzamanjustcse.tree_visualization.visualizer.TreeVisualizerPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DSAVisulizerTheme {
                //DraggableElementPreview()
               // HighLightLine()
               // SelectionSortCodeExecution()
               // PageReplacement()
               // LayoutTreeKnuthPreview()
             //   LayoutShanonLayoutPreview()
                TreeVisualizerPreview()
            }
        }
    }
}
