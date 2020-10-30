package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebasprovider.providers.Autentificacion;
import com.example.pruebasprovider.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PaginaPrincipal extends AppCompatActivity implements View.OnClickListener{

    TextView cerrar, nombreUsuario;
    Button btnPerfil, btnCambiarPestana, btnUnirse, btnMeterteSala;
    Autentificacion mauth;
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    String idUsuario ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        mauth = new Autentificacion();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();

        nombreUsuario = findViewById(R.id.nombreVer);
        cerrar = findViewById(R.id.textCierre);
        cerrar.setOnClickListener(this);
        btnCambiarPestana = findViewById(R.id.btnCambiarPesatanaCrear);
        btnCambiarPestana.setOnClickListener(this);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(this);
        btnUnirse = findViewById(R.id.btnUnirseSala);
        btnUnirse.setOnClickListener(this);
        btnMeterteSala = findViewById(R.id.btnMeterteSala);
        btnMeterteSala.setOnClickListener(this);
        cargarDatos();

    }

    public void cargarDatos(){
        Task<DocumentSnapshot> task = usersProvider.getUsuario(mauth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombreUsuario.setText(documentSnapshot.get("nombre").toString());
                idUsuario = documentSnapshot.getId();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaginaPrincipal.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textCierre){
            mauth.logOut();
            Intent miIntentoLogin = new Intent(this, login.class);
            miIntentoLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(miIntentoLogin);
        }
        if(view.getId()==R.id.btnPerfil){
            Intent miIntent = new Intent(this,PerfilActivity.class);
            startActivity(miIntent);
        }
        if(view.getId()==R.id.btnCambiarPesatanaCrear){
            Intent miIntento1 = new Intent(this,CrearSala_Activity.class);
            startActivity(miIntento1);
        }
        if(view.getId()==R.id.btnUnirseSala){
            Intent miIntento2 = new Intent(this,UnirseSala_Activity.class);
            startActivity(miIntento2);
        }
        if(view.getId()==R.id.btnMeterteSala){
            final boolean salaEncontrada = true;
            CollectionReference busquedaSala = db.collection("Salas");
            busquedaSala.whereArrayContains("grupo", idUsuario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot document : task.getResult()){
                            String id = (String) document.get("id");
                            Intent miIntento3 = new Intent(PaginaPrincipal.this,EleccionSala_Activity.class);
                            miIntento3.putExtra("id",id);
                            miIntento3.putExtra("idUsuario",idUsuario);
                            startActivity(miIntento3);
                        }
                    }
                }
            });

        }
    }
}