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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class login extends AppCompatActivity implements View.OnClickListener{

    TextView correo,contrasena,textoCambio;
    Button btnInciar;
    Autentificacion mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mauth = new Autentificacion();

        correo = findViewById(R.id.textCorreoInicio);
        contrasena = findViewById(R.id.textContrasenaInicio);
        btnInciar = findViewById(R.id.btnInciar);
        btnInciar.setOnClickListener(this);
        textoCambio = findViewById(R.id.textoCambioaRegistro);
        textoCambio.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textoCambioaRegistro){
            Intent miIntento = new Intent(this,MainActivity.class);
            startActivity(miIntento);
        }
        if(view.getId()==R.id.btnInciar){
            String scorreo = correo.getText().toString();
            String scontrasena = contrasena.getText().toString();
            mauth.iniciarSesion(scorreo,scontrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent miIntento = new Intent(login.this,PaginaPrincipal.class);
                        startActivity(miIntento);
                    }else{
                        Toast.makeText(login.this, "No pusistes bien los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}