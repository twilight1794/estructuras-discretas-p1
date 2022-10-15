package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityFavoritosBinding;

public class FavoritosActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager lm;
    RecursoAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFavoritosBinding binding = ActivityFavoritosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.favoritos_cont);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        ra = new RecursoAdapter(Comun.favoritos, Comun.Fuente.FAVORITOS);
        rv.setAdapter(ra);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ra.notifyDataSetChanged();
    }
}