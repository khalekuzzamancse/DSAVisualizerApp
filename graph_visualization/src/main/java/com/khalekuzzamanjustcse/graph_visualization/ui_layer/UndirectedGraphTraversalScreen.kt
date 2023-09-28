package com.khalekuzzamanjustcse.graph_visualization.ui_layer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun UndirectedGraphTraversalScreen(
    list: List<Int>,
    scaffoldPadding: PaddingValues = PaddingValues(0.dp),
) {
    //val nodeSize = 64.dp
//    val sizePx = nodeSize.value * LocalDensity.current.density
//    val graphState = remember {
//        GraphState(sizePx)
//    }

    Log.i("RecompositionScreen", ":UnDirScreen")

    Column(
        modifier = Modifier
            .padding(scaffoldPadding)
    ) {

        Text(list.joinToString(","))
//        CustomDropDownMenu(
//            expanded = graphState.showTraversalMode,
//            options = graphState.traversalOptions,
//            onItemClick = graphState::onTraversalOptionChanged
//        )


//        PopupWithRadioButtons(
//            isOpen = graphState.openNeighboursSelectedPopup,
//            state = graphState.arrayComposableState,
//            onListReOrdered = graphState::onNeighbourOrderSelected
//        )
    }
}
