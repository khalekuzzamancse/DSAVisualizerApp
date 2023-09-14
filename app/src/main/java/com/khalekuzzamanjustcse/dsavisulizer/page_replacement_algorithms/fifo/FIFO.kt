package com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms.fifo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.khalekuzzamanjustcse.dsavisulizer.PageInput
import com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms.PageLoad
import com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms.PageReplacementAlgorithms
import com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms.ReplacementType
import com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms.VisualMemory

@Preview
@Composable
fun PageReplacementPreview(){
    PageReplacement()
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PageReplacement() {
    var loadPage by remember {
        mutableStateOf(PageLoad(-1, -1))
    }
    var memoryCapacity by remember {
        mutableStateOf(4)
    }
    var pageRequests by remember {
        mutableStateOf(emptyList<Int>())
    }
    PageInput{cellNo,list->
        memoryCapacity=cellNo
        pageRequests=list
    }

    var states by remember {
        mutableStateOf(
            PageReplacementAlgorithms(pageRequests, memoryCapacity)
                .getState(ReplacementType.FIFO)
        )
    }

    var i by remember {
        mutableStateOf(0)
    }

    if(pageRequests.isNotEmpty()){

        Column {
            FlowRow {
                MyButton(text = "FIFO") {
                    states = PageReplacementAlgorithms(pageRequests, memoryCapacity)
                        .getState(ReplacementType.FIFO)
                }
                MyButton(text = "LRU") {
                    states = PageReplacementAlgorithms(pageRequests, memoryCapacity)
                        .getState(ReplacementType.LRU)
                }
                MyButton(text = "MRU") {
                    states = PageReplacementAlgorithms(pageRequests, memoryCapacity)
                        .getState(ReplacementType.MRU)
                }
                MyButton(text = "OPTIMAL") {
                    states = PageReplacementAlgorithms(pageRequests, memoryCapacity)
                        .getState(ReplacementType.OPTIMAL)
                }

            }
            Button(onClick = {
                if (i <= pageRequests.size - 1) {
                    loadPage = PageLoad(
                        memoryCellNo = states[i].replaceMemoryCell,
                        pageRequestNo = states[i].loadedPageRequestNo
                    )
                    i++
                }
            }) {
                Text(text = "Load")
            }
            VisualMemory(
                memoryCapacity = memoryCapacity,
                pageRequests = pageRequests,
                loadPage = loadPage
            )
        }
    }


}

@Composable
private fun MyButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text(text = text)
    }

}