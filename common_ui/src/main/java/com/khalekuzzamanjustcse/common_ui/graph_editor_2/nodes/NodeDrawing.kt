package com.khalekuzzamanjustcse.common_ui.graph_editor_2.nodes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun NodeDrawPreview() {
    val textMeasure = rememberTextMeasurer()
    val density = LocalDensity.current.density
    val paddingPx = 8.dp.value * density
    val minNodeSizePx = 40.dp.value * density

    val nodeManager = remember {
        CanvasNodeManagerImp(
            nodePaddingPx = paddingPx,
            nodeMinNodeSizePx = minNodeSizePx
        )
    }

    val nodes = nodeManager.nodes.collectAsState().value
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            MyButton(label = "AddNode") {
                nodeManager.addNode("11")
            }
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                enabled = nodeManager.enabledRemove.collectAsState().value,
                label = "RemoveNode",
                onClick =nodeManager::removeNode
            )
            Spacer(modifier = Modifier.width(4.dp))
            MyButton(
                label = "DragModeOn/Off",
                onClick =nodeManager::flipEditMode
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        nodeManager.onCanvasTap(it)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = nodeManager::onDragStart
                    ) { change, dragAmount ->
                        nodeManager.onDrag(dragAmount)
                    }
                }

        ) {
            nodes.forEach {
                drawNode(node = it, measurer = textMeasure)
            }

        }
    }


}


private fun DrawScope.drawNode(
    node: TouchableNode,
    measurer: TextMeasurer,
) {
    val measuredText = measurer.measure(node.text)
    val textHeightPx = measuredText.size.height * 1f
    val textWidthPx = measuredText.size.width * 1f

    val textMaxSizePx = maxOf(textHeightPx, textWidthPx)
    val radius = (maxOf(node.minNodeSizePx, textMaxSizePx) / 2) + node.paddingPx
    translate(
        left = node.topLeft.x,
        top = node.topLeft.y
    ) {
        drawCircle(
            color = Color.Red,
            radius = radius,
            center = Offset(radius, radius)
        )
        drawText(
            text = node.text,
            topLeft = Offset(radius - textWidthPx / 2, radius - textHeightPx / 2),
            textMeasurer = measurer,
            style = TextStyle(color = Color.White)
        )

    }


}