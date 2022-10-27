package xyz.campanita.estructurasdiscretasp1;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
        int pos = -1;
        if (dataset != null) {
            Log.i("UUU", "Desde recursos!");
            elem = dataset.get(position);
            pos = position;
        } else {
            Log.i("UUU", "Desde favoritos o búsqueda!");
            for (int i = 0; i < Comun.recursos.size(); i++){
                Recurso posible = Comun.recursos.get(i);
                if (Objects.equals(refs.get(position), posible.getId().get(0))) {
                    Log.i("UUU", String.format("Comparando refs=%s, posible=%s", refs.get(position), posible.getId().get(0)));
                    elem = posible;
                    pos = i;
                    break;
                }
            }
        }

        // Procesar
        int finalPos = pos;
        holder.elemento.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), RecursoActivity.class);
            i.putExtra("pos", finalPos);
            v.getContext().startActivity(i);
        });
        if (elem.getTipo() == TipoRecurso.LIBRO){
            holder.icono.setImageResource(R.drawable.ic_baseline_book_40);
        } else {
            holder.icono.setImageResource(R.drawable.ic_baseline_insert_drive_file_40);
        }
        holder.titulo.setText(elem.getTitulo());
        StringBuilder cols = new StringBuilder();
        ArrayList<Colaborador> colab = elem.getColaborador();
        if (colab.isEmpty()) {
            holder.autor.setText(R.string.desconocido);
        } else {
            for (Colaborador c: colab) {
                cols.append(c.getNombre());
                cols.append(" • ");
            }
            holder.autor.setText(cols.substring(0, cols.length() - 3));
        }
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

