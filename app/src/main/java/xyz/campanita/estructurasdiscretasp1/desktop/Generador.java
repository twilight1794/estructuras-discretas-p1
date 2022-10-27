package xyz.campanita.estructurasdiscretasp1.desktop;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Biblioteca;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Recurso;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.TipoColaborador;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.TipoId;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.TipoRecurso;

public class Generador {
    public static void main(String[] s){
        try {
            Comun.recursos = new LinkedList<>();
            importarExcel();
            try {
                rellenarRecursos();
                escribirJSON("datos.json");
            } catch (Exception e){
                System.out.println("Error");
                escribirJSON("datos_.json");
                throw e;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /*
     * Descarga el indice en Excel y genera objetos Recurso a partir de el
     * @author: @emi-cruz
     */
    public static void importarExcel() throws IOException, InvalidFormatException, EncryptedDocumentException {
        File f = new File("C:/Users/Xivan/", "datos.xlsx");
        XSSFWorkbook libro = new XSSFWorkbook(f);
        XSSFSheet hoja = libro.getSheetAt(0);
        DataFormatter fTexto = new DataFormatter();

        Iterator<Row> filaIterator = hoja.rowIterator();
        int indiceFila = 0;
        while (filaIterator.hasNext()){
            if (indiceFila < 2){
                filaIterator.next();
                indiceFila++;
                continue;
            }
            int temaIn = 0, subtemaIn = 1;
            Recurso r = new Recurso();
            r.setTipo(TipoRecurso.LIBRO);
            XSSFRow fila = (XSSFRow) filaIterator.next();
            Iterator<Cell> columnaIterator = fila.cellIterator();
            int indiceColumna = 0;
            boolean filaValida = true; // A veces, el Excel da por validas filas que realmente estan vacias. Esto indica si realmente es valida una fila
            while (columnaIterator.hasNext()){
                XSSFCell celda = (XSSFCell) columnaIterator.next();
                String val = fTexto.formatCellValue(celda);
                switch (indiceColumna){
                    /* Id */
                    case 0:
                        if (val.isEmpty()) {
                            System.out.println("Fila inválida");
                            filaValida = false;
                        }
                        else {
                            r.setId(val.split(","));
                            System.out.println("" + indiceColumna + ": " + r.getId().get(0) + "("+val+")");
                        }
                        break;
                    /* Tipo */
                    case 1:
                        if (Objects.equals(val, "0")){ r.setTipoId(TipoId.ISBN); }
                        else if (Objects.equals(val, "1")) { r.setTipoId(TipoId.CLASIFICACION); }
                        break;
                    /* Temas de primer orden */
                    case 2:
                        if (!val.isEmpty()) { r.setUrlElectronico(val); }
                        break;
                    case 12: case 13: case 14: case 15: case 16: // 1, 8, 15, 23, 31
                        temaIn++;
                        subtemaIn = 1;
                        switch (val){
                            case "true": case "1":
                                r.setTemaAsignatura(temaIn, 0, true, null);
                                break;
                            case "false": case "0":
                                r.setTemaAsignatura(temaIn, 0, false, null);
                                break;
                            default:
                                r.setTemaAsignatura(temaIn, 0, true, val);
                        }
                        break;
                    /* Existencias en Vasconcelos */
                    case 10: // 38
                        int valIntA = Integer.parseInt(val);
                        if (valIntA>0){ r.setExistencia(Biblioteca.VASCONCELOS, null, valIntA,-1, null, new ArrayList<>()); }
                        break;
                    /* Existencias en la Biblioteca Nacional */
                    case 11: // 39
                        int valIntB = Integer.parseInt(val);
                        if (valIntB > 0) { r.setExistencia(Biblioteca.BNM, null, valIntB,-1, null, new ArrayList<>()); }
                        break;
                    /* Temas de segundo orden */
                    case -1: // En esta edición no se usarán subtemas, pero la función ya está implementada
                        switch (val){
                            case "true":
                                r.setTemaAsignatura(temaIn, subtemaIn, true, null);
                                break;
                            case "false":
                                r.setTemaAsignatura(temaIn, subtemaIn, false, null);
                                break;
                            default:
                                r.setTemaAsignatura(temaIn, subtemaIn, true, val);
                        }
                        subtemaIn++;
                }
                indiceColumna++;
            }
            if (filaValida) {
                Comun.recursos.add(r);
                System.out.println("Ahora hay "+Comun.recursos.size());
                System.out.println("Añadiendo "+r.getId().get(0));
            } else { System.out.println("Saliendo");break; }
            indiceFila++;
        }
    }

    /*
     * Descarga datos de las bibliotecas
     */
    public static void rellenarRecursos() throws IOException {
        // Bibliotecas con un /endpoint/
        OkHttpClient okHttp = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
        ArrayList<Recurso> eliminar = new ArrayList<>();
        for (Recurso r: Comun.recursos){
            System.out.println("Rellenando "+r.getId().get(0));
            boolean existe = false;
            for (Biblioteca b: Comun.consultables){
                String uri = Comun.getURIBusqueda(r.getId().get(0), b);
                Request docreq = new Request.Builder().url(uri).addHeader("User-Agent",Comun.userAgent).get().build();
                Document doc = Jsoup.parse(okHttp.newCall(docreq).execute().body().string());
                System.out.println(Comun.getURIBusqueda(r.getId().get(0), b));
                if (b == Biblioteca.CENTRAL){
                    Element comp = doc.selectFirst("strong:containsOwn(La búsqueda no encontró ninguna coincidencia en los documentos.)");
                    if (comp == null){
                        System.out.println("Encontrado en "+b.name()+"!");
                        existe = true;
                        // Obtener url de existencias
                        System.out.println("Hay "+doc.select("table").size()+" tablas");
                        Element tbl = doc.selectFirst("table");
                        r.setUriExistCentral(tbl.selectFirst("a:containsOwn(Todos los ejemplares)").attr("href"));
                        // Obtener datos generales
                        String datosuri = doc.selectFirst("a:containsOwn(Nombre de etiquetas)").attr("href");
                        Request datosreq = new Request.Builder().url(datosuri).addHeader("User-Agent",Comun.userAgent).get().build();
                        Document datos = Jsoup.parse(okHttp.newCall(datosreq).execute().body().string());
                        Element datosE = datos.selectFirst("table");
                        // Titulo
                        Element valTitulo = datosE.selectFirst("td:containsOwn(Título)");
                        if (valTitulo != null) {
                            r.setTitulo(valTitulo.nextElementSibling().text().split("/")[0]);
                            System.out.println("Titulo!"+r.getTitulo());
                        }
                        // Subtitulo
                        // Descripcion
                        Element valDescripcion = datosE.selectFirst("td:containsOwn(Descripción fisica)");
                        if (valDescripcion != null){
                            r.setDescripcion(valDescripcion.nextElementSibling().text());
                            System.out.println("Descripcion!"+r.getDescripcion());
                        }
                        // DatosPublicacion
                        Element valDatosPub = datosE.selectFirst("td:containsOwn(Datos de publicac.)");
                        if (valDatosPub == null){
                            valDatosPub = datosE.selectFirst("td:containsOwn(Pie de imprenta)");
                        }
                        if (valDatosPub != null){
                            r.setDatosPublicacion(valDatosPub.nextElementSibling().text());
                            System.out.println("DatosPub!"+r.getDatosPublicacion());
                        }
                        // Tema
                        Elements valTemas = datosE.select("td:containsOwn(Materia)");
                        if (!valTemas.isEmpty()){
                            for (Element e: valTemas) {
                                r.setTema(e.nextElementSibling().text().toLowerCase());
                                System.out.println("Temas!" + String.join(",", r.getTemas()));
                            }
                        }
                        // URL portada
                        r.setUrlPortada("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Ghostscript_spots.pdf/page1-543px-Ghostscript_spots.pdf.jpg");
                        // Colaboradores
                        Element valAutor = datosE.selectFirst("td:containsOwn(Autor)");
                        if (valAutor != null){
                            r.setColaborador(TipoColaborador.AUTOR, valAutor.nextElementSibling().text().replace(", autor", ""));
                            System.out.println("Colab!AUTOR," + valAutor.nextElementSibling().text().replace(", autor", ""));
                        }
                        Elements colaboradores = datosE.select("td:containsOwn(Coautor personal)");
                        for (Element c: colaboradores){
                            String cadc = c.nextElementSibling().text();
                            if (cadc.endsWith(", autor")){
                                r.setColaborador(TipoColaborador.AUTOR, cadc.replace(", autor", ""));
                                System.out.println("Colab!AUTOR," + cadc);
                            } else if (cadc.endsWith(", traductor")){
                                r.setColaborador(TipoColaborador.TRADUCTOR, cadc.replace(", traductor", ""));
                                System.out.println("Colab!TRADUCTOR," + cadc);
                            } else if (cadc.endsWith(", editor")){
                                r.setColaborador(TipoColaborador.EDITOR, cadc.replace(", editor", ""));
                                System.out.println("Colab!EDITOR," + cadc);
                            } else {
                                r.setColaborador(TipoColaborador.COLABORADOR, cadc);
                                System.out.println("Colab!COLABORADOR," + cadc);
                            }
                        }
                        // Calificacion y votos
                        r.setCalificacion(0);
                        r.setVotos(0);
                        break;
                    }
                } else {
                    Element comp = doc.selectFirst("strong:containsOwn(No results found!)"); // No se encontraron resultados!
                    if (comp == null){
                        System.out.println("Encontrado en "+b.name()+"!");
                        existe = true;
                        Pattern p2 = Pattern.compile(".*(\\d)\\.(\\d) \\((\\d+) votes\\)", Pattern.MULTILINE);
                        Element datos = doc.selectFirst("#catalogue_detail_biblio>.record");
                        // Titulo
                        Element valTitulo = datos.selectFirst("[property=name]");
                        if (valTitulo != null) {
                            Element elim = valTitulo.selectFirst(".title_resp_stmt");
                            r.setTitulo(valTitulo.text().replace((elim != null)?elim.text():"", "").split("/")[0]);
                            System.out.println("Titulo!"+r.getTitulo());
                        }
                        // Subtitulo
                        // Descripcion
                        Element valDescripcion = datos.selectFirst("[property=description]");
                        if (valDescripcion != null) {
                            r.setDescripcion(valDescripcion.text());
                            System.out.println("Descripcion!"+r.getDescripcion());
                        }
                        // DatosPublicacion
                        Element valDatosPub = datos.selectFirst(".results_summary:has(span:containsOwn(Publisher))");
                        if (valDatosPub != null) {
                            r.setDatosPublicacion(valDatosPub.text().replace("Publisher: ", "").replace(" : ", ": "));
                            System.out.println("Pub!"+r.getDatosPublicacion());
                        }
                        // Tema
                        Elements valTemas = datos.select("[property=keywords]");
                        for (Element a: valTemas){
                            r.setTema(a.text().toLowerCase());
                            System.out.println("Tema!"+a.text());
                        }
                        // URL portada
                        Element valURLP = doc.selectFirst("#bookcover img");
                        if (valURLP != null) {
                            r.setUrlPortada(valURLP.attr("src"));
                            System.out.println("Portada!"+r.getUrlPortada());
                        } else {
                            r.setUrlPortada("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Ghostscript_spots.pdf/page1-543px-Ghostscript_spots.pdf.jpg");
                        }
                        // Colaboradores
                        Elements valColaboradores = datos.select("h5.author a");
                        for (Element c: valColaboradores){
                            Element n = c.selectFirst("[resource=\"#record\"]");
                            Element t = c.selectFirst(".relatorCode");
                            TipoColaborador tc;
                            String tt = (t != null)?t.text():"";
                            switch (tt){
                                case "[autor]":
                                    tc = TipoColaborador.AUTOR; break;
                                case "[editor]":
                                    tc = TipoColaborador.EDITOR; break;
                                case "[traductor]":
                                    tc = TipoColaborador.TRADUCTOR; break;
                                default:
                                    tc = TipoColaborador.COLABORADOR;
                            }
                            r.setColaborador(tc, n.text());
                            System.out.println("Colab!"+ tc.name() + "," + n.text());
                        }
                        // Calificacion y votos
                        Element valRating = doc.selectFirst("#rating_text");
                        if (valRating != null) {
                            Matcher m2 = p2.matcher(valRating.text());
                            m2.find();
                            r.setCalificacion(Integer.parseInt(m2.group(1))*10 + Integer.parseInt(m2.group(2)));
                            r.setVotos(Integer.parseInt(m2.group(3)));
                            System.out.println("Calif!"+r.getCalificacion());
                            System.out.println("Votos!"+r.getVotos());
                        }
                        break;
                    }
                }
            }
            if (!existe) {
                eliminar.add(r);
                System.out.println("Eliminando "+r.getId().get(0));
            }
        }
        for (Recurso r: eliminar) { Comun.recursos.remove(r); }
    }

    /*
     * Generar JSON con todos los datos de los recursos
     */
    public static void escribirJSON(String nomf) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("C:\\Users\\Xivan", nomf), Comun.recursos);
    }
}
