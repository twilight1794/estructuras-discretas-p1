package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaRecursosBinding;

public class ListaRecursosActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager lm;
    RecursoAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaRecursosBinding binding = ActivityListaRecursosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.lista_recursos_cont);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        ra = new RecursoAdapter(Comun.recursos, Comun.Fuente.LISTA_RECURSOS);
        rv.setAdapter(ra);
        ((TextView) findViewById(R.id.contador)).setText(getString(R.string.x_elementos, Comun.recursos.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.resto, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.acc_ayuda){
            (new AyudaDialogFragment()).show(getSupportFragmentManager(), "ayuda");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}