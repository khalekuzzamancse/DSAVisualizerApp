package com.khalekuzzamanjustcse.graph_editor.edge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import com.khalekuzzamanjustcse.graph_editor.basic_concept_demo.EdgePoint

fun DrawScope.drawEdge(
    edge: GraphEditorVisualEdge,
    textMeasurer: TextMeasurer? = null,
) {
    //drawEdge
    drawPath(path = edge.path, color = edge.pathColor, style = Stroke(3f))
    //draw anchor point
    when (edge.selectedPoint) {
        EdgePoint.Start ->
            drawControlPoints(edge.selectedPointColor, edge.anchorPointRadius, edge.start)

        EdgePoint.End ->
            drawControlPoints(edge.selectedPointColor, edge.anchorPointRadius, edge.end)

        EdgePoint.Control ->
            drawControlPoints(edge.selectedPointColor, edge.anchorPointRadius, edge.pathCenter)

        else -> {}
    }
    //draw edge cost
    if (textMeasurer != null) {
        edge.cost?.let { text ->
            drawEdgeCost(text, textMeasurer, edge.slop, edge.pathCenter)
        }
    }
    if (edge.isDirected) {
        drawArrowHead(edge.pathColor, edge.arrowHeadPosition, edge.end)
    }

}

private fun DrawScope.drawEdgeCost(
    cost: String,
    textMeasurer: TextMeasurer,
    slop: Float,
    pathCenter: Offset
) {
    if(pathCenter!= Offset.Unspecified){
        val textHalfWidth = textMeasurer.measure(cost).size.width / 2
        rotate(slop, pathCenter) {
            drawText(
                text = cost,
                topLeft = pathCenter - Offset(textHalfWidth.toFloat(), 0f),
                textMeasurer = textMeasurer
            )
        }
    }

}

private fun DrawScope.drawControlPoints(color: Color, radius: Dp, center: Offset) {
    drawCircle(color, radius = radius.toPx(), center = center)
}

private fun DrawScope.drawArrowHead(color: Color, arrowHeadPosition: Offset, end: Offset) {
    if (arrowHeadPosition != Offset.Unspecified){
        rotate(30f, end) {
            drawLine(color, start = arrowHeadPosition, end = end, 3f)
        }
        rotate(-30f, end) {
            drawLine(color, arrowHeadPosition, end, 3f)
        }
    }


}

