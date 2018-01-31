package com.example.roxana.myapplication.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.roxana.myapplication.Pojos.Notas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;



public class DaoNotas {//

    private Context _contexto;
    private SQLiteDatabase _midb;

    public DaoNotas(Context contexto){
        this._contexto = contexto;
        this._midb = new MiDBOpenHelper(contexto).getWritableDatabase();
    }

    public long Insert(Notas c){
        ContentValues cv = new ContentValues();
        try {
            cv.put(MiDBOpenHelper.COLUMNS_NOTAS[1], c.getTitulo());
            cv.put(MiDBOpenHelper.COLUMNS_NOTAS[2], c.getDescripcion());
            cv.put(MiDBOpenHelper.COLUMNS_NOTAS[3], c.getFecha());
            cv.put(MiDBOpenHelper.COLUMNS_NOTAS[4], c.getHora());
            cv.put(MiDBOpenHelper.COLUMNS_NOTAS[5], c.isEstado());
            cv.put(MiDBOpenHelper.COLUMNS_NOTAS[6], c.getTipo());
        }catch (Exception err){}
        return _midb.insert(MiDBOpenHelper.TABLE_NOTAS,null,cv) ;

    }

    public long Update(Notas c){
        ContentValues cv = new ContentValues();
        try{
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[1],c.getTitulo());
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[2],c.getDescripcion());
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[3],c.getFecha());
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[4],c.getHora());
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[5],c.isEstado());
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[6],c.getTipo());

        }catch (Exception err){}

        return _midb.update(MiDBOpenHelper.TABLE_NOTAS,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.getId())});

    }

      public int delete(String id){
        return  _midb.delete("Notas","_id='"+id+"'",null);
      }



    public List<Notas> getAll(){
        List<Notas> ls=null;

        try{
        Cursor c = _midb.query(MiDBOpenHelper.TABLE_NOTAS,
                MiDBOpenHelper.COLUMNS_NOTAS,
                null,
                null,
                null,
                null,
                MiDBOpenHelper.COLUMNS_NOTAS[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Notas>();
            do {
                Notas ctc = new Notas();

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        MiDBOpenHelper.COLUMNS_NOTAS[0])
                        )
                );

                ctc.setId(c.getInt(0));
                ctc.setTitulo(c.getString(1));
                ctc.setDescripcion(c.getString(2));
                ctc.setFecha(c.getString(3));
                ctc.setHora(c.getString(4));

                ls.add(ctc);

            }while(c.moveToNext());
        }
        }catch (Exception err){}
        return ls;
    }


    public List<Notas> getnotas() {
        List<Notas> notasArrayList = new ArrayList<Notas>();
        try{
            String selectQuery = "SELECT  * FROM " + "Notas where tipo='1'";
            Log.d("", selectQuery);
            SQLiteDatabase db = this._midb;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    Notas nota = new Notas();
                    nota.setId(c.getInt(c.getColumnIndex("_id")));
                    nota.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                    nota.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                    nota.setFecha(c.getString(c.getColumnIndex("fecha")));
                    nota.setHora(c.getString(c.getColumnIndex("hora")));
                    nota.setEstado(c.getString(c.getColumnIndex("estado")));
                    nota.setTipo(c.getString(c.getColumnIndex("tipo")));
                    notasArrayList.add(nota);
                } while (c.moveToNext());
            }
        }catch (Exception err){
            Toast.makeText(_contexto,"no se pudieron cargar las Notas",Toast.LENGTH_SHORT).show();
        }
        return notasArrayList;
    }

    public List<Notas> gettareas() {
        List<Notas> notasArrayList = new ArrayList<Notas>();
        try{
        String selectQuery = "SELECT  * FROM " + "Notas where tipo='2' order by fecha ASC";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Notas nota = new Notas();
                nota.setId(c.getInt(c.getColumnIndex("_id")));
                nota.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                nota.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                nota.setFecha(c.getString(c.getColumnIndex("fecha")));
                nota.setHora(c.getString(c.getColumnIndex("hora")));
                nota.setEstado(c.getString(c.getColumnIndex("estado")));
                nota.setTipo(c.getString(c.getColumnIndex("tipo")));
                notasArrayList.add(nota);
            } while (c.moveToNext());
        }
        }catch (Exception err){
            Toast.makeText(_contexto,"no se pudieron cargar las Tareas",Toast.LENGTH_SHORT).show();
        }
        return notasArrayList;
    }

    public Notas getultimoelemento() {
        Notas nota = new Notas();
        try{
            String selectQuery = "SELECT  * FROM Notas order by _id desc limit 1";
            Log.d("", selectQuery);
            SQLiteDatabase db = this._midb;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    nota.setId(c.getInt(c.getColumnIndex("_id")));
                    nota.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                    nota.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                    nota.setFecha(c.getString(c.getColumnIndex("fecha")));
                    nota.setHora(c.getString(c.getColumnIndex("hora")));
                    nota.setEstado(c.getString(c.getColumnIndex("estado")));
                    nota.setTipo(c.getString(c.getColumnIndex("tipo")));
                    break;
                } while (c.moveToNext());
            }
        }catch (Exception err){
            Toast.makeText(_contexto,"no se pudieron Mostrar los datos",Toast.LENGTH_SHORT).show();
        }
        return nota;
    }


    public List<Notas> buscarnotaotarea(String tipo ,String texto) {
        List<Notas> notasArrayList = new ArrayList<Notas>();
        try{
        String selectQuery = "SELECT  * FROM " + "Notas where tipo='"+tipo+"' and (Titulo like '"+texto+"%' or Descripcion like '%"+texto+"%')";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Notas nota = new Notas();
                nota.setId(c.getInt(c.getColumnIndex("_id")));
                nota.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                nota.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                nota.setFecha(c.getString(c.getColumnIndex("fecha")));
                nota.setHora(c.getString(c.getColumnIndex("hora")));
                nota.setEstado(c.getString(c.getColumnIndex("estado")));
                nota.setTipo(c.getString(c.getColumnIndex("tipo")));
                notasArrayList.add(nota);
            } while (c.moveToNext());
        }
        }catch (Exception err){}
        return notasArrayList;
    }

    public Notas obtenerNotaoTarea(String id) {
        Notas nota = new Notas();
        try{
        String selectQuery = "SELECT  * FROM Notas where _id='"+id+"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                nota.setId(c.getInt(c.getColumnIndex("_id")));
                nota.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                nota.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                nota.setFecha(c.getString(c.getColumnIndex("fecha")));
                nota.setHora(c.getString(c.getColumnIndex("hora")));
                nota.setEstado(c.getString(c.getColumnIndex("estado")));
                nota.setTipo(c.getString(c.getColumnIndex("tipo")));
            } while (c.moveToNext());
        }
        }catch (Exception err){
            Toast.makeText(_contexto,"no se pudieron Mostrar los datos",Toast.LENGTH_SHORT).show();
        }
        return nota;
    }





}//termina
