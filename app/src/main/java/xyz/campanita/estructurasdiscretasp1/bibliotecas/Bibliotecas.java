package xyz.campanita.estructurasdiscretasp1.bibliotecas;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Bibliotecas {
    public static Document d;
    public static InputStream descargarLista(String uri) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(uri);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "EstructurasDiscretas/pre0.1");
        return urlConnection.getInputStream();
    }
}
