package xyz.campanita.estructurasdiscretasp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.widget.Button;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import xyz.campanita.estructurasdiscretasp1.Bibliotecas.Bibliotecas;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Button button = findViewById(R.id.mostrar_todos_l);
        button.setOnClickListener(v -> Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        button = findViewById(R.id.mostrar_todos_t);
        button.setOnClickListener(v -> Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        button = findViewById(R.id.preferencias);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AjustesActivity.class)));

        button = findViewById(R.id.acercade);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AcercaDeActivity.class)));

        binding.fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BusquedaActivity.class)));

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("es_primera_vez", true)) {
            prefs.edit().putBoolean("es_primera_vez", false).apply();
            File file = new File(getFilesDir(), "latest.xml");
            if (!file.exists()) {
                String uri = prefs.getString("ubicacion_indice", "");
                Bibliotecas b = new Bibliotecas();
                InputStream inputStream = b.descargarLista(uri);

                try (OutputStream outputStream = new FileOutputStream(file)) {
                    IOUtils.copy(inputStream, outputStream);
                } catch (Exception e) {
                    AlertDialog.Builder a = new AlertDialog.Builder(this);
                    a.setTitle(R.string.error_descarga_t);
                    a.setMessage(R.string.error_descarga);
                    a.setPositiveButton(android.R.string.ok, (dialog, which) -> {});
                    a.setCancelable(false);
                    a.create();
                    a.show();
                }
            }
        }
    }
}

