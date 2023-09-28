package com.khalekuzzamanjustcse.graph_visualization.ui_layer.screen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.khalekuzzamanjustcse.common_ui.CommonScreenLayout


class GraphTraversalScreensViewModel : ViewModel()

@Composable
fun GraphTraversalScreen(
    currentScreen: @Composable (PaddingValues) -> Unit,
) {
    Log.i("RecompositionScreen", ":MainScreen")
    CommonScreenLayout {
        currentScreen(it)
    }

}