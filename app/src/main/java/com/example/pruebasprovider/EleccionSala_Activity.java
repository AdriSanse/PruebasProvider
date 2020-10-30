package com.example.pruebasprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

public class EleccionSala_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnSalaPersonal, btnSucesos;
    String id, idUsuario;
    Spinner salasCombo;
    FirebaseFirestore db;
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
        //salasCombo.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.combo_salas));
        Intent miIntento = getIntent();
        id = (String) miIntento.getSerializableExtra("id");
        idUsuario = (String) miIntento.getSerializableExtra("idUsuario");
    }
    public void cargarDatos(){
        
    }

    @Override
    public void onClick(View view) {
        if(R.id.btnActividadSala==view.getId()){
            Intent miIntentoSucesos = new Intent(this, Sucesos_Activity.class);
            miIntentoSucesos.putExtra("id",id);
            startActivity(miIntentoSucesos);
        }
        if(R.id.btnIrSalaPersonal==view.getId()){
            Intent miIntentoSala = new Intent(this, SalaPersonal_Activity.class);
            miIntentoSala.putExtra("id",id);
            miIntentoSala.putExtra("idUsuario",idUsuario);
            startActivity(miIntentoSala);
        }
    }
}