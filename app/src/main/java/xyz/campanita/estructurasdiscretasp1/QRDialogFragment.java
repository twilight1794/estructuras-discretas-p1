package xyz.campanita.estructurasdiscretasp1;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class QRDialogFragment extends DialogFragment {
    private static final float[] NEGATIVO = {
            -1.0f,     0,     0,    0, 255, // red
            0, -1.0f,     0,    0, 255, // green
            0,     0, -1.0f,    0, 255, // blue
            0,     0,     0, 1.0f,   0  // alpha
    };
    public static String TAG = "qr";
    public Bitmap qr;
    public String url;
    public String titulo;

    public QRDialogFragment(Bitmap bitmap, String url, String titulo){
        super();
        this.qr = bitmap;
        this.url = url;
        this.titulo = titulo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_qr, null);
        ImageView img = v.findViewById(R.id.imagen_qr);
        int modo = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (modo == Configuration.UI_MODE_NIGHT_YES){
            img.setColorFilter(new ColorMatrixColorFilter(NEGATIVO));
        }
        img.setImageBitmap(qr);
        builder.setView(v);
        builder.setPositiveButton(android.R.string.ok, null );

        v.findViewById(R.id.compartir_trad_qr).setOnClickListener(w -> {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, this.url);
                    sendIntent.putExtra(Intent.EXTRA_TITLE, this.titulo);
                    startActivity(Intent.createChooser(sendIntent, "Compartir recurso"));
                });
        return builder.create();
    }
}