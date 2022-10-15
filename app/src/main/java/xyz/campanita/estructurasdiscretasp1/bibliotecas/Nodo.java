package xyz.campanita.estructurasdiscretasp1.bibliotecas;

import java.util.ArrayList;

/*
 * @author: @emi-cruz
 */
public class Nodo {
    private ArrayList<Nodo> subtemas;
    private boolean explicado;
    String pagina;

    public Nodo(){}

    public Nodo(boolean bool, String pag){
        explicado = bool;
        pagina = pag;
    }

    public ArrayList<Nodo> getSubtemas() { return subtemas; }
    public void setSubtema(int pos, Nodo nodoHijo){
        if (subtemas == null){
            subtemas = new ArrayList<>();
        }
        subtemas.add(pos, nodoHijo);
    }

    public boolean getExplicado(){ return this.explicado; }
    public void setExplicado(boolean bool){ this.explicado = bool; }

    public String getPagina(){ return this.pagina; }
    public void setPagina(String pag){ this.pagina = pag; }
}