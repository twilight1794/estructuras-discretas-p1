package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.util.concurrent.Executors;

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

        try {
            Comun.inicializar(this);
        } catch (IOException ignored) {}

        rv = findViewById(R.id.favoritos_cont);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        ra = new RecursoAdapter(Comun.favoritos, Comun.Fuente.FAVORITOS);
        rv.setAdapter(ra);
        actualizarContador(findViewById(R.id.contador));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ra.notifyDataSetChanged();
        actualizarContador(findViewById(R.id.contador));
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

    public void actualizarContador(TextView t){
        t.setText(getString(R.string.x_elementos, Comun.favoritos.size()));
    }
}