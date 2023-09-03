package com.khalekuzzamanjustcse.dsavisulizer.postion2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
Concept Used:
*Relation among;Position In Root, Position in Parent,Position in Window
*Transformation of origin(Coordinate geometry)
*find the same position coordinate   with respect two different origin

 */
@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PPP() {
    val cellWidth = 100.dp
    val density = LocalDensity.current.density
    val padding = 8.dp
    val paddingPx =(padding.value * density).toInt()

    val n = 5
    var cellPosition by remember {
        mutableStateOf(listOf<Offset>())
    }
    var offset by remember {
        mutableStateOf(mapOf<Int, IntOffset>())
    }

    fun getIntOffset(offset: Offset): IntOffset {
        return IntOffset(offset.x.toInt(), offset.y.toInt())
    }


    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
        ) {
            for (i in 1..n) {
                Box(modifier = Modifier
                    .size(cellWidth)
                    .border(color = Color.Black, width = 2.dp)
                    .padding(8.dp)
                    .onGloballyPositioned {
                        val tempCell = cellPosition.toMutableList()
                        tempCell.add(it.positionInParent())
                        cellPosition = tempCell
                    })
            }

        }

        for (i in 1..n) {
            Box(
                modifier = Modifier
                    .size(cellWidth - 16.dp)
                    .offset {
                        offset[i - 1] ?: IntOffset.Zero
                    }
                    .background(color = Color.Blue)
                    .onGloballyPositioned {
                        val temp = offset.toMutableMap()
                        temp[i - 1] =
                            (temp[i - 1]?.minus(getIntOffset(it.positionInParent()))
                                ?: IntOffset.Zero) + getIntOffset(cellPosition[i - 1])+
                                    IntOffset(paddingPx,paddingPx)
                        offset = temp

                    }
            ) {
                Text(
                    text = "$i",
                    style = TextStyle(color = Color.White, fontSize = 16.sp),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }


    }


}

