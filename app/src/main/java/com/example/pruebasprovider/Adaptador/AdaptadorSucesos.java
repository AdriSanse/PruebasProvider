package com.example.pruebasprovider.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebasprovider.Objects.Sucesos;
import com.example.pruebasprovider.R;

import java.util.ArrayList;

public class AdaptadorSucesos extends RecyclerView.Adapter<AdaptadorSucesos.ViewHolderSucesos> {

    ArrayList<Sucesos> misSucesos;

    public AdaptadorSucesos(ArrayList<Sucesos> misSucesos) {
        this.misSucesos = misSucesos;
    }

    @NonNull
    @Override
    public ViewHolderSucesos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View miView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sucesos_lista,null,false);
        return new ViewHolderSucesos(miView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSucesos holder, int position) {
        holder.nombreTxt.setText(misSucesos.get(position).getUsuario());
        holder.fechaTxt.setText(misSucesos.get(position).getFecha());
        holder.asuntoTxt.setText(misSucesos.get(position).getAsunto());
        holder.dineroTxt.setText(misSucesos.get(position).getDinero());
    }


    @Override
    public int getItemCount() {
        return misSucesos.size();
    }

    public class ViewHolderSucesos extends RecyclerView.ViewHolder {

        TextView nombreTxt, fechaTxt, asuntoTxt, dineroTxt;

        public ViewHolderSucesos(@NonNull View itemView) {
            super(itemView);
            nombreTxt = itemView.findViewById(R.id.nombreUsuario);
            fechaTxt = itemView.findViewById(R.id.fechaSuceso);
            asuntoTxt = itemView.findViewById(R.id.ArgumentoSuceso);
            dineroTxt = itemView.findViewById(R.id.dineroSuceso);

        }
    }
}
