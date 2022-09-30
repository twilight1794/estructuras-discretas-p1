package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaTemasBinding;

public class ListaTemasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaTemasBinding binding = ActivityListaTemasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Crear Lista
        Resources res = getResources();
        ArrayList<String[]> temas = new ArrayList<>();
        String[] t0 = res.getStringArray(R.array.temas);
        for (int i=1; i<=t0.length; i++){
            String[] i0 = new String[2];
            i0[0] = ""+i;
            i0[1] = t0[i-1];
            temas.add(i0);
            String[] t1 = res.getStringArray(res.getIdentifier("temas_"+i, "array", getPackageName()));
            for (int ii=1; ii<=t1.length; ii++){
                String[] i1 = new String[2];
                i1[0] = ""+i+"."+ii;
                i1[1] = t1[ii-1];
                temas.add(i1);
                String[] t2 = res.getStringArray(res.getIdentifier("temas_"+i+"_"+ii, "array", getPackageName()));
                for (int iii=1; iii<=t2.length; iii++){
                    String[] i2 = new String[2];
                    i2[0] = ""+i+"."+ii+"."+iii;
                    i2[1] = t2[iii-1];
                    temas.add(i2);
                }
            }
        }

        // Crear RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.lista_temas_cont);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        TemaAdapter mAdapter = new TemaAdapter(temas);
        mRecyclerView.setAdapter(mAdapter);
    }
}