package xyz.campanita.estructurasdiscretasp1.bibliotecas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/*
 * @author: @emi-cruz
 */
public class Recurso {
    private TipoRecurso tipo;
    private ArrayList<String> isbn = new ArrayList<>();
    private String titulo;
    private String subtitulo;
    private String descripcion;
    private String datosPublicacion;
    private final ArrayList<String> tema = new ArrayList<>();
    private final ArrayList<Nodo> temaAsignatura = new ArrayList<>();
    private String urlPortada;
    private final ArrayList<Colaborador> colaborador = new ArrayList<>();
    private int calificacion;
    private int votos;
    private final HashMap<Biblioteca, Existencia> existencias = new HashMap<>();
    private Date ultimaAct;
    // Especial Biblioteca Central
    private String uriExistCentral;

    public TipoRecurso getTipo(){ return tipo; }
    public void setTipo(TipoRecurso tipo) { this.tipo = tipo; }

    public ArrayList<String> getIsbn() {
        return isbn;
    }
    public void setIsbn(String[] isbn) {
        this.isbn = new ArrayList<>(Arrays.asList(isbn));
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDatosPublicacion() { return datosPublicacion; }
    public void setDatosPublicacion(String datosPublicacion) { this.datosPublicacion = datosPublicacion;  }

    public ArrayList<String> getTemas() { return tema; }
    public void setTema(String tema) { this.tema.add(tema); }

    public ArrayList<Nodo> getTemasAsignatura() { return temaAsignatura; }
    public void setTemaAsignatura(int indice, int subindice, boolean explicado, String pagina){
        if (subindice == 0){
            // Tema de primer orden
            temaAsignatura.add(indice - 1, new Nodo(explicado, pagina));
        } else {
            // Tema de segundo orden
            temaAsignatura.get(indice - 1).setSubtema(subindice - 1, new Nodo(explicado, pagina));
        }
    }

    public String getUrlPortada() { return urlPortada; }
    public void setUrlPortada(String urlPortada) { this.urlPortada = urlPortada; }

    public ArrayList<Colaborador> getColaborador() { return colaborador; }
    public void setColaborador(TipoColaborador t, String n) { this.colaborador.add(new Colaborador(t, n)); }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public int getVotos() { return votos; }
    public void setVotos(int votos) { this.votos = votos; }

    public HashMap<Biblioteca, Existencia> getExistencias() { return existencias; }
    public Existencia getExistencia(Biblioteca b){ return existencias.get(b); }
    public void setExistencia(Biblioteca b, String i, int e, int d, String c, ArrayList<Date> f) { this.existencias.put(b, new Existencia(i, e, d, c, f)); }

    public Date getUltimaAct() { return ultimaAct; }
    public void setUltimaAct(Date ultimaAct) { this.ultimaAct = ultimaAct; }

    public String getUriExistCentral() {return uriExistCentral; }
    public void setUriExistCentral(String uriExistCentral) { this.uriExistCentral = uriExistCentral; }
}



