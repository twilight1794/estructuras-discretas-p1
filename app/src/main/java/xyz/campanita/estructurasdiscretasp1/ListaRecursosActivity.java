package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaRecursosBinding;

public class ListaRecursosActivity extends AppCompatActivity {

    private static final String[] myDataSet = {
            "PHP","Javascript","Go","Python","C","C--","C++ - ISO/IEC 14882","C# - ISO/IEC 23270","C/AL","Caché ObjectScript","C Shell","Caml","Candle","Cayenne","CDuce","Cecil","Cel","Cesil","Ceylon","CFML","Cg","Chapel","CHAIN","Charity","Charm","Chef","CHILL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaRecursosBinding binding = ActivityListaRecursosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView mRecyclerView = findViewById(R.id.lista_recursos_cont);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        RecursoAdapter mAdapter = new RecursoAdapter(myDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }
}