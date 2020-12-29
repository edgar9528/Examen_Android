package com.eavc.examen.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper  extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tabla1= "CREATE TABLE EMPLOYEES("+
                        "ID_EMPLOYEE INT PRIMARY KEY,"+
                        "NAME VARCHAR(50),"+
                        "MAIL VARCHAR(30))";

        String tabla2= "CREATE TABLE LOCATION("+
                        "ID_LOCATION INT UNIQUE,"+
                        "LAT VARCHAR(20),"+
                        "LOG VARCHAR(20),"+
                        "FOREIGN KEY(ID_LOCATION) REFERENCES EMPLOYEES(ID_EMPLOYEE) )";

        db.execSQL(tabla1);
        db.execSQL(tabla2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
