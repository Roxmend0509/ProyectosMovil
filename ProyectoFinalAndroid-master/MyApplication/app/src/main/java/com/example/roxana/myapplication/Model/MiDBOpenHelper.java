package com.example.roxana.myapplication.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MiDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notas";
    private static final int DB_VERSION = 1;
    public static final String[] COLUMNS_NOTAS = {"_id", "Titulo", "Descripcion", "fecha","hora","estado","tipo"};
    public static final String[] COLUMNS_MULTIMEDIA = {"_id", "Archivo", "Descripcion", "Nota"};
    public static final String[] COLUMNS_RECORDATORIOS = {"_id", "Fecha","Hora","Nota"};
    public static final String TABLE_NOTAS = "Notas";
    public static final String TABLE_MULTIMEDIA = "Multimedia";
    public static final String TABLE_RECORDATORIOS = "recordatorios";

    private  final String TABLE_NOTAS_SCRIPT = "create table Notas ("+
            COLUMNS_NOTAS[0]+" integer primary key autoincrement, "+
            COLUMNS_NOTAS[1]+" varchar(50) not null," +
            COLUMNS_NOTAS[2]+" text not null,"+
            COLUMNS_NOTAS[3]+" varchar(30) null,"+
            COLUMNS_NOTAS[4]+" date null,"+
            COLUMNS_NOTAS[5]+" char(1) null,"+
            COLUMNS_NOTAS[6]+" char(1) null);"; //1 nota  2 tarea

    private  final String TABLE_RECORDATORIOS_SCRIPT = "create table Recordatorios ("+
            COLUMNS_RECORDATORIOS[0]+" integer primary key autoincrement, "+
            COLUMNS_RECORDATORIOS[1]+" date not null," +
            COLUMNS_RECORDATORIOS[2]+" varchar(10) not null,"+
            COLUMNS_RECORDATORIOS[3]+" Nota int not null, foreign key (Nota) references Notas(_id) on delete cascade);";

    private  final String TABLE_MULTIMEDIA_SCRIPT = "create table Multimedia ("+
            COLUMNS_MULTIMEDIA[0]+" integer primary key autoincrement, "+
            COLUMNS_MULTIMEDIA[1]+" varchar(100) not null," +
            COLUMNS_MULTIMEDIA[2]+" text not null,"+
            COLUMNS_MULTIMEDIA[3]+" Nota int not null, foreign key (Nota) references Notas(_id) on delete cascade);";


    public MiDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(TABLE_NOTAS_SCRIPT);
            sqLiteDatabase.execSQL(TABLE_RECORDATORIOS_SCRIPT);
            sqLiteDatabase.execSQL(TABLE_MULTIMEDIA_SCRIPT);
        }catch (Exception err){}
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try{
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Notas");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Recordatorios");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Multimedia");
            onCreate(sqLiteDatabase);
        }catch (Exception err){

        }
    }


}
