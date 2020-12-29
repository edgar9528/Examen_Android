package com.eavc.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eavc.examen.dataBase.BaseLocal;
import com.eavc.examen.model.EmpleadoLocal;
import com.eavc.examen.model.Employees;
import com.eavc.examen.model.Ubicacion;
import com.eavc.examen.model.UbicacionLocal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Vista2Activity extends AppCompatActivity {

    List<Employees> empleadosCardViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Button button_salir = findViewById(R.id.bt_salir);
        button_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        int indice = getIntent().getIntExtra("indice",0);

        empleadosCardViews = BaseLocal.ObtenerEmpleados(getApplicationContext());


        String lat = empleadosCardViews.get(indice).location.lat;
        String log = empleadosCardViews.get(indice).location.log;

        final float finalLat = Float.parseFloat(lat);
        final float finalLon = Float.parseFloat(log);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear();

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(finalLat, finalLon))
                        .zoom(15)
                        .tilt(10)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3000, null);

                for(int i=0; i<empleadosCardViews.size();i++)
                {
                    float finalLon = Float.parseFloat(empleadosCardViews.get(i).location.log);
                    float finalLat = Float.parseFloat(empleadosCardViews.get(i).location.lat);

                    Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(finalLat, finalLon))
                                        .title(empleadosCardViews.get(i).name));
                    if(i==indice)
                        marker.showInfoWindow();

                }
            }
        });




    }

}