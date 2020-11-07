package com.example.pruebasprovider.providers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pruebasprovider.MainActivity;
import com.example.pruebasprovider.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Autentificacion {

    private FirebaseAuth auth;

    public Autentificacion() {
        auth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> crearUsuario(User miUsuario){
        return auth.createUserWithEmailAndPassword(miUsuario.getEmail(),miUsuario.getContrasena());
    }
    public String getIdUser() {
        return auth.getCurrentUser().getUid();
    }

    public Task<AuthResult> iniciarSesion(String correo, String contrasena){
        return auth.signInWithEmailAndPassword(correo,contrasena);
    }
    public void logOut(){
        auth.signOut();
    }

}
