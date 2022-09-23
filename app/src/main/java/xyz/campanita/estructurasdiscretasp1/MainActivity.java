package xyz.campanita.estructurasdiscretasp1;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Button button = (Button) findViewById(R.id.mostrar_todos_l);
        button.setOnClickListener(v -> Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        button = (Button) findViewById(R.id.mostrar_todos_t);
        button.setOnClickListener(v -> Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        button = (Button) findViewById(R.id.preferencias);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AjustesActivity.class)));

        button = (Button) findViewById(R.id.acercade);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AcercaDeActivity.class)));

        binding.fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BusquedaActivity.class)));
    }
}