package com.khalekuzzamanjustcse.tree_visualization

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

data class SearchResult(
    val currentValue: Int,
    val key: Int,
    val isEqual: String
)

fun searchSequences(list: List<Int>,onKeyChanged:()->Int) = sequence {
    for (value in list) {
        val key=onKeyChanged()
        val isEqual = value == key
        val result = SearchResult(value, key, if (isEqual) "YES" else "NO")
        yield(result)
    }
}

@Preview
@Composable
fun SequenceDemo() {

   var key by remember {
       mutableStateOf(0)
   }
    val onKeyChanged:()->Int={
        key
    }
    val fibonacciIterator by remember {
        mutableStateOf(
            searchSequences(
                list = listOf(1, 2, 3, 4, 5),
                onKeyChanged=onKeyChanged
            ).iterator()
        )
    }

    Column {
        Button(onClick = {
            key++
            Log.i("FibonacciNext", "${fibonacciIterator.next()}")
        }) {
            Text("Next")
        }
    }
}
