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

data class Node(
    val id: String,
    val color: Color = Color.Red,
    val density: Float,
    val text: String,
    val topLeft: Offset = Offset.Zero,
    val minNodeSize: Dp = 50.dp,
     val radius: Dp = 20.dp,
    private val dragEnabled: Boolean = false
) {
    fun isInsideCircle(touchPosition: Offset): Boolean {
        val minX = topLeft.x
        val minY = topLeft.y
        val shapeSize = minNodeSize
        val maxX = minX + shapeSize.value * density
        val maxY = minY + shapeSize.value * density
        return touchPosition.x in minX..maxX && touchPosition.y in minY..maxY
        // return center.minus(touchPosition).getDistanceSquared() <= radiusPx * radiusPx
    }


    fun enableEdit(touchPosition: Offset): Node {
        return if (isInsideCircle(touchPosition))
            this.copy(dragEnabled = true)
        else this
    }

    fun disableEdit(): Node {
        return this.copy(dragEnabled = false, color = Color.Red)
    }

    fun updateCenter(amount: Offset): Node {
        var (x, y) = topLeft + amount
        if (x < 0f)
            x = 0f
        if (y < 0f)
            y = 0f
        return if (dragEnabled) {
            //  this.copy(center = center + amount, color = Color.Green)
            this.copy(topLeft =Offset(x, y), color = Color.Green)
        } else this
    }

}

fun DrawScope.drawNode(
    node: Node,
    measurer: TextMeasurer,
) {
    val measuredText = measurer.measure(node.text)
    val textHeightPx = measuredText.size.height * 1f
    val textWidthPx = measuredText.size.width * 1f

    val textMaxSizePx = maxOf(textHeightPx, textWidthPx)
    val radius = (maxOf(node.minNodeSize.toPx(), textMaxSizePx) / 2)
    translate(
        left = node.topLeft.x,
        top = node.topLeft.y
    ) {
        drawCircle(
            color = node.color,
            radius = radius,
            center = Offset(radius, radius)
        )
        drawText(
            text = node.text,
            topLeft = Offset(radius - textWidthPx / 2, radius - textHeightPx / 2),
            textMeasurer = measurer,
            style = TextStyle(
                color = if (node.color.luminance() > 0.5) {
                    Color.Black
                } else {
                    Color.White
                }
            )
        )

    }


}