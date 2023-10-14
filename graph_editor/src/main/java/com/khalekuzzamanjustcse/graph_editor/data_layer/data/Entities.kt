package com.khalekuzzamanjustcse.graph_editor.data_layer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
class EditorNodeEntity(
    @PrimaryKey val id:String= UUID.randomUUID().toString(),
    val label:String,
    val topLeftX:Float,
    val topLeftY:Float,
    val minNodeSizeDp:Int,
    val radiusDp:Int,
)
@Entity
class EditorEdgeEntity(
   @PrimaryKey val id: String=UUID.randomUUID().toString(),
    val cost:String?,
    val hasDirection:Boolean,
    val startX:Float,
    val startY:Float,
    val endX:Float,
    val endY:Float,
    val controlX:Float,
    val controlY:Float,
)
