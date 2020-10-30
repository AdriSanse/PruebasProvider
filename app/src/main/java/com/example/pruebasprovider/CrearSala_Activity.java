package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebasprovider.Objects.Sala;
import com.example.pruebasprovider.providers.Autentificacion;
import com.example.pruebasprovider.providers.SalaProviders;
import com.example.pruebasprovider.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class CrearSala_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView textoNombreSala, textoNombreUsuario, textoContrasenaSala, textoRepetirContrasenaSala, textoDinero;
    Button btnCrear;
    SalaProviders salaProviders;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_sala_);

        salaProviders = new SalaProviders();
        usersProvider = new UsuariosProvider();
        mAuth = new Autentificacion();
        db = FirebaseFirestore.getInstance();

        textoNombreSala = findViewById(R.id.textNombreSala);
        textoNombreUsuario = findViewById(R.id.textNombreUsuarioCrearSala);
        textoContrasenaSala = findViewById(R.id.textContrasenaSala);
        textoRepetirContrasenaSala = findViewById(R.id.textRepetirContrasenaSala);
        textoDinero = findViewById(R.id.textDinero);
        btnCrear = findViewById(R.id.btnCrearSala);
        btnCrear.setOnClickListener(this);
        cargarDatos();
    }

    public void cargarDatos(){
        Task<DocumentSnapshot> task = usersProvider.getUsuario(mAuth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textoNombreUsuario.setText(documentSnapshot.get("id").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearSala_Activity.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnCrearSala){
            Sala miSala = new Sala();
            if(textoContrasenaSala.getText().toString().equals(textoRepetirContrasenaSala.getText().toString())){
                miSala.setDinero(textoDinero.getText().toString());
                miSala.setNombreCreador(textoNombreUsuario.getText().toString());
                miSala.setNombreSala(textoNombreSala.getText().toString());
                miSala.setContrasena(textoContrasenaSala.getText().toString());
                ArrayList<String> miGrupo = new ArrayList<>();
                miGrupo.add(textoNombreUsuario.getText().toString());
                miSala.setGrupo(miGrupo);
                salaProviders.createSala(miSala).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            CollectionReference idSala = db.collection("Salas");
                            Query query = idSala.whereEqualTo("nombreSala",textoNombreSala.getText().toString());
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            String id = document.getId();
                                            Intent miIntento = new Intent(CrearSala_Activity.this,InvitarGente_Activity.class);
                                            miIntento.putExtra("id",id);
                                            startActivity(miIntento);
                                        }
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(CrearSala_Activity.this, "No se unio", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                Toast.makeText(this, "No es la misma Contrasena", Toast.LENGTH_SHORT).show();
            }
            
        }
    }
}