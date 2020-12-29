package com.eavc.examen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eavc.examen.dataBase.BaseLocal;
import com.eavc.examen.model.ArchivoEmployees;
import com.eavc.examen.model.DataEmployess;
import com.eavc.examen.model.EmpleadoLocal;
import com.eavc.examen.model.Employees;
import com.eavc.examen.model.JsonPlaceHolder;
import com.eavc.examen.model.NodoPrincipal;
import com.eavc.examen.model.Ubicacion;
import com.eavc.examen.model.UbicacionLocal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button bt1 = findViewById(R.id.bt1);
        Button bt2 = findViewById(R.id.bt2);
        Button bt3 = findViewById(R.id.bt3);
        Button bt4 = findViewById(R.id.bt4);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Vista1Activity.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AgregaColaboradorActivity.class);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Employees> empleadosCardViews = BaseLocal.ObtenerEmpleados(getApplicationContext());

                DataEmployess dataEmployess = new DataEmployess();
                dataEmployess.employees = empleadosCardViews;

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("data")
                        .add(dataEmployess)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MenuActivity.this, getString(R.string.msg_infoAct), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MenuActivity.this, getString(R.string.msg_errorSubir), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        obtenerURL();

    }

    private void obtenerURL()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dl.dropboxusercontent.com/s/5u21281sca8gj94/")
                .build();

        JsonPlaceHolder service = retrofit.create(JsonPlaceHolder.class);
        Call<NodoPrincipal> request = service.getInformacion();

        request.enqueue(new Callback<NodoPrincipal>() {
            @Override
            public void onResponse(Call<NodoPrincipal> call, Response<NodoPrincipal> response) {
                if(response.isSuccessful())
                {
                    NodoPrincipal nodo = response.body();
                    descargarArchivo(nodo.data.file);
                }
            }

            @Override
            public void onFailure(Call<NodoPrincipal> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void descargarArchivo(String ruta)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dl.dropboxusercontent.com/s/5u21281sca8gj94/")
                .build();

        JsonPlaceHolder service = retrofit.create(JsonPlaceHolder.class);
        Call<ResponseBody> request = service.downloadFile(ruta);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    boolean archivoGuardado = guardarArchivo(response.body());
                    if(archivoGuardado)
                    {
                        File archivoDescargado = new File(Environment.getExternalStorageDirectory() + File.separator + "employees_data.json.zip");
                        File archivoDescomprimir = new File(Environment.getExternalStorageDirectory() + File.separator);

                        boolean archivoDescomprimido = descomprimir(archivoDescargado,archivoDescomprimir);

                        if(archivoDescomprimido)
                        {
                            String jsonEmployees = leerArchivo();
                            Gson gson = new Gson();
                            ArchivoEmployees archivoEmployees = gson.fromJson(jsonEmployees, ArchivoEmployees.class);
                            guardarEmpleados(archivoEmployees);
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("salida", "Error: "+t.getMessage());
            }
        });

    }

    private boolean guardarArchivo(ResponseBody body) {
        try
        {

            File rutaArchivo = new File(Environment.getExternalStorageDirectory() + File.separator + "employees_data.json.zip");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try
            {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(rutaArchivo);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("File Download: " , fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e)
            {
                Log.d("salida", "Error: "+e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e)
        {
            Log.d("salida", "Error: "+e.getMessage());
            return false;
        }
    }

    private boolean descomprimir(File zipFile, File folderToExtract) {

        if(!zipFile.exists()) {
            Toast.makeText(this,getString(R.string.msg_noExiste), Toast.LENGTH_SHORT).show();
            return false;
        }

        try
        {

            int totalFilesInZip = countFiles(zipFile);
            int countFiles = 0;

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry;
            byte[] buffer = new byte[1024];
            int count;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                if(zipEntry.isDirectory()) {

                    File f = new File(folderToExtract.getPath() + zipEntry.getName());
                    if(!f.isDirectory()) {
                        f.mkdirs();
                    }

                } else {

                    ++countFiles;
                    Log.v("salida", "Unzipping " + " File " + countFiles + "/" + totalFilesInZip + " Name: " + zipEntry.getName());

                    File fileOut = new File(folderToExtract, zipEntry.getName());
                    FileOutputStream fileOutputStream = new FileOutputStream(fileOut);
                    while ((count = zipInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, count);
                    }

                    zipInputStream.closeEntry();
                    fileOutputStream.close();
                }

            }
            zipInputStream.close();

            return true;

        } catch(Exception e) {
            Log.e("salida", "Unzip file " + zipFile.getName(), e);
            return false;
        }

    }

    private int countFiles(File zipFile) throws IOException {
        ZipFile xzipFile = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = xzipFile.entries();
        int numRegularFiles = 0;
        while (entries.hasMoreElements()) {
            if (! entries.nextElement().isDirectory()) {
                ++numRegularFiles;
            }
        }
        return numRegularFiles;
    }

    private String leerArchivo()
    {
        try
        {
            File archivo = new File(Environment.getExternalStorageDirectory() + File.separator + "employees_data.json");

            FileReader fileReader = new FileReader(archivo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String responce = stringBuilder.toString();
            return responce;

        } catch(Exception ex) {
            Log.d("salida", "Error al leer el fichero desde la memoria interna");
            return null;
        }
    }

    private void guardarEmpleados(ArchivoEmployees archivoEmployees)
    {
        ArrayList<EmpleadoLocal> empleados = new ArrayList<>();
        ArrayList<Ubicacion> ubicaciones = new ArrayList<>();

        for(int i=0; i< archivoEmployees.data.employees.size();i++)
        {
            Employees e = archivoEmployees.data.employees.get(i);
            empleados.add(new EmpleadoLocal(e.id,e.name,e.mail));
            ubicaciones.add(e.location);
        }

        String consulta;
        for(int i=0; i< empleados.size();i++)
        {
            EmpleadoLocal e = empleados.get(i);
            Ubicacion u = ubicaciones.get(i);

            consulta = "INSERT INTO EMPLOYEES VALUES ("+e.getID_EMPLOYEE()+",'"+e.getNAME()+"','"+e.getMAIL()+"')";
            BaseLocal.Insert( consulta, getApplicationContext() );
            consulta = "INSERT INTO LOCATION VALUES ("+e.getID_EMPLOYEE()+",'"+u.lat+"','"+u.log+"')";
            BaseLocal.Insert( consulta, getApplicationContext() );

        }

    }

}