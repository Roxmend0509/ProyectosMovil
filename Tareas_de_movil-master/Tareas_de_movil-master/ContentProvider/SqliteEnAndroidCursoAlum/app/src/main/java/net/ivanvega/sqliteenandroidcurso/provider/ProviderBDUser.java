package net.ivanvega.sqliteenandroidcurso.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import net.ivanvega.sqliteenandroidcurso.datos.DBUsuarios;
import net.ivanvega.sqliteenandroidcurso.datos.UsuariosDAO;

/**
 * Created by alcohonsilver on 01/02/18.
 */

public class ProviderBDUser extends ContentProvider {
    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table 3 are shown.
         */

        /*
         * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
         * in the path
         */
        sUriMatcher.addURI("net.ivanvega.sqliteenandroidcurso.provider", "usuario", 1);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.app.provider/table3/3" matches, but
         * "content://com.example.app.provider/table3 doesn't.
         */
        sUriMatcher.addURI("net.ivanvega.sqliteenandroidcurso.provider", "usuario/#", 2);

        sUriMatcher.addURI("net.ivanvega.sqliteenandroidcurso.provider", "usuario/*", 3);
    }

        private UsuariosDAO _dao;


    @Override
    public boolean onCreate() {

        _dao = new UsuariosDAO(this.getContext());

        return false;   // nota: revisar lo qur devielve

    }


    @Override
    public Cursor query(Uri uri, String[] strings, String s,
                        String[] strings1, String s1) {

        Cursor c =null;


        c = _dao .getAll();

        switch (sUriMatcher.match(uri)) {


            // If the incoming URI was for all of table3
            case 1:

                c = _dao .getAll();

                break;

            // If the incoming URI was for a single row
            case 2:

                c = _dao.getOneCursorByID( Integer.valueOf(  uri.getLastPathSegment()));
                break;

            case 3:
                c = _dao.getUserByCriterio(uri.getLastPathSegment());


            default:
                break;
                // If the URI is not recognized, you should do some error handling here.
        }
        // call the code to actually do the query

        return c;
    }






    @Override
    public String getType(Uri uri) {

        String tipomime="";

        switch (sUriMatcher.match(uri)) {


            // If the incoming URI was for all of table3
            case 1:

                tipomime="vnd.android.cursor.dir/vnd.net.ivanvega.sqliteenandroidcurso.provider.provider.usuario";

                break;

            // If the incoming URI was for a single row
            case 2:

                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                tipomime="vnd.android.cursor.item/vnd.net.ivanvega.sqliteenandroidcurso.provider.provider.usuario";
                break;

            case 3:

                tipomime="vnd.android.cursor.dir/vnd.net.ivanvega.sqliteenandroidcurso.provider.provider.usuario";
break;
            default:
                break;
            // If the URI is not recognized, you should do some error handling here.
        }
        // call the code to actually do the query

        return tipomime ;

    }



    SQLiteDatabase SQLDB;
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long registro = 0;
        try {
            //sUriMatcher.match(uri) == 1
            if (true) {
                SQLDB = _dao._db.getWritableDatabase();
                registro = SQLDB.insert("usuarios", null, values);
            }
        } catch (IllegalArgumentException e) {
           // Toast.makeText(getContext(),"Argumento no admitido",Toast.LENGTH_SHORT).show();
           Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        // Comprobar si se inserto bien el registro
        if (registro > 0) {
            //Toast.makeText(getContext(),"Registro creado correctamente",Toast.LENGTH_SHORT).show();
           Log.e("INSERT", "Registro creado correctamente");
        } else {
            Toast.makeText(getContext(),"Error al Insertar",Toast.LENGTH_SHORT).show();
           Log.e("Error", "Al insertar registro: " + registro);
        }

        return Uri.parse("usuario/" + registro);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int registro = 0;
        try {
            if (true) {
                String id = "_id=" + uri.getLastPathSegment();
                SQLDB = _dao._db.getWritableDatabase();
                registro = SQLDB.delete("usuarios", id, null);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return registro;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        String id = "";
        try {
            if (true) {
                id = uri.getLastPathSegment();
                SQLDB = _dao._db.getWritableDatabase();
                SQLDB.update("usuarios", values, "_id=" + id, selectionArgs);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return Integer.parseInt(id);
    }
}
