package com.khalekuzzamanjustcse.dsavisulizer.postion2

import android.icu.text.Transliterator.Position
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun PositionShift() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // First Box (blue)
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(color = Color.Black, width = 2.dp)
                .padding(8.dp)
                .onGloballyPositioned {

                }
        )

        // Red Box (last)
        Box(
            modifier = Modifier
                .size(98.dp)
                .border(color = Color.Red, width = 2.dp)
                .padding(8.dp)
        )
    }
}
