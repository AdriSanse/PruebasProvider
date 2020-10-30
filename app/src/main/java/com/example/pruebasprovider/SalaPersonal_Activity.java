package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebasprovider.Objects.Sucesos;
import com.example.pruebasprovider.providers.SalaProviders;
import com.example.pruebasprovider.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.DateFormat;
import java.util.Calendar;

public class SalaPersonal_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView textoDineroCambiar, textoDineroCargar, textoAsunto, textoFecha;
    RadioButton radioGasto, radioIngreso;
    Button btnActualizar, btnCalendario;
    SalaProviders salaProviders;
    FirebaseFirestore db;
    String id, fechaS, idUsuario;
    private ListenerRegistration miEscuchador;
    Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_personal_);

        salaProviders = new SalaProviders();
        db = FirebaseFirestore.getInstance();

        textoDineroCambiar = findViewById(R.id.textoDineroCambiador);
        textoDineroCargar = findViewById(R.id.textDineroDisponible);
        textoAsunto = findViewById(R.id.textAsunto);
        textoFecha = findViewById(R.id.textFecha);
        radioIngreso = findViewById(R.id.radioAgregar);
        radioGasto = findViewById(R.id.radioGastar);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnCalendario.setOnClickListener(this);
        btnActualizar = findViewById(R.id.btnActualizarCartera);
        btnActualizar.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        Intent miDinero = getIntent();
        id = (String) miDinero.getSerializableExtra("id");
        idUsuario = (String) miDinero.getSerializableExtra("idUsuario");
        cargarDatos();
    }

    public void cargarDatos(){
        CollectionReference busquedaDinero = db.collection("Salas");
        Query query = busquedaDinero.whereEqualTo("id",id);
        miEscuchador = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null && !value.isEmpty()){
                    textoDineroCargar.setText(""+value.getDocuments().get(0).get("dinero"));
                }
            }
        });
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(DocumentSnapshot document : task.getResult()){
//                        Toast.makeText(SalaPersonal_Activity.this, document.getData()+" y este dinero "+document.get("dinero"), Toast.LENGTH_SHORT).show();
//                        String dinero = ""+ document.get("dinero");
//                        textoDineroCargar.setText(dinero);
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onStop() {
        if(miEscuchador!=null){
            miEscuchador.remove();
        }
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        if(radioGasto.isChecked() || radioIngreso.isChecked()){
            textoDineroCambiar.setEnabled(true);
        }
        if(view.getId()==R.id.btnActualizarCartera){

            final String sAsunto = textoAsunto.getText().toString();
            final int dineroInt = Integer.parseInt(textoDineroCambiar.getText().toString());
            if(radioGasto.isChecked()){
                CollectionReference busquedaId = db.collection("Salas");
                Query query = busquedaId.whereEqualTo("id",id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()) {
                                int dinero = Integer.parseInt("" + document.get("dinero"));
                                int resultado = dinero - dineroInt;
                                String sResultado = resultado + "";
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId());
                                actuDinero.update("dinero",sResultado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            Sucesos miSuceso = new Sucesos();
                                            miSuceso.setDinero(""+dineroInt);
                                            miSuceso.setAsunto(sAsunto);
                                            miSuceso.setFecha(fechaS);
                                            miSuceso.setUsuario(idUsuario);
                                            miSuceso.setGastoIngreso("Gasto");

                                            DocumentReference actuAsunto = db.collection("Salas").document(id);
                                            actuAsunto.update("sucesos", FieldValue.arrayUnion(miSuceso)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, "Funciono", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }

            if(radioIngreso.isChecked()){
                CollectionReference busquedaId = db.collection("Salas");
                Query query = busquedaId.whereEqualTo("id",id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()) {
                                int dinero = Integer.parseInt("" + document.get("dinero"));
                                int resultado = dinero + dineroInt;
                                String sResultado = resultado + "";
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId());
                                actuDinero.update("dinero",sResultado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            Sucesos miSuceso = new Sucesos();
                                            miSuceso.setDinero(""+dineroInt);
                                            miSuceso.setAsunto(sAsunto);
                                            miSuceso.setFecha(fechaS);
                                            miSuceso.setUsuario(idUsuario);
                                            miSuceso.setGastoIngreso("Ingreso");

                                            DocumentReference actuAsunto = db.collection("Salas").document(id);
                                            actuAsunto.update("sucesos", FieldValue.arrayUnion(miSuceso)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, "Funciono", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
            textoFecha.setText("");
            textoAsunto.setText("");
            textoDineroCambiar.setText("");
            radioIngreso.setChecked(false);
            radioGasto.setChecked(false);

        }

        if(view.getId()==R.id.btnCalendario){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, monthOfYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String fecha = dayOfMonth+"/"+monthOfYear+"/"+year;
                    fechaS=fecha;
                    textoFecha.setText(fecha);
                }
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        }
    }
}