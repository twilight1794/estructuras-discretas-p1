package xyz.campanita.estructurasdiscretasp1;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AyudaDialogFragment extends DialogFragment {
    public static String TAG = "ayuda";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_ayuda, null);
        TextView txt = (TextView) v.findViewById(R.id.ayuda_texto);
        txt.setText(Html.fromHtml(getString(R.string.ayuda_msg), FROM_HTML_MODE_COMPACT));
        txt.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setView(v);
        builder.setNeutralButton(android.R.string.ok, null );

        return builder.create();
    }
}