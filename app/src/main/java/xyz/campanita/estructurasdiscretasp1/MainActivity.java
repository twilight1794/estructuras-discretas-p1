package xyz.campanita.estructurasdiscretasp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.StrictMode;
import android.widget.Button;

import org.apache.commons.io.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Bibliotecas;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Button button = findViewById(R.id.mostrar_todos_l);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListaRecursosActivity.class)));

        button = findViewById(R.id.mostrar_todos_t);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListaTemasActivity.class)));

        button = findViewById(R.id.mostrar_favoritos);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavoritosActivity.class)));

        button = findViewById(R.id.preferencias);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AjustesActivity.class)));

        button = findViewById(R.id.acercade);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AcercaDeActivity.class)));

        binding.fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BusquedaActivity.class)));

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        File file = new File(getExternalFilesDir(null), "latest.xml");
        if (!file.exists() || file.length() == 0) {
            String uri = prefs.getString("ubicacion_indice", "https://raw.githubusercontent.com/twilight1794/estructuras-discretas-p1/main/indices/latest.xml");

            try (OutputStream outputStream = new FileOutputStream(file)) {
                IOUtil.copy(Bibliotecas.descargarLista(uri), outputStream);
            } catch (Exception e) {
                AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setTitle(R.string.error_descarga_t);
                a.setMessage(R.string.error_descarga);
                a.setPositiveButton(android.R.string.ok, (dialog, which) -> MainActivity.super.onBackPressed());
                a.setCancelable(false);
                a.create();
                a.show();
            }
        }
    }
}

