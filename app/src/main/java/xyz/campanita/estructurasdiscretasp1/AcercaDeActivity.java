package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityAcercaDeBinding;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityMainBinding;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityAcercaDeBinding binding = ActivityAcercaDeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ImageView im = (ImageView) findViewById(R.id.img_fi);
        im.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ingenieria.unam.mx/"));
            startActivity(in);
        });
        im = (ImageView) findViewById(R.id.img_unam);
        im.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.unam.mx/"));
            startActivity(in);
        });
    }
}