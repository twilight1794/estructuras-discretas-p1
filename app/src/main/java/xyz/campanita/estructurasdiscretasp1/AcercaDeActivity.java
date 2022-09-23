package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

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