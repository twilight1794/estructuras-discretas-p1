package xyz.campanita.estructurasdiscretasp1.bibliotecas;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

import xyz.campanita.estructurasdiscretasp1.BuildConfig;
import xyz.campanita.estructurasdiscretasp1.R;

public class Comun {
    public static LinkedList<String> temasFiltrados = new LinkedList<>();
    public static LinkedList<Recurso> recursos;
    public static LinkedList<String> favoritos;
    public static LinkedList<String> resultados = new LinkedList<>();
    // Titulo, autor, tema, editorial, código
    public static boolean[] opciones = new boolean[5];
    public static String consulta = "";

    public static String userAgent = "EstructurasDiscretas/" + BuildConfig.VERSION_CODE;
    public static Biblioteca[] consultables = { Biblioteca.DOVALI, Biblioteca.RIVERO, Biblioteca.ENZO, Biblioteca.PREPA1, Biblioteca.PREPA5, Biblioteca.CCHOR, Biblioteca.CENTRAL };

    public enum Fuente {
        FAVORITOS,
        LISTA_RECURSOS,
        BUSQUEDA
    }

    public static void inicializar(Context context) throws IOException {
        if (favoritos == null){
            cargarFavoritos(context);
        }
        if (recursos == null){
            cargarJSON(context);
        }
    }

    // Crear diálogos de error fatal
    public static AlertDialog crearDialogoFatal(Context context, int mensaje){
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setTitle(R.string.error_descarga_t);
        a.setMessage(mensaje);
        a.setPositiveButton(android.R.string.ok, (dialog, which) -> ((AppCompatActivity) context).onBackPressed());
        a.setCancelable(false);
        return a.create();
    }

    // Enlaces
    public static String getURIBusqueda(String isbn, Biblioteca b){
        String uri;
        switch (b) {
            case CENTRAL:
                uri = "https://catalogos.bibliotecacentral.unam.mx/F/?func=find-a&local_base=L1001&filter_code_1=WLN&filter_request_1=&find_code=WIS&request=%s"; break;
            case DOVALI: //+ branch%3AL875
                uri = "https://fiantoniodovalijaime.bibliotecas.unam.mx:81/cgi-bin/koha/opac-search.pl?idx=&q=%s&branch_group_limit="; break;
            case RIVERO:
                uri = "https://fienriqueriveroborrell.bibliotecas.unam.mx:81/cgi-bin/koha/opac-search.pl?idx=nb&q=%s&branch_group_limit=branch%3ALD150"; break;
            case ENZO:
                uri = "https://fienzolevi.bibliotecas.unam.mx:81/cgi-bin/koha/opac-search.pl?idx=nb&q=%s&branch_group_limit=branch%3AL8950"; break;
            case PREPA1:
                uri = "https://biblioenp5.bibliotecas.unam.mx:81/cgi-bin/koha/opac-search.pl?idx=nb&q=%s&branch_group_limit="; break;
            case PREPA5:
                uri = "https://biblioenp1.bibliotecas.unam.mx:81/cgi-bin/koha/opac-search.pl?idx=&q=%s&branch_group_limit="; break;
            case CCHOR:
                uri = "https://cch-oriente.bibliotecas.unam.mx:81/cgi-bin/koha/opac-search.pl?idx=nb&q=%s&branch_group_limit="; break;
            default:
                uri = "";
        }
        return uri.replace("%s", isbn);
    }

    public static void cargarJSON(Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(null), "datos.json");
        if (!file.exists() || file.length() == 0) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            URL url = new URL(prefs.getString("ubicacion_indice", "https://raw.githubusercontent.com/twilight1794/estructuras-discretas-p1/main/indices/latest.json"));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", Comun.userAgent);
            IOUtils.copy(urlConnection.getInputStream(), new FileOutputStream(file));
        }
        if (file.exists() && file.length() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            Recurso[] pojos = objectMapper.readValue(file, Recurso[].class);
            recursos = new LinkedList<>(Arrays.asList(pojos));
        }
    }

    public static void cargarFavoritos(Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(null), "favs.json");
        if (!file.exists() || file.length() == 0){
            favoritos = new LinkedList<>();
            escribirFavoritos(context);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] pojos = objectMapper.readValue(file, String[].class);
            favoritos = new LinkedList<>(Arrays.asList(pojos));
        }
    }

    public static void escribirJSON(Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(context.getExternalFilesDir(null), "datos.json"), Comun.recursos);
    }

    public static void escribirFavoritos(Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(context.getExternalFilesDir(null), "favs.json"), Comun.favoritos);
    }
}
