package gustavorivera.proyectogrado.gbba.modulomedicogbba.model

import android.content.ContentValues

interface BaseModel {
    var id:Int
    val tableName: String
    fun getContentValues() : ContentValues
}