package com.khalekuzzamanjustcse.graph_editor.ui.ui.node

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp



fun DrawScope.drawNode(
    graphEditorNode: GraphEditorNode,
    measurer: TextMeasurer,
) {
    val measuredText = measurer.measure(graphEditorNode.label)
    val textHeightPx = measuredText.size.height * 1f
    val textWidthPx = measuredText.size.width * 1f

    val textMaxSizePx = maxOf(textHeightPx, textWidthPx)
    val radius = (maxOf(graphEditorNode.minNodeSize.toPx(), textMaxSizePx) / 2)
    translate(
        left = graphEditorNode.topLeft.x,
        top = graphEditorNode.topLeft.y
    ) {
        drawCircle(
            color = graphEditorNode.color,
            radius = radius,
            center = Offset(radius, radius)
        )
        drawText(
            text = graphEditorNode.label,
            topLeft = Offset(radius - textWidthPx / 2, radius - textHeightPx / 2),
            textMeasurer = measurer,
            style = TextStyle(
                color = if (graphEditorNode.color.luminance() > 0.5) {
                    Color.Black
                } else {
                    Color.White
                }
            )
        )

    }


}
fun DrawScope.drawNode(
    graphEditorNode: GraphEditorNode,
    padding:Dp=0.dp,
    measurer: TextMeasurer,
) {
    val measuredText = measurer.measure(graphEditorNode.label)
    val textHeightPx = measuredText.size.height * 1f
    val textWidthPx = measuredText.size.width * 1f

    val textMaxSizePx = maxOf(textHeightPx, textWidthPx)
    val radius = (maxOf(graphEditorNode.minNodeSize.toPx(), textMaxSizePx) / 2)+padding.toPx()*2
    translate(
        left = graphEditorNode.topLeft.x,
        top = graphEditorNode.topLeft.y
    ) {
        drawCircle(
            color = graphEditorNode.color,
            radius = radius,
            center = Offset(radius, radius)
        )
        drawText(
            text = graphEditorNode.label,
            topLeft = Offset(radius - textWidthPx / 2, radius - textHeightPx / 2),
            textMeasurer = measurer,
            style = TextStyle(
                color = if (graphEditorNode.color.luminance() > 0.5) {
                    Color.Black
                } else {
                    Color.White
                }
            )
        )

    }

}