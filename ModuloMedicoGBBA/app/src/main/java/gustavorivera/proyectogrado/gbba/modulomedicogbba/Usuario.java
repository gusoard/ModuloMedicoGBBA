package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gustavo on 24/03/16.
 */
public class Usuario implements Serializable {

    private String mNombre;
    private String mCedula;

    private String mGrupoSang;

    private ArrayList<FichaModelo> mFichas;

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public String getCedula() {
        return mCedula;
    }

    public void setCedula(String cedula) {
        mCedula = cedula;
    }

    public String getGrupoSang() {
        return mGrupoSang;
    }

    public void setGrupoSang(String grupoSang) {
        mGrupoSang = grupoSang;
    }

    public ArrayList<FichaModelo> getFichas() {
        return mFichas;
    }

    public void setFichas(ArrayList<FichaModelo> fichas) {
        mFichas = fichas;
    }
}
