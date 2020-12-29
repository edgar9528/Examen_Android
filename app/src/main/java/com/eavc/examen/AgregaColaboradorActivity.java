package com.eavc.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eavc.examen.dataBase.BaseLocal;
import com.eavc.examen.model.DataEmployess;
import com.eavc.examen.model.Employees;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AgregaColaboradorActivity extends AppCompatActivity {

    private TextInputEditText ti_nombre,ti_correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega_colaborador);

        Button bt_agregar,bt_salir;

        ti_nombre = findViewById(R.id.ti_nombre);
        ti_correo = findViewById(R.id.ti_correo);
        bt_agregar = findViewById(R.id.bt_agregar);
        bt_salir = findViewById(R.id.bt_salir);

        bt_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarColaborador();
            }
        });

        bt_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void guardarColaborador()
    {
        if(ti_correo.getText().toString().isEmpty() || ti_nombre.getText().toString().isEmpty())
        {
            Toast.makeText(AgregaColaboradorActivity.this, getString(R.string.msg_rellena), Toast.LENGTH_SHORT).show();
        }
        else
        {
            int id = Integer.parseInt( BaseLocal.SelectDato("SELECT max (ID_EMPLOYEE) FROM employees",getApplicationContext()) ) +1 ;

            String lat = String.valueOf( (Math.random() * 19) + 1 );
            String log = String.valueOf( (Math.random() * 99) + 1 );


            String consulta = "INSERT INTO EMPLOYEES VALUES ("+id+",'"+ti_nombre.getText().toString()+"','"+ti_correo.getText().toString()+"')";
            BaseLocal.Insert(consulta, getApplicationContext());

            consulta = "INSERT INTO LOCATION VALUES ("+id+",'"+lat+"','"+log+"')";
            BaseLocal.Insert(consulta, getApplicationContext());

            ti_nombre.setText("");
            ti_correo.setText("");

            Toast.makeText(AgregaColaboradorActivity.this, getString(R.string.msg_colaboradorAgre), Toast.LENGTH_SHORT).show();
            actualizarInformacion();
        }
    }

    private void actualizarInformacion()
    {
        List<Employees> empleadosCardViews = BaseLocal.ObtenerEmpleados(getApplicationContext());

        DataEmployess dataEmployess = new DataEmployess();
        dataEmployess.employees = empleadosCardViews;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("data")
                .add(dataEmployess)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregaColaboradorActivity.this, getString(R.string.msg_infoAct), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregaColaboradorActivity.this, getString(R.string.msg_errorSubir), Toast.LENGTH_LONG).show();
                    }
                });
    }
}