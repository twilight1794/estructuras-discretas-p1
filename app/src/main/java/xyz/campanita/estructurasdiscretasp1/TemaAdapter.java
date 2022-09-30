package xyz.campanita.estructurasdiscretasp1;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TemaAdapter extends RecyclerView.Adapter<TemaAdapter.ViewHolder> {
    private ArrayList<String[]> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public TextView pos;
        public ViewHolder(LinearLayout tv) {
            super(tv);
            titulo = tv.findViewById(R.id.lista_elemento_tema_nombre);
            pos = tv.findViewById(R.id.lista_elemento_tema_indice);
        }
    }

    public TemaAdapter(ArrayList<String[]> myDataSet) {
        mDataSet = myDataSet;
    }

    @Override
    public TemaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_elemento_tema, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titulo.setText(mDataSet.get(position)[1]);
        holder.pos.setText("Tema " + mDataSet.get(position)[0]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

