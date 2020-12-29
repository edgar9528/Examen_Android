package com.eavc.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eavc.examen.dataBase.DataBaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText ti_correo,ti_contrasena;
    boolean tienePermisos = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button bt_iniciar;
        TextView tv_creaCuenta;

        ti_correo = findViewById(R.id.ti_correo);
        ti_contrasena = findViewById( R.id.ti_pass );
        bt_iniciar = findViewById(R.id.bt_iniciar);
        tv_creaCuenta = findViewById(R.id.tv_creaCuenta);

        verificarPermisos();

        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(tienePermisos)
                {
                    if(ti_correo.getText().toString().isEmpty() || ti_contrasena.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this, R.string.msg_rellena, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        autenticar(ti_correo.getText().toString(), ti_contrasena.getText().toString());
                    }
                }
                else
                    Toast.makeText(MainActivity.this, getString(R.string.msg_permisos), Toast.LENGTH_SHORT).show();
            }
        });

        tv_creaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreaCuentaActivity.class);
                startActivity(intent);
            }
        });

        crearBaseLocal();

    }


    private void autenticar(String correo, String pass )
    {
        mAuth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText( MainActivity.this, getString(R.string.msg_usuIncorrecto), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void crearBaseLocal()
    {
        try
        {
            DataBaseHelper databaseHelper = new DataBaseHelper(getApplication(), "dataBase.db", null, 1);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.close();
        }catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Error"+ e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }


    private void verificarPermisos()
    {
        int hasWriteContactsPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

            // request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){

            tienePermisos=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(1 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tienePermisos=true;
            } else {
                tienePermisos=false;
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}