package com.eavc.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreaCuentaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText ti_correo,ti_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_cuenta);

        mAuth = FirebaseAuth.getInstance();

        Button bt_registro, bt_salir;

        ti_correo = findViewById(R.id.ti_correo);
        ti_contrasena = findViewById( R.id.ti_pass );
        bt_registro = findViewById(R.id.bt_registro);
        bt_salir = findViewById(R.id.bt_salir);

        bt_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ti_correo.getText().toString().isEmpty() || ti_contrasena.getText().toString().isEmpty())
                {
                    Toast.makeText(CreaCuentaActivity.this, getString(R.string.msg_rellena), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registrar(ti_correo.getText().toString(), ti_contrasena.getText().toString());
                }
            }
        });

        bt_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void registrar(String correo, String pass)
    {
        mAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(CreaCuentaActivity.this, getString(R.string.msg_usuCreado), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(CreaCuentaActivity.this, getString(R.string.msg_usuError), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

}