package xyz.campanita.estructurasdiscretasp1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        binding.fab.setOnClickListener(v -> onSearchRequested());
    }
}

