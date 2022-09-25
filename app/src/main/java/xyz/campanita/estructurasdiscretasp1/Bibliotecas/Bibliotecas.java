package xyz.campanita.estructurasdiscretasp1.Bibliotecas;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Bibliotecas {
    public InputStream descargarLista(String uri) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "EstructurasDiscretas/pre0.1");
            return new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception ignored) {
        } finally {
            if (urlConnection != null){ urlConnection.disconnect(); }
        }
        return null;
    }
}
