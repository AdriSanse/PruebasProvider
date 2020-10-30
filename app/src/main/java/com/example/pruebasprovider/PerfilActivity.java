package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebasprovider.Objects.User;
import com.example.pruebasprovider.providers.Autentificacion;
import com.example.pruebasprovider.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textoUsuario, textoCorreo, textoTelefono;
    Button btnGuardarCambios;
    ImageButton btnVolver;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    String id ="";
    String contrasena ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = new Autentificacion();
        usersProvider = new UsuariosProvider();

        textoUsuario = findViewById(R.id.textUserPerfil);
        textoCorreo = findViewById(R.id.textCorreoPerfil);
        textoTelefono = findViewById(R.id.textTelefonoPerfil);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnVolver = findViewById(R.id.imgVolver);
        btnGuardarCambios.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
        cargarDatosPerfil();
    }

    public void cargarDatosPerfil(){
        Task<DocumentSnapshot> task = usersProvider.getUsuario(mAuth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id=documentSnapshot.get("id").toString();
                contrasena=documentSnapshot.get("contrasena").toString();
                textoUsuario.setText(documentSnapshot.get("nombre").toString());
                textoCorreo.setText(documentSnapshot.get("email").toString());
                textoTelefono.setText(documentSnapshot.get("telefono").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PerfilActivity.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void guardarCambios(){
        String telefono = textoTelefono.getText().toString();
        String usuario = textoUsuario.getText().toString();
        String correo = textoCorreo.getText().toString();
        User miUsuario = new User(id,usuario,contrasena,correo,telefono);
        usersProvider.updateUser(mAuth.getIdUser(), miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PerfilActivity.this, "Se actualizo correcamente tu perfil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PerfilActivity.this, "Fallo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnGuardarCambios){
            guardarCambios();
        }
        if(view.getId()==R.id.imgVolver){
            Intent miIntento = new Intent(this,PaginaPrincipal.class);
            startActivity(miIntento);
        }
    }
}