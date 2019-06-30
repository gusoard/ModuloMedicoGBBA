package gustavorivera.proyectogrado.gbba.modulomedicogbba.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import gustavorivera.proyectogrado.gbba.modulomedicogbba.model.*
import java.lang.reflect.Type
import java.util.*

class FichasDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO F
    }

    fun <T> crearRegistro(registro: BaseModel) {
        var db:SQLiteDatabase = writableDatabase
        var values = ContentValues()
        values = registro.getContentValues()
        db.insert(registro.tableName, null, values)
    }


    fun getUsuarioLogIn(email : String, docIdentidad : String) : Usuario? {
        var db: SQLiteDatabase = readableDatabase
        var usuario: Usuario? = null
        var cursor: Cursor = db.query(
                USUARIOS_TABLE_NAME,
                arrayOf(USUARIOS_ID,
                        USUARIOS_NAME,
                        USUARIOS_CORREO,
                        USUARIOS_DOC_IDENTIDAD,
                        USUARIOS_ULT_ING,
                        USUARIOS_GRUPO_SANG
                ),
                "$USUARIOS_CORREO =? AND $USUARIOS_DOC_IDENTIDAD =?",
                arrayOf(email, docIdentidad),
                null, null, null, null)

        if (!cursor.isClosed && cursor.moveToFirst()){
            usuario = Usuario()
            usuario.id = cursor.getInt(cursor.getColumnIndex(USUARIOS_ID))
            usuario.nombre = cursor.getString(cursor.getColumnIndex(USUARIOS_NAME))
            usuario.correo = cursor.getString(cursor.getColumnIndex(USUARIOS_CORREO))
            usuario.cedula = cursor.getString(cursor.getColumnIndex(USUARIOS_DOC_IDENTIDAD))
            usuario.ultimoIngreso = cursor.getLong(cursor.getColumnIndex(USUARIOS_ULT_ING))
            usuario.grupoSang = cursor.getString(cursor.getColumnIndex(USUARIOS_GRUPO_SANG))
        }

        cursor.close()
        db.close()
        return usuario
    }

}
