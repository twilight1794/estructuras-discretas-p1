package xyz.campanita.estructurasdiscretasp1;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import xyz.campanita.estructurasdiscretasp1.Bibliotecas.PersistenciaDatos;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityBusquedaBinding;

public class BusquedaActivity extends AppCompatActivity implements FiltroTemasDialogFragment.FiltroTemasDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityBusquedaBinding binding = ActivityBusquedaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.busqueda, menu);

        MenuItem searchItem = menu.findItem(R.id.acc_buscar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acc_buscar:
                return true;
            case R.id.acc_listar:
                FiltroTemasDialogFragment dialogFragment = new FiltroTemasDialogFragment();
                dialogFragment.show(getSupportFragmentManager(),"filtro_temas");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Relanzar la busqueda
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Nada
    }
}