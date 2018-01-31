package roxana.net.aplicacion_notasmultimedia.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.channels.NotYetBoundException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rox on 19/10/2017.
 */

public class DAONotas {

    private Context _contexto;
    private SQLiteDatabase _midb;

    public DAONotas(Context contexto){
        this._contexto=contexto;
        this._midb= new MiDBOpenHelper(contexto).getWritableDatabase();
    }

    public long insert (Notas n){
        ContentValues cv=new ContentValues();

        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[1],n.getTitulo());
        cv.put(MiDBOpenHelper.COLUMNS_NOTAS[2],n.getDescripion());

        return _midb.insert(MiDBOpenHelper.TABLE_NOTAS_NAME,null,cv);
    }

    public List<Notas> getAll(){
        List<Notas> ls=null;

        Cursor c = _midb.query(MiDBOpenHelper.TABLE_NOTAS_NAME,
                MiDBOpenHelper.COLUMNS_NOTAS,
                null,
                null,
                null,
                null,
                MiDBOpenHelper.COLUMNS_NOTAS[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Notas>();
            do {
                Notas ctc = new Notas(
                );

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        MiDBOpenHelper.COLUMNS_NOTAS[0])
                        )
                );
                ctc.setTitulo(c.getString(1));
                ctc.setDescripion(c.getString(2));

                ls.add(ctc);

            }while(c.moveToNext());
        }

        return ls;
    }

}
