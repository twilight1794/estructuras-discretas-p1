package xyz.campanita.estructurasdiscretasp1;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Colaborador;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Nodo;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Recurso;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityBusquedaBinding;

public class BusquedaActivity extends AppCompatActivity implements FiltroTemasDialogFragment.FiltroTemasDialogListener {
    
    RecyclerView rv;
    LinearLayoutManager lm;
    RecursoAdapter ra;
    SearchView buscadorCampo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityBusquedaBinding binding = ActivityBusquedaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        try {
            Comun.inicializar(this);
        } catch (IOException ignored) {}

        rv = findViewById(R.id.resultados_cont);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        ra = new RecursoAdapter(Comun.resultados, Comun.Fuente.BUSQUEDA);
        rv.setAdapter(ra);
        actualizarContador(findViewById(R.id.contador));

        findViewById(R.id.opt_titulo).setOnClickListener(v -> { Comun.opciones[0] = ((Chip) v).isChecked(); buscador(Comun.consulta); });
        findViewById(R.id.opt_autor).setOnClickListener(v -> { Comun.opciones[1] = ((Chip) v).isChecked(); buscador(Comun.consulta); });
        findViewById(R.id.opt_tema).setOnClickListener(v -> { Comun.opciones[2] = ((Chip) v).isChecked(); buscador(Comun.consulta); });
        findViewById(R.id.opt_editorial).setOnClickListener(v -> { Comun.opciones[3] = ((Chip) v).isChecked(); buscador(Comun.consulta); });
        findViewById(R.id.opt_codigo).setOnClickListener(v -> { Comun.opciones[4] = ((Chip) v).isChecked(); buscador(Comun.consulta); });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Comun.consulta = intent.getStringExtra(SearchManager.QUERY);
            buscador(Comun.consulta);
        } else if (!Comun.consulta.isEmpty()) {
            buscador(Comun.consulta);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ra.notifyDataSetChanged();
        actualizarContador(findViewById(R.id.contador));
        Log.i("UUU", "Resumed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("UUU", "Creando menu");
        getMenuInflater().inflate(R.menu.busqueda, menu);

        MenuItem searchItem = menu.findItem(R.id.acc_buscar);
        buscadorCampo = (SearchView) searchItem.getActionView();
        buscadorCampo.setIconifiedByDefault(false);
        buscadorCampo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String cad) {
                Log.i("UUU", "Submit");
                buscador(cad);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String cad) {
                return true;
            }
        });
        buscadorCampo.setQuery(Comun.consulta, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.acc_listar) {
            (new FiltroTemasDialogFragment()).show(getSupportFragmentManager(), "filtro_temas");
            return true;
        } else if (item.getItemId() == R.id.acc_ayuda) {
            (new AyudaDialogFragment()).show(getSupportFragmentManager(), "ayuda");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(FiltroTemasDialogFragment dialog) {
        Comun.temasFiltrados.clear();
        Pattern p = Pattern.compile("Tema (\\d(?:\\.\\d)?): .+", Pattern.MULTILINE);
        for (int i = 0; i < dialog.llc.getChildCount(); i++) {
            CheckBox c = (CheckBox) dialog.llc.getChildAt(i);
            if (c.isChecked()){
                Matcher m = p.matcher(c.getText());
                if (m.find()) { Comun.temasFiltrados.add(m.group(1)); Log.i("UUU", "A??adido "+m.group(1)); }
            }
        }
        Log.i("UUU", "Rebuscar por dialogo:"+buscadorCampo.getQuery().toString());
        buscador(buscadorCampo.getQuery().toString());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}

    public void actualizarContador(TextView t){
        t.setText(getString(R.string.x_resultados, Comun.resultados.size()));
    }

    public void buscador(String cad){
        Log.i("UUU", "B??squeda iniciada:"+cad+"!");
        Comun.resultados.clear();
        if (cad.isEmpty()) {
            ra.notifyDataSetChanged();
            actualizarContador(findViewById(R.id.contador));
            return;
        }
        for (Recurso r: Comun.recursos){
            String idp = r.getId().get(0);
            String cadn = cad.toLowerCase(Locale.ROOT);
            Comun.consulta = cadn;
            boolean encontrado = false;
            ArrayList<Nodo> temasAsig = r.getTemasAsignatura();
            for (int i = 0; i < temasAsig.size(); i++){
                encontrado = false;
                Nodo n = temasAsig.get(i);
                if (n.getExplicado()) {
                    String temaIn = String.valueOf(i+1);
                    if (Comun.temasFiltrados.contains(temaIn)) {
                        encontrado = true;
                        break;
                    }
                    /*ArrayList<Nodo> temasAsig2 = n.getSubtemas();
                    for (int ii = 0; ii < temasAsig2.size(); ii++) {
                        encontrado = false;
                        Nodo n2 = temasAsig2.get(ii);
                        if (n2.getExplicado()) {
                            temaIn = String.format("%d.%d", i+1, ii+1);
                            if (Comun.temasFiltrados.contains(temaIn)) {
                                encontrado = true;
                                break;
                            }
                        }
                    }
                    if (encontrado) { break; }*/
                }
            }
            if (!encontrado){ continue; }
            Log.i("UUU", "Si est?? en tema!");

            Log.i("UUU", "");
            if (Comun.opciones[0] && r.getTitulo().toLowerCase(Locale.ROOT).contains(cadn)){
                Comun.resultados.add(idp);
                Log.i("UUU", "Coincidencia encontrada en Titulo");
                continue;
            }
            if (Comun.opciones[1]){
                encontrado = false;
                for (Colaborador c: r.getColaborador()){
                    if (c.getNombre().toLowerCase(Locale.ROOT).contains(cadn)){
                        Comun.resultados.add(idp);
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado) { Log.i("UUU", "Coincidencia encontrada en Colaborador");continue; }
            }
            if (Comun.opciones[2]){
                encontrado = false;
                for (String t: r.getTemas()){
                    if (t.toLowerCase(Locale.ROOT).contains(cadn)){
                        Comun.resultados.add(idp);
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado) { Log.i("UUU", "Coincidencia encontrada en Temas");continue; }
            }
            if (Comun.opciones[3] && r.getDatosPublicacion().toLowerCase(Locale.ROOT).contains(cadn)){
                Comun.resultados.add(idp);
                Log.i("UUU", "Coincidencia encontrada en Editorial");
                continue;
            }
            if (Comun.opciones[4]){
                for (String c: r.getId()){
                    if (c.equals(cadn)){
                        Comun.resultados.add(idp);
                        Log.i("UUU", "ISBN encontrado");
                        break;
                    }
                }
            }
        }

        Log.i("UUU", "B??squeda finalizada!");
        ra.notifyDataSetChanged();
        actualizarContador(findViewById(R.id.contador));
    }
}