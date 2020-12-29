package com.eavc.examen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eavc.examen.dataBase.BaseLocal;
import com.eavc.examen.model.EmpleadoLocal;
import com.eavc.examen.adapter.EmpleadosAdapterRecyclerView;
import com.eavc.examen.model.Employees;
import com.eavc.examen.model.Ubicacion;
import com.eavc.examen.model.UbicacionLocal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Vista1Activity extends AppCompatActivity {

    EmpleadosAdapterRecyclerView empleadosAdapterRecyclerView;
    RecyclerView empleadosRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    List<Employees> empleadosCardViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista1);

        empleadosRecyclerView = findViewById(R.id.empleadosRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        empleadosRecyclerView.setLayoutManager(linearLayoutManager);

        Button button_salir = findViewById(R.id.bt_salir);
        button_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        empleadosCardViews = BaseLocal.ObtenerEmpleados(getApplicationContext());

        empleadosAdapterRecyclerView = new EmpleadosAdapterRecyclerView(empleadosCardViews, R.layout.cardview_empleados,this);
        empleadosRecyclerView.setAdapter(empleadosAdapterRecyclerView);
    }

    public void seleccionar(int indice)
    {
        Intent intent = new Intent(Vista1Activity.this, Vista2Activity.class);
        intent.putExtra("indice",indice);
        startActivity(intent);
    }

}