package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pruebasprovider.Objects.Sala;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EleccionSala_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnSalaPersonal, btnSucesos;
    String idUsuario;
    ArrayList<String> misIdSala;
    Spinner salasCombo;
    FirebaseFirestore db;
    private ArrayAdapter<Sala> salaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_sala_);

        db = FirebaseFirestore.getInstance();

        btnSalaPersonal = findViewById(R.id.btnIrSalaPersonal);
        btnSalaPersonal.setOnClickListener(this);
        btnSucesos = findViewById(R.id.btnActividadSala);
        btnSucesos.setOnClickListener(this);
        salasCombo = findViewById(R.id.comboSalasUnidos);
        Intent miIntento = getIntent();
        misIdSala = (ArrayList<String>) miIntento.getSerializableExtra("id");
        idUsuario = (String) miIntento.getSerializableExtra("idUsuario");

        cargarDatos();
    }
    public void cargarDatos(){
        salaAdapter = new ArrayAdapter<>(EleccionSala_Activity.this,R.layout.support_simple_spinner_dropdown_item,new ArrayList<Sala>());
        salasCombo.setAdapter(salaAdapter);
        for(String a: misIdSala){
            DocumentReference miColeccionSala = db.collection("Salas").document(a);
            miColeccionSala.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Sala miSala = new Sala(task.getResult().get("id").toString(), task.getResult().get("nombreSala").toString());
                        salaAdapter.add(miSala);
                        Toast.makeText(EleccionSala_Activity.this, ""+task.getResult().get("nombreSala"), Toast.LENGTH_SHORT).show();
                        salaAdapter.notifyDataSetChanged();
                    }
                }
            });
        }


    }

    @Override
    public void onClick(View view) {
        if(R.id.btnActividadSala==view.getId()){
            Toast.makeText(this, ""+salasCombo.getSelectedItem(), Toast.LENGTH_SHORT).show();
//            Intent miIntentoSucesos = new Intent(this, Sucesos_Activity.class);
//            miIntentoSucesos.putExtra("id",salasCombo.getSelectedItemId());
//            miIntentoSucesos.putExtra("idUsuario",idUsuario);
//            startActivity(miIntentoSucesos);
        }
        if(R.id.btnIrSalaPersonal==view.getId()){
            Intent miIntentoSala = new Intent(this, SalaPersonal_Activity.class);
            //miIntentoSala.putExtra("id",id);
            miIntentoSala.putExtra("idUsuario",idUsuario);
            startActivity(miIntentoSala);
        }
    }
}