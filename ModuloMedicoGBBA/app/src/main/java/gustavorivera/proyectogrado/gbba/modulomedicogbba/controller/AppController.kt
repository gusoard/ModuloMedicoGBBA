package gustavorivera.proyectogrado.gbba.modulomedicogbba.controller

import android.app.Application
import gustavorivera.proyectogrado.gbba.modulomedicogbba.Ficha
import gustavorivera.proyectogrado.gbba.modulomedicogbba.data.FichasDatabaseHandler
import gustavorivera.proyectogrado.gbba.modulomedicogbba.model.FichaModelo
import gustavorivera.proyectogrado.gbba.modulomedicogbba.model.Usuario
import java.util.ArrayList

class AppController : Application() {

    private var mFichasDatabaseHandler: FichasDatabaseHandler? = null

    val fichasDatabaseHandler: FichasDatabaseHandler
        get() {
            if (mFichasDatabaseHandler== null) {
                mFichasDatabaseHandler = FichasDatabaseHandler(applicationContext)
            }

            return this.mFichasDatabaseHandler!!
        }

    fun crearFicha(ficha: FichaModelo) {
        //fichasDatabaseHandler.crearRegistro(ficha)
    }

    fun getFichasUsuario(idUsuario: Int): ArrayList<FichaModelo> {
        // fichasDatabaseHandler.fichasUsuarioId(id)
        return arrayListOf()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun historialUsuario(mIdUsuario: Int): Usuario {
        var usuario = Usuario()
        // Return data of User
        return usuario
    }

    companion object {
        val TAG = AppController::class.java.simpleName
        @get:Synchronized
        var instance: AppController? = null
            private set
    }

}