package roxana.net.aplicacion_notasmultimedia.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Rox on 19/10/2017.
 */

public class MiDBOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="mibasesitadatos";
    private static final int DB_VERSION=1;

    public static final String [] COLUMNS_NOTAS = {"_id","titulo","descripcion"};

    public static final String TABLE_NOTAS_NAME="notas";

    private final String  TABLE_NOTAS_SCRIPT="create table notas("+
            "_id integer primary key autoincrement," +
            "titulo varchar(100) not null,"+
            "descripcion varchar(500) null);";



    public MiDBOpenHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_NOTAS_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTIS notas");
        onCreate(db);
    }
}
