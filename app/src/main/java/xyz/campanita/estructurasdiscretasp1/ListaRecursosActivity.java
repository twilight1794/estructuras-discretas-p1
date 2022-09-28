package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaRecursosBinding;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaTemasBinding;

public class ListaRecursosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaRecursosBinding binding = ActivityListaRecursosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}