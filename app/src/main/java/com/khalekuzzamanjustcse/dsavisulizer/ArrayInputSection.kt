package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrayInputComposable(
    modifier: Modifier=Modifier,
    onInputChanged:(List<Int>)->Unit,
) {
    var text by remember {
        mutableStateOf("60 50 40 30 20 10")
    }
    var start by remember { mutableStateOf(false) }
    var inputDisable by remember { mutableStateOf(false) }
    var list by remember {
        mutableStateOf(emptyList<Int>())
    }

    ElevatedCard(
        shape = RectangleShape,
        modifier = modifier.padding(4.dp).
        border(width =1.dp, Color.Black)

    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            value = text,
            onValueChange = { text = it },
            readOnly = inputDisable
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val elements = text.split("[,\\s]+".toRegex())
            list = elements.mapNotNull { it.toIntOrNull() }
            start=!start
            onInputChanged(list)
            inputDisable=true
        }) {
            Text("Done")
        }

    }
}