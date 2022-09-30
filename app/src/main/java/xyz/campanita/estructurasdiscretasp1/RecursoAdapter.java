package xyz.campanita.estructurasdiscretasp1;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.ViewHolder> {
    private String[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView tv) {
            super(tv); //v
            textView = tv;
        }
    }

    public RecursoAdapter(String[] myDataSet) {
        mDataSet = myDataSet;
    }

    @Override
    public RecursoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_elemento_recurso, parent, false);*/
        TextView v = (TextView) new TextView(parent.getContext());

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}

