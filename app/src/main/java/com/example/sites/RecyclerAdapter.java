package com.example.sites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sites.database.Sitio;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private ArrayList<Sitio> datos;
    private Context context;
    private OnItemClickListener listener;


    //para contato
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RecyclerAdapter(Context context, ArrayList<Sitio> datos,OnItemClickListener listener) {
        this.datos = datos;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_list,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sitio sitio = datos.get(position);

        // Cargar la foto en el ImageView utilizando Glide
        if (sitio.getFoto() != null && sitio.getFoto().length > 0) {
            Glide.with(context)
                    .load(sitio.getFoto())
                    .into(holder.getImg());
        } else {
            // Si no hay foto, puedes mostrar una imagen por defecto
            holder.getImg().setImageResource(R.drawable.mont);
        }


        // Mostrar el nombre en el TextView
        holder.getTextView().setText(sitio.getTitulo());
        holder.getTextViewNombre().setText(sitio.getNombre());
        holder.getTextViewTelefono().setText(sitio.getTelefono());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar al método onItemClick del listener al hacer clic
                int position = holder.getAdapterPosition(); // Obtener la posición actual
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return datos.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo;
        private final TextView txtNombre;
        private final TextView txtTelefono;
        private final ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.textTitulo);
            txtNombre = itemView.findViewById(R.id.textNombre);
            txtTelefono = itemView.findViewById(R.id.textTelefono);
            img = itemView.findViewById(R.id.imageView);

        }

        public TextView getTextView() {
            return txtTitulo;
        }
        public TextView getTextViewNombre() {
            return txtNombre;
        }
        public TextView getTextViewTelefono() {
            return txtTelefono;
        }
        public ImageView getImg() {
            return img;
        }


    }
}
