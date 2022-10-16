package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityAcercaDeBinding;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityAcercaDeBinding binding = ActivityAcercaDeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Resources r = getResources();

        ((TextView) findViewById(R.id.versionTexto)).setText(r.getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        findViewById(R.id.img_fi).setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ingenieria.unam.mx/"));
            startActivity(in);
        });
        findViewById(R.id.img_unam).setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.unam.mx/"));
            startActivity(in);
        });
        findViewById(R.id.img_github).setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/twilight1794/estructuras-discretas-p1"));
            startActivity(in);
        });
    }
}