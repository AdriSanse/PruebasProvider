package com.example.pruebasprovider;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InvitarGente_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView textoTelefono, textoUsuario;
    Button btnInvitar, btnIrSala;
    String miId="", nombreUsuario="";
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitar_gente_);

        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();

        textoUsuario = findViewById(R.id.nombreUsuarioInvitar);
        textoTelefono = findViewById(R.id.textNumeroTelefono);
        btnInvitar = findViewById(R.id.btnInvitarGente);
        btnInvitar.setOnClickListener(this);
        btnIrSala = findViewById(R.id.btnIrSalaCreada);
        btnIrSala.setOnClickListener(this);
        Intent miIntentoId = getIntent();
        miId = (String) miIntentoId.getSerializableExtra("id");
        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED){
            String[] permissions = {Manifest.permission.SEND_SMS};
            requestPermissions(permissions,1);
        }
        cargarDatos();
    }

    public void cargarDatos(){
        CollectionReference busquedaId = db.collection("User");
        Query query = busquedaId.whereEqualTo("id",miId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Toast.makeText(InvitarGente_Activity.this, "Holaaaaa"+document.getData(), Toast.LENGTH_SHORT).show();
                        String nombre = (String) document.get("nombre");
                        textoUsuario.setText(nombre);
                    }
                }
            }
        });

    }

    public void EnviarSMS(String telefono, String mensaje){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefono,null,mensaje,null,null);
            Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Fallo al enviar el sms", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View view) {
        if(R.id.btnInvitarGente==view.getId()){
            Toast.makeText(this, ""+miId, Toast.LENGTH_SHORT).show();
            EnviarSMS(textoTelefono.getText().toString(),miId);
        }
        if(R.id.btnIrSalaCreada==view.getId()){
            Intent miIntento = new Intent(this,SalaPersonal_Activity.class);
            miIntento.putExtra("id",miId);
            startActivity(miIntento);
        }
    }
}