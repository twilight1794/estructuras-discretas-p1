package xyz.campanita.estructurasdiscretasp1;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    ExecutorService mExecutor;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mExecutor = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
        inicializar(this);
    }

    public interface OnProcessedListener {
        void onProcessed(int estado);
    }

    private void inicializar(final AppCompatActivity context){
        final RecursoActivity.OnProcessedListener listener = estado -> mHandler.post(() -> {
            if (estado != 0){
                AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setTitle(R.string.error_descarga_t);
                a.setMessage(R.string.error_descarga);
                a.setPositiveButton(android.R.string.ok, (dialog, which) -> context.finish()/*SplashActivity.super.onBackPressed()*/);
                a.setCancelable(false);
                a.create();
                a.show();
            } else {
                context.startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            mExecutor.shutdown();
        });

        Runnable backgroundRunnable = () -> {
            try {
                Comun.inicializar(context);
                listener.onProcessed(0);
            } catch (IOException e) {
                e.printStackTrace();
                listener.onProcessed(1);
            }
        };
        mExecutor.execute(backgroundRunnable);
    }
}