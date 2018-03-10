package com.example.gerardo.myapplication.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.gerardo.myapplication.Pojos.Notas;
import com.example.gerardo.myapplication.Pojos.Recordatorio;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Gerardo on 29/10/2017.
 */

public class DaoRecordatorios {

    private Context _contexto;
    private SQLiteDatabase _midb;

    public DaoRecordatorios(Context contexto){
        this._contexto = contexto;
        this._midb = new MiDBOpenHelper(contexto).getWritableDatabase();
    }


    public int registros(){
        Cursor c = _midb.rawQuery("select * from Recordatorios",null);
         return c.getCount();
    }


    public long Insert(Recordatorio c){
        ContentValues cv = new ContentValues();
        try{
        cv.put(MiDBOpenHelper.COLUMNS_RECORDATORIOS[1],c.getFecha());
        cv.put(MiDBOpenHelper.COLUMNS_RECORDATORIOS[2],c.getHora());
        cv.put(MiDBOpenHelper.COLUMNS_RECORDATORIOS[3],c.getNota());
        }catch (Exception err){

        }
        return _midb.insert(MiDBOpenHelper.TABLE_RECORDATORIOS,null,cv) ;

    }

    public List<Recordatorio> notificacionescumplidas(String fecha) {
        List<Recordatorio> studentsArrayList = new ArrayList<Recordatorio>();
        try{
        String selectQuery = "SELECT  * FROM " + "Recordatorios WHERE Fecha='"+fecha+"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Recordatorio recordatorio = new Recordatorio();
                recordatorio.setId(c.getInt(c.getColumnIndex("_id")));
                recordatorio.setFecha(c.getString(c.getColumnIndex("Fecha")));
                recordatorio.setHora(c.getString(c.getColumnIndex("Hora")));
                recordatorio.setNota(c.getInt(c.getColumnIndex("Nota")));


                studentsArrayList.add(recordatorio);
            } while (c.moveToNext());
        }
        }catch (Exception err){

        }
        return studentsArrayList;
    }

    public long Update(Recordatorio c){
        ContentValues cv = new ContentValues();
        try{
        cv.put(MiDBOpenHelper.COLUMNS_RECORDATORIOS[1],c.getFecha());
        cv.put(MiDBOpenHelper.COLUMNS_RECORDATORIOS[2],c.getHora());
        cv.put(MiDBOpenHelper.COLUMNS_RECORDATORIOS[3],c.getNota());

        }catch (Exception err){

        }

        return _midb.update(MiDBOpenHelper.TABLE_RECORDATORIOS,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.getId())});

    }

    public int delete(String id){
        return  _midb.delete("Recordatorios","_id='"+id+"'",null);
    }

    public List<Recordatorio> getrecordatoriosnota(String id) {
        List<Recordatorio> studentsArrayList = new ArrayList<Recordatorio>();
        try{
        String selectQuery = "SELECT  * FROM " + "Recordatorios WHERE Nota='"+id+"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Recordatorio recordatorio = new Recordatorio();
                recordatorio.setId(c.getInt(c.getColumnIndex("_id")));
                recordatorio.setFecha(c.getString(c.getColumnIndex("Fecha")));
                recordatorio.setHora(c.getString(c.getColumnIndex("Hora")));
                recordatorio.setNota(c.getInt(c.getColumnIndex("Nota")));


                studentsArrayList.add(recordatorio);
            } while (c.moveToNext());
        }
        }catch (Exception err){

        }
        return studentsArrayList;
    }



    public List<Recordatorio> getAll(){
        List<Recordatorio> ls=null;

        try{
        Cursor c = _midb.query(MiDBOpenHelper.TABLE_RECORDATORIOS,
                MiDBOpenHelper.COLUMNS_RECORDATORIOS,
                null,
                null,
                null,
                null,
                MiDBOpenHelper.COLUMNS_RECORDATORIOS[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Recordatorio>();
            do {
                Recordatorio ctc = new Recordatorio();

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        MiDBOpenHelper.COLUMNS_MULTIMEDIA[0])
                        )
                );

                ctc.setId(c.getInt(0));
                ctc.setFecha(c.getString(1));
                ctc.setHora(c.getString(2));
                ctc.setNota(c.getInt(3));
                ls.add(ctc);

            }while(c.moveToNext());
        }

        }catch (Exception err){

        }
        return ls;
    }



}
