package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Variable(
    val name: String,
    val value: String? = null
)

@Preview
@Composable
private fun ShowVariablesPreview() {
    var variables by remember {
        mutableStateOf(
            listOf(
                Variable(name = "i", value = "2"),
                Variable(name = "j"),
                Variable(name = "k"),
            )
        )
    }
    Column {
        Button(onClick = {
            val updatedVariables = variables.toMutableList()
            updatedVariables[1] = updatedVariables[1].copy(value = "66")
            variables = updatedVariables
        }) {
            Text(text = "Update")
        }
        VariablesStates(variables = variables)
    }


}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VariablesStates(
    modifier: Modifier = Modifier,
    variables: List<Variable>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(color = Color.Black, width = 1.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentSize(),
            text = "Active Variables",
        )

        FlowRow(
            modifier = Modifier
                .padding(8.dp)
        ) {
            variables.forEach { variable ->
                if (variable.value != null) {
                    VariableShow(
                        cellSize = 50.dp,
                        variableName = variable.name,
                        value = variable.value
                    )
                }
            }
        }

    }


}

@Composable
fun VariableShow(
    modifier: Modifier = Modifier,
    variableName: String,
    value: String,
    fontSize: TextUnit = 16.sp,
    cellSize: Dp = Dp.Unspecified
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .size(cellSize)

    ) {

        Box(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Black)

        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                text = value,
                fontSize = fontSize,
            )
        }
        Text(
            text = variableName,
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
        )


    }


}
