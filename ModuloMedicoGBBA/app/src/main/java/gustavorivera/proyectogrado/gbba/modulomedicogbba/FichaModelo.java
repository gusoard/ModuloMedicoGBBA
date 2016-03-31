package gustavorivera.proyectogrado.gbba.modulomedicogbba;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gustavo on 23/03/16.
 */
public class FichaModelo implements Serializable {

    private Integer spo2;
    private Double altura;
    private Integer ppm;
    private String fecha;
    private boolean nuevo = true;
    private ArrayList<Float> ecg = new ArrayList<>();
    private ArrayList<Float> espirometria = new ArrayList<>();


    public ArrayList<Float> getEspirometria() {
        return espirometria;
    }

    public void setEspirometria(ArrayList<Float> espirometria) {
        this.espirometria = espirometria;
    }


    public Integer getSpo2() {
        return spo2;
    }

    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Integer getPpm() {
        return ppm;
    }

    public void setPpm(Integer ppm) {
        this.ppm = ppm;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public ArrayList<Float> getEcg() {
        return ecg;
    }

    public void setEcg(ArrayList<Float> ecg) {
        this.ecg = ecg;
    }
}