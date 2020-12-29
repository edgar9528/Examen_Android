package com.eavc.examen.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eavc.examen.model.EmpleadoLocal;
import com.eavc.examen.model.Employees;
import com.eavc.examen.model.Ubicacion;
import com.eavc.examen.model.UbicacionLocal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseLocal {

    public static void Insert(String consulta, Context context)
    {
        try {
            DataBaseHelper databaseHelper = new DataBaseHelper(context, "dataBase.db", null, 1);
            SQLiteDatabase bd = databaseHelper.getWritableDatabase();
            bd.execSQL(consulta);
            bd.close();
        }
        catch (Exception e)
        {
            Log.d("salida","Error baseLocal Insert "+ e.getMessage());
        }
    }

    public static String SelectDato(String consulta, Context context)
    {
        String datoReturn=null;
        try {

            DataBaseHelper databaseHelper = new DataBaseHelper(context, "dataBase.db", null, 1);
            SQLiteDatabase bd = databaseHelper.getReadableDatabase();

            Cursor cursor = bd.rawQuery(consulta, null);

            if(cursor.moveToNext())
            {
                datoReturn= cursor.getString(0 );
            }

            bd.close();

        }
        catch (Exception e)
        {
            Log.d("salida","Error baseL: "+e.toString());
            datoReturn=null;
        }

        return datoReturn;
    }

    public static String Select(String consulta, Context context)
    {
        String json=null;

        DataBaseHelper databaseHelper = new DataBaseHelper(context, "dataBase.db", null, 1);
        SQLiteDatabase bd = databaseHelper.getReadableDatabase();

        try {

            Cursor cursor = bd.rawQuery(consulta, null);

            if(cursor.getCount()>0)
                Log.d("salida","encontro info en la bd (Base local) "+consulta);

            json = cur2Json(cursor);

            bd.close();

        }
        catch (Exception e)
        {
            Log.d("salida","Error baseLocal Select: "+e.toString());
            json=null;
        }
        finally {
            if(bd.isOpen())
                bd.close();
        }

        return json;
    }

    private static String cur2Json(Cursor cursor)
    {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {

                if (cursor.getColumnName(i) != null) {
                    try
                    {
                        String name = cursor.getColumnName(i);
                        if(name.contains("."))
                            name=name.substring(name.indexOf(".")+1);

                        if(cursor.getString(i)==null)
                            rowObject.put(name, "");
                        else
                            rowObject.put(name, cursor.getString(i));

                    } catch (Exception e) {
                        Log.d("salida","Error cursor2json: "+ e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet.toString();
    }

    public static List<Employees> ObtenerEmpleados(Context context)
    {
        try
        {
            List<Employees> empleadosList = new ArrayList<>();

            //para obtener la información almacenada localmente y convertirla a los objetos
            String json1 = BaseLocal.Select("SELECT * FROM EMPLOYEES", context);
            String json2 = BaseLocal.Select("SELECT * FROM LOCATION", context);

            Gson gson = new Gson();
            EmpleadoLocal[] empleados = gson.fromJson(json1, EmpleadoLocal[].class);
            UbicacionLocal[] ubicaciones = gson.fromJson(json2, UbicacionLocal[].class);

            for (int i = 0; i < empleados.length; i++) {
                EmpleadoLocal e = empleados[i];
                Ubicacion u = new Ubicacion(ubicaciones[i].getLAT(), ubicaciones[i].getLOG());
                Employees employee = new Employees(e.getID_EMPLOYEE(), e.getNAME(), u, e.getMAIL());
                empleadosList.add(employee);
            }

            return empleadosList;

        }catch (Exception e)
        {
            return null;
        }
    }

}
