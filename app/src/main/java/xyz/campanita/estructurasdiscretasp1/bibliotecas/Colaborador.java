package xyz.campanita.estructurasdiscretasp1.bibliotecas;

public class Colaborador {
    TipoColaborador tipo;
    String nombre;

    public Colaborador(){}

    public Colaborador(TipoColaborador t, String n){
        tipo = t;
        nombre = n;
    }

    public TipoColaborador getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }
}
