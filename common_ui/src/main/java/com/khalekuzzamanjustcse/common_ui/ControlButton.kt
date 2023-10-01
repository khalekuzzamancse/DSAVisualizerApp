package com.khalekuzzamanjustcse.common_ui

import androidx.compose.runtime.MutableState

interface ControlButton {
    val icon: Any
    val label: String
    val enabled: MutableState<Boolean>
    fun onClick()
}
