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

import com.example.pruebasprovider.Objects.Sala;
import com.example.pruebasprovider.Objects.Sucesos;
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
    Autentificacion auth;
    SalaProviders salaProviders;
    UsuariosProvider userProviders;
    FirebaseFirestore db;
    String id, fechaS, idUsuario, nombre;
    private ListenerRegistration miEscuchador;
    Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_personal_);

        auth = new Autentificacion();
        salaProviders = new SalaProviders();
        userProviders = new UsuariosProvider();
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
        Toast.makeText(this, ""+idUsuario, Toast.LENGTH_SHORT).show();
        cargarDatos();
        sacarUsuario();
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
        Toast.makeText(this, "Eres "+idUsuario, Toast.LENGTH_SHORT).show();
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

    public void sacarUsuario(){
        Task<DocumentSnapshot> task = userProviders.getUsuario(auth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(SalaPersonal_Activity.this, ""+documentSnapshot.get("nombre").toString(), Toast.LENGTH_SHORT).show();
                nombre = documentSnapshot.get("nombre").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SalaPersonal_Activity.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
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
                                final String sResultado = resultado + "";

                                Sucesos miSuceso = new Sucesos();
                                miSuceso.setDinero(""+dineroInt);
                                miSuceso.setAsunto(sAsunto);
                                miSuceso.setFecha(fechaS);
                                miSuceso.setUsuario(nombre);
                                miSuceso.setGastoIngreso("Gasto");
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId()).collection("Sucesos").document();
                                actuDinero.set(miSuceso).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SalaPersonal_Activity.this, "Funciona Crear Sucesos Dentro de la Sala", Toast.LENGTH_SHORT).show();
                                            DocumentReference actuDineroSala = db.collection("Salas").document(id);
                                            actuDineroSala.update("dinero",sResultado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, "Se actualizo el dinero de la sala", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SalaPersonal_Activity.this, "No funciono la creacion", Toast.LENGTH_SHORT).show();
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
                                final String sResultado = resultado + "";

                                Sucesos miSuceso = new Sucesos();
                                miSuceso.setDinero(""+dineroInt);
                                miSuceso.setAsunto(sAsunto);
                                miSuceso.setFecha(fechaS);
                                miSuceso.setUsuario(nombre);
                                miSuceso.setGastoIngreso("Ingreso");
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId()).collection("Sucesos").document();
                                actuDinero.set(miSuceso).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SalaPersonal_Activity.this, "Funciona Crear Sucesos Dentro de la Sala", Toast.LENGTH_SHORT).show();
                                            DocumentReference actuDineroSala = db.collection("Salas").document(id);
                                            actuDineroSala.update("dinero",sResultado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, "Se actualizo el dinero de la sala", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SalaPersonal_Activity.this, "No funciono la creacion", Toast.LENGTH_SHORT).show();
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