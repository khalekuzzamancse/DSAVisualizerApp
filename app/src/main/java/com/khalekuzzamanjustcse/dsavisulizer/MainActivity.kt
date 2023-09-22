package com.khalekuzzamanjustcse.dsavisulizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import com.khalekuzzamanjustcse.common_ui.queue_queue.StackQueuePreview
import com.khalekuzzamanjustcse.dsavisulizer.ui.theme.DSAVisulizerTheme
import com.khalekuzzamanjustcse.graph_visualization.GraphBuilderPreview

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
                //TreeVisualizerPreview()
                Column() {
                    StackQueuePreview()
                    //     GraphBuilderPreview()
                }

            }
        }
    }
}
