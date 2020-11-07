package com.example.pruebasprovider.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebasprovider.Objects.Sucesos;
import com.example.pruebasprovider.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AdaptadorSucesos extends FirestoreRecyclerAdapter<Sucesos, AdaptadorSucesos.ViewHolderSucesos> {

    ArrayList<Sucesos> misSucesos;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdaptadorSucesos(@NonNull FirestoreRecyclerOptions<Sucesos> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolderSucesos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View miView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sucesos_lista,parent,false);
        ViewHolderSucesos holder = new ViewHolderSucesos(miView);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolderSucesos holder, int position, @NonNull Sucesos suceso) {

        holder.asuntoTxt.setText("El asunto fue "+suceso.getAsunto()+" el dia "+suceso.getFecha()+" con un "+suceso.getGastoIngreso()+" de "+suceso.getDinero()+"€");
        holder.nombreTxt.setText(suceso.getUsuario());
    }

    public class ViewHolderSucesos extends RecyclerView.ViewHolder {

        TextView nombreTxt, asuntoTxt;

        public ViewHolderSucesos(@NonNull View itemView) {
            super(itemView);
            nombreTxt = itemView.findViewById(R.id.nombreUsuario);
            asuntoTxt = itemView.findViewById(R.id.ArgumentoSuceso);

        }
    }
}