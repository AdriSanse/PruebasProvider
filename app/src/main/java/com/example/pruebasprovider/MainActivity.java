package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebasprovider.Objects.Sala;
import com.example.pruebasprovider.Objects.User;
import com.example.pruebasprovider.providers.Autentificacion;
import com.example.pruebasprovider.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nombre,contrasena,correo,telefono, cambio;
    Button btnRegistrar;
    Autentificacion auth;
    UsuariosProvider usuarioProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = new Autentificacion();
        usuarioProvider = new UsuariosProvider();
        nombre = findViewById(R.id.TextNombre);
        contrasena = findViewById(R.id.TextContrasena);
        correo = findViewById(R.id.TextCorreo);
        telefono = findViewById(R.id.TextTelefono);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);
        cambio = findViewById(R.id.textCambioaInicio);
        cambio.setOnClickListener(this);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getUid()!=null){
            startActivity(new Intent(this,PaginaPrincipal.class));
        }
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnRegistrar){
            String scorreo = correo.getText().toString();
            String spass = contrasena.getText().toString();
            final String snombre = nombre.getText().toString();
            final String stelefono = telefono.getText().toString();
            final User miUsuario = new User(spass,scorreo);
            auth.crearUsuario(miUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        miUsuario.setId(auth.getIdUser());
                        miUsuario.setNombre(snombre);
                        miUsuario.setTelefono(stelefono);
                        usuarioProvider.createUser(miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Se unio al Provider", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "No se unio", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Toast.makeText(MainActivity.this, "Se refgistro", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "No", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(view.getId()==R.id.textCambioaInicio){
            Intent miIntento = new Intent(this,login.class);
            startActivity(miIntento);
        }
    }
}