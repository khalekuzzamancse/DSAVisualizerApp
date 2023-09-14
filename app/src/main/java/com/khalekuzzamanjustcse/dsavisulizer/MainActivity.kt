package com.khalekuzzamanjustcse.dsavisulizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms.fifo.PageReplacement
import com.khalekuzzamanjustcse.dsavisulizer.selection_sort.SelectionSortCodeExecution
import com.khalekuzzamanjustcse.dsavisulizer.ui.theme.DSAVisulizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DSAVisulizerTheme {
                //DraggableElementPreview()
               // HighLightLine()
               // SelectionSortCodeExecution()
                PageReplacement()
            }
        }
    }
}
