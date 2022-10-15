package xyz.campanita.estructurasdiscretasp1.bibliotecas;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Existencia {
    private String id;
    private int existencias;
    private int disponibilidad;
    private String catalogo;
    private ArrayList<Date> fechasDevolucion;

    public Existencia(){}

    public Existencia(String id, int existencias, int disponibilidad, String catalogo, ArrayList<Date> fechasDevolucion){
        this.id = id;
        this.existencias = existencias;
        this.disponibilidad = disponibilidad;
        this.catalogo = catalogo;
        this.fechasDevolucion = fechasDevolucion;
        Collections.sort(this.fechasDevolucion);
    }

    public String getId(){ return id; }

    public int getExistencias() {
        return existencias;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public String getCatalogo() {
        return catalogo;
    }

    public ArrayList<Date> getFechasDevolucion() { return fechasDevolucion; }

    @JsonIgnore
    public Date getDevolucionProxima() {
        if (fechasDevolucion.isEmpty()){
            return null;
        } else {
            return fechasDevolucion.get(0);
        }
    }
}
