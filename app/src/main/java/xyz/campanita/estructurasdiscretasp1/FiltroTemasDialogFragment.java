package xyz.campanita.estructurasdiscretasp1;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;

public class FiltroTemasDialogFragment extends DialogFragment {
    public static String TAG = "filtro_temas";
    public LinearLayoutCompat llc;

    public interface FiltroTemasDialogListener {
        void onDialogPositiveClick(FiltroTemasDialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    FiltroTemasDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FiltroTemasDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " debe implementar FiltroTemasDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.buscaren_filtro_titulo);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_filtro_temas, null);

        llc = v.findViewById(R.id.ctcont);
        Resources res = getResources();
        String[] encArray = res.getStringArray(R.array.temas);
        String temaId = null;
        for (int i = 0; i<encArray.length; i++) {
            CheckBox ch = new CheckBox(getContext());
            temaId = Integer.toString(i+1);
            int pid = View.generateViewId();
            ch.setId(pid);
            if (Comun.temasFiltrados.contains(temaId)) {
                ch.setChecked(true);
            }
            ch.setText(String.format("Tema %s: %s", temaId, encArray[i]));
            ch.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Medium);
            llc.addView(ch);
            String[] encSubArray = res.getStringArray(res.getIdentifier("temas_"+(i+1), "array", getContext().getPackageName()));
            for (int ii = 0; ii<encSubArray.length; ii++) {
                CheckBox ach = new CheckBox(getContext());
                temaId = String.format("%d.%d", i+1, ii+1);
                if (Comun.temasFiltrados.contains(temaId)) {
                    Log.i("UUU", "Contiene "+temaId);
                    ach.setChecked(true);
                } else {
                    ach.setChecked(false);
                }
                ach.setId(View.generateViewId());
                ach.setText(String.format("Tema %s: %s", temaId, encSubArray[ii]));
                llc.addView(ach);
            }
        }
        builder.setView(v);

        builder.setPositiveButton(android.R.string.ok, (dialog, id) -> listener.onDialogPositiveClick(FiltroTemasDialogFragment.this))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> listener.onDialogNegativeClick(FiltroTemasDialogFragment.this));

        return builder.create();
    }
}

