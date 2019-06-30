package gustavorivera.proyectogrado.gbba.modulomedicogbba.model;

import android.content.ContentValues;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by gustavo on 24/03/16.
 */
public class Usuario implements Serializable, BaseModel {

    private int id = -1;
    private String mNombre;
    private String mCorreo;
    private String mCedula;
    private String mGrupoSang;
    private Long mUltimoIngreso;

    //region " Getters and setters "
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

    public String getCorreo() { return mCorreo; }
    public void setCorreo(String correo) { mCorreo = correo; }

    public Long getUltimoIngreso() { return mUltimoIngreso; }
    public void setUltimoIngreso(Long UltimoIngreso) { mUltimoIngreso = UltimoIngreso; }

    //endregion

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int i) {
        id = i;
    }

    @NotNull
    @Override
    public String getTableName() {
        return ConstantsKt.getUSUARIOS_TABLE_NAME();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if (id > 0) values.put(ConstantsKt.getUSUARIOS_ID(), this.id);
        values.put(ConstantsKt.getUSUARIOS_NAME(), this.mNombre);
        values.put(ConstantsKt.getUSUARIOS_DOC_IDENTIDAD(), this.mCedula);
        values.put(ConstantsKt.getUSUARIOS_ULT_ING(), this.mUltimoIngreso);
        values.put(ConstantsKt.getUSUARIOS_GRUPO_SANG(), this.mGrupoSang);

        return values;
    }


}
