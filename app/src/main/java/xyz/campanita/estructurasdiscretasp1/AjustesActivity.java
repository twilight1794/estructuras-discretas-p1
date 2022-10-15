package xyz.campanita.estructurasdiscretasp1;

import static android.app.UiModeManager.MODE_NIGHT_NO;
import static android.app.UiModeManager.MODE_NIGHT_YES;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;

public class AjustesActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.ajustes, rootKey);
            Preference pr =  findPreference("cargar_datos");
            pr.setOnPreferenceClickListener(preference -> {
                try {
                    File file = new File(this.getContext().getExternalFilesDir(null), "datos.json");
                    file.delete();
                    Comun.cargarJSON(this.getContext());
                    Snackbar.make(getView(), R.string.exito_descarga_s, Snackbar.LENGTH_LONG).show();
                } catch (IOException e) {
                    Snackbar.make(getView(), R.string.error_descarga_s, Snackbar.LENGTH_LONG).show();
                }
                return true;
            });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if ("tema_app".equals(key)) {
            int val;
            switch (SP.getString(key, "")) {
                case "light":
                    val = MODE_NIGHT_NO;
                    break;
                case "dark":
                    val = MODE_NIGHT_YES;
                    break;
                default:
                    val = MODE_NIGHT_FOLLOW_SYSTEM;
            }
            AppCompatDelegate.setDefaultNightMode(val);
        }
    }
}