package roxana.net.agendasqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rox on 27/10/2017.
 */

public class DaoContactos {
    private Context _contexto;
    private SQLiteDatabase _midb;

    public DaoContactos(Context contexto){
        this._contexto = contexto;
        this._midb = new MiDBOpenHelper(contexto).getWritableDatabase();
    }


    public long insert(Contacto c){

        ContentValues cv = new ContentValues();

        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[1],c.getNombre());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[2],c.getCorreo_electronico());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[3],c.getTwitter());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[4],c.getTelefono());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[5],c.getFecha_nacimiento());

        return _midb.insert(MiDBOpenHelper.TABLE_CONTACTOS_NAME,null,cv) ;

    }



    public long update(Contacto c){
        ContentValues cv = new ContentValues();

        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[1],c.getNombre());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[2],c.getCorreo_electronico());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[3],c.getTwitter());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[4],c.getTelefono());
        cv.put(MiDBOpenHelper.COLUMNS_CONTACTOS[5],c.getFecha_nacimiento());

        return _midb.update(MiDBOpenHelper.TABLE_CONTACTOS_NAME,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.id)});

    }

    public int delete(String id){
        return  _midb.delete("contactos","_id='"+id+"'",null);
    }

    public List<Contacto> getAll(){
        List<Contacto> ls=null;

        Cursor c = _midb.query(MiDBOpenHelper.TABLE_CONTACTOS_NAME,
                MiDBOpenHelper.COLUMNS_CONTACTOS,
                null,
                null,
                null,
                null,
                MiDBOpenHelper.COLUMNS_CONTACTOS[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Contacto>();
            do {
                Contacto ctc = new Contacto();

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        MiDBOpenHelper.COLUMNS_CONTACTOS[0])
                        )
                );

                ctc.setId(c.getInt(0));
                ctc.setNombre(c.getString(1));
                ctc.setCorreo_electronico(c.getString(2));
                ctc.setTwitter(c.getString(3));
                ctc.setTelefono(c.getString(4));
                ctc.setFecha_nacimiento(c.getString(5));

                ls.add(ctc);

            }while(c.moveToNext());
        }

        return ls;
    }


    public List<Contacto> Buscar(String nombre){
        List<Contacto> ls=null;

        Cursor c = _midb.query(MiDBOpenHelper.TABLE_CONTACTOS_NAME,
                MiDBOpenHelper.COLUMNS_CONTACTOS,
                null,
                null,
                null,
                null,
                MiDBOpenHelper.COLUMNS_CONTACTOS[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Contacto>();
            do {
                Contacto ctc = new Contacto();

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        MiDBOpenHelper.COLUMNS_CONTACTOS[0])
                        )
                );

                ctc.setId(c.getInt(0));
                ctc.setNombre(c.getString(1));
                ctc.setCorreo_electronico(c.getString(2));
                ctc.setTwitter(c.getString(3));
                ctc.setTelefono(c.getString(4));
                ctc.setFecha_nacimiento(c.getString(5));


                if(c.getString(1).toUpperCase().startsWith(nombre.toUpperCase())) {
                    ls.add(ctc);
                }

            }while(c.moveToNext());
        }

        return ls;
    }

    public List<Contacto> getAllStudentsList() {
        List<Contacto> studentsArrayList = new ArrayList<Contacto>();
        String selectQuery = "SELECT  * FROM " + "contactos";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Contacto students = new Contacto();
                students.id = c.getInt(c.getColumnIndex("_id"));
                students.nombre = c.getString(c.getColumnIndex("nombre"));
                students.correo_electronico = c.getString(c.getColumnIndex("correo_electronico"));
                students.twitter = c.getString(c.getColumnIndex("twitter"));
                students.telefono = c.getString(c.getColumnIndex("telefono"));
                students.fecha_nacimiento = c.getString(c.getColumnIndex("fecha_nacimiento"));
                studentsArrayList.add(students);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }

    public List<Contacto> buscarcontacto(String name) {
        List<Contacto> studentsArrayList = new ArrayList<Contacto>();
        String selectQuery = "SELECT  * FROM contactos WHERE nombre LIKE '"+name+"%'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Contacto students = new Contacto();
                students.id = c.getInt(c.getColumnIndex("_id"));
                students.nombre = c.getString(c.getColumnIndex("nombre"));
                students.correo_electronico = c.getString(c.getColumnIndex("correo_electronico"));
                students.twitter = c.getString(c.getColumnIndex("twitter"));
                students.telefono = c.getString(c.getColumnIndex("telefono"));
                students.fecha_nacimiento = c.getString(c.getColumnIndex("fecha_nacimiento"));
                studentsArrayList.add(students);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }



    public ArrayList<Contacto> obtenercontacto(String id) {
        ArrayList<Contacto> studentsArrayList = new ArrayList<Contacto>();
        String selectQuery = "select * from contactos where _id='"+id+"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Contacto students = new Contacto();
                students.id = c.getInt(c.getColumnIndex("_id"));
                students.nombre = c.getString(c.getColumnIndex("nombre"));
                students.correo_electronico = c.getString(c.getColumnIndex("correo_electronico"));
                students.twitter = c.getString(c.getColumnIndex("twitter"));
                students.telefono = c.getString(c.getColumnIndex("telefono"));
                students.fecha_nacimiento = c.getString(c.getColumnIndex("fecha_nacimiento"));
                studentsArrayList.add(students);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }


}