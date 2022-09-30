package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityFavoritosBinding;

public class FavoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFavoritosBinding binding = ActivityFavoritosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}