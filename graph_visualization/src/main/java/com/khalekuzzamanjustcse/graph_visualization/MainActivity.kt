package com.khalekuzzamanjustcse.graph_visualization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.khalekuzzamanjustcse.graph_visualization.ui.theme.DSAVisulizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DSAVisulizerTheme {
              
            }
        }
    }
}



@Preview
@Composable
fun GreetingPreview() {
   Text(text = "Hello Graph")
}