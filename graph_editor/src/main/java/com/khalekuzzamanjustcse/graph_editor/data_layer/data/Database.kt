package com.khalekuzzamanjustcse.graph_editor.data_layer.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphEditorDao{
    @Query("SELECT *FROM EditorNodeEntity")
    fun getEditorNode(): Flow<List<EditorNodeEntity>>
    @Query("SELECT *FROM EditorEdgeEntity")
    fun getEditorEdge(): Flow<List<EditorEdgeEntity>>
    @Insert
    fun insertNode(nodes:List<EditorNodeEntity>)
    @Insert
    fun insertEdge(nodes:List<EditorEdgeEntity>)

    @Query("DELETE FROM EditorNodeEntity")
    fun deleteAllNode()
    @Query("DELETE FROM EditorEdgeEntity")
    fun deleteAllEdge()

}
@Database(
    version =1,
    entities = [
        EditorNodeEntity::class,EditorEdgeEntity::class,
    ],
    exportSchema = false
)
abstract class GraphEditorDatabase:RoomDatabase(){
    abstract  val dao:GraphEditorDao
}
 fun createDao(context: Context)=Room
         .databaseBuilder(context,GraphEditorDatabase::class.java,name ="GraphEditorDB")
         .build()
         .dao
