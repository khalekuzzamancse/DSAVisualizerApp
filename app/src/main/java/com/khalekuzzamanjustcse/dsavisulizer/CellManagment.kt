package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class ArrayCell(
    val position: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val cellSize: Dp,
    val currentElementReference: MutableState<Int?> = mutableStateOf(null),
)