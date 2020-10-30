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
import com.example.pruebasprovider.providers.Autentificacion;
import com.example.pruebasprovider.providers.SalaProviders;
import com.example.pruebasprovider.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.Authenticator;

public class UnirseSala_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView textoNombreSala, textoContrasenaSala, textoNombreUsuario;
    Button btnUnirse;
    SalaProviders salaProviders;
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    Autentificacion mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_sala_);

        salaProviders = new SalaProviders();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();
        mAuth = new Autentificacion();

        textoNombreSala = findViewById(R.id.textNombreSalaUnirse);
        textoNombreUsuario = findViewById(R.id.textNombreUsuarioUnirse);
        textoContrasenaSala = findViewById(R.id.textContrasenaSalaUnirse);
        btnUnirse = findViewById(R.id.btnUnirse);
        btnUnirse.setOnClickListener(this);
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
                Toast.makeText(UnirseSala_Activity.this, "Algo ha fallado", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnUnirse){
            CollectionReference busquedaId = db.collection("Salas");
            Query query = busquedaId.whereEqualTo("id",textoNombreSala.getText().toString());
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            DocumentReference washingtonRef = db.collection("Salas").document(document.getId());
                            washingtonRef.update("grupo", FieldValue.arrayUnion(textoNombreUsuario.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UnirseSala_Activity.this, "Se unio a la sala", Toast.LENGTH_SHORT).show();
                                    Intent miIntento = new Intent(UnirseSala_Activity.this,SalaPersonal_Activity.class);
                                    miIntento.putExtra("id",textoNombreSala.getText().toString());
                                    startActivity(miIntento);
                                }
                            });
                        }
                    }
                }
            });
            Intent miIntento = new Intent(UnirseSala_Activity.this,SalaPersonal_Activity.class);
            startActivity(miIntento);
        }
    }
}