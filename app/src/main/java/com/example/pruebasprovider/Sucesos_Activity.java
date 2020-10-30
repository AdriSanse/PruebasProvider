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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Sucesos_Activity extends AppCompatActivity {

    ArrayList<Sucesos> misSucesos;
    RecyclerView miLista;
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
        AdaptadorSucesos adaptador = new AdaptadorSucesos(misSucesos);
        miLista.setAdapter(adaptador);
        Intent miIntento = getIntent();
        id = (String) miIntento.getSerializableExtra("id");
        cargarAdaptador();
    }

    public void cargarAdaptador(){
        DocumentReference coleccion = db.collection("Salas").document(id);
        coleccion.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<Sucesos> misSucesos = documentSnapshot.toObject(Sala.class).getSucesos();

                for(Sucesos a : misSucesos){
                    System.out.println(a.getAsunto()+" en "+a.getFecha());
                }
            }
        });
    }

}