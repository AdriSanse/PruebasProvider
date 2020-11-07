package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pruebasprovider.Adaptador.AdaptadorSucesos;
import com.example.pruebasprovider.Objects.Sala;
import com.example.pruebasprovider.Objects.Sucesos;
import com.example.pruebasprovider.Objects.SucesosListado;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Sucesos_Activity extends AppCompatActivity {

    ArrayList<Sucesos> misSucesos;
    RecyclerView miLista;
    AdaptadorSucesos adaptador;
    String id;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucesos_);

        db = FirebaseFirestore.getInstance();

        misSucesos = new ArrayList<>();
        miLista = findViewById(R.id.miListaRecycler);
        miLista.setLayoutManager(new LinearLayoutManager(this));

        Intent miIntento = getIntent();
        id = (String) miIntento.getSerializableExtra("id");
        cargarAdaptador();
    }

    public void cargarAdaptador(){
        Query query = db.collection("Salas").document(id).collection("Sucesos");
        FirestoreRecyclerOptions<Sucesos> firestoreSucesos = new FirestoreRecyclerOptions.Builder<Sucesos>()
                .setQuery(query, Sucesos.class).build();

        adaptador = new AdaptadorSucesos(firestoreSucesos);
        miLista.setAdapter(adaptador);
        adaptador.startListening();

    }

}