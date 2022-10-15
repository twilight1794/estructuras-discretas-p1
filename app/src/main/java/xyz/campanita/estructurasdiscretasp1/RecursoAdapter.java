package xyz.campanita.estructurasdiscretasp1;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.Objects;

import xyz.campanita.estructurasdiscretasp1.bibliotecas.Colaborador;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Recurso;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.TipoRecurso;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.ViewHolder> {
    private LinkedList<Recurso> dataset;
    private LinkedList<String> refs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout elemento;
        public ImageView icono;
        public TextView titulo;
        public TextView autor;
        public ViewHolder(ConstraintLayout l) {
            super(l);
            elemento = l;
            icono = l.findViewById(R.id.tipo_icono);
            titulo = l.findViewById(R.id.lista_titulo);
            autor = l.findViewById(R.id.lista_autor);
        }
    }

    public RecursoAdapter(LinkedList dataset, Comun.Fuente i) {
        if (i == Comun.Fuente.LISTA_RECURSOS) { this.dataset = dataset; }
        else { this.refs = dataset; }
    }

    @Override
    public RecursoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout l = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_elemento_recurso, parent, false);
        return new ViewHolder(l);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Obtener elemento
        Recurso elem = null;
        if (dataset != null) {
            Log.i("UUU", "Desde recursos!");
            elem = dataset.get(position);
        } else {
            Log.i("UUU", "Desde favoritos o búsqueda!");
            for (int i = 0; i < Comun.recursos.size(); i++){
                Recurso posible = Comun.recursos.get(i);
                if (Objects.equals(refs.get(position), posible.getIsbn().get(0))) {
                    Log.i("UUU", String.format("Comparando refs=%s, posible=%s", refs.get(position), posible.getIsbn().get(0)));
                    elem = posible;
                    break;
                }
            }
        }

        // Procesar
        holder.elemento.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), RecursoActivity.class);
            i.putExtra("pos", position);
            v.getContext().startActivity(i);
        });
        if (elem.getTipo() == TipoRecurso.LIBRO){
            holder.icono.setImageResource(R.drawable.ic_baseline_book_40);
        } else {
            holder.icono.setImageResource(R.drawable.ic_baseline_insert_drive_file_40);
        }
        holder.titulo.setText(elem.getTitulo());
        StringBuilder cols = new StringBuilder();
        for (Colaborador c: elem.getColaborador()){
            cols.append(c.getNombre());
            cols.append(" • ");
        }
        holder.autor.setText(cols.substring(0, cols.length() - 3));
    }

    @Override
    public int getItemCount() {
        if (dataset != null){
            return dataset.size();
        } else {
            return refs.size();
        }
    }
}

