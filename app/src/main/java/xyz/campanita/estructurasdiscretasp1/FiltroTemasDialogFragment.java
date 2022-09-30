package xyz.campanita.estructurasdiscretasp1;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

public class FiltroTemasDialogFragment extends DialogFragment {
    public static String TAG = "filtro_temas";

    public interface FiltroTemasDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
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

        LinearLayoutCompat llc = v.findViewById(R.id.ctcont);
        Resources res = getResources();
        String[] encArray = res.getStringArray(R.array.temas);
        for (int i = 0; i<encArray.length; i++) {
            CheckBoxTriStates ch = new CheckBoxTriStates(llc, getContext());
            int pid = View.generateViewId();
            ch.setId(pid);
            ch.setText("Tema " + (i+1) + ": " + encArray[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ch.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Medium);
            }
            llc.addView(ch);
            String[] encSubArray = res.getStringArray(res.getIdentifier("temas_"+(i+1), "array", getContext().getPackageName()));
            for (int ii = 0; ii<encSubArray.length; ii++) {
                CheckBoxTriStates ach = new CheckBoxTriStates(llc, getContext());
                ach.setId(View.generateViewId());
                ach.setText("Tema " + (i+1) + "." + (ii+1) + ": " + encSubArray[ii]);
                ach.setParentItem(pid);
                llc.addView(ach);
            }
        }
        builder.setView(v);

        builder.setPositiveButton(android.R.string.ok, (dialog, id) -> listener.onDialogPositiveClick(FiltroTemasDialogFragment.this))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> listener.onDialogPositiveClick(FiltroTemasDialogFragment.this));

        return builder.create();
    }
}

