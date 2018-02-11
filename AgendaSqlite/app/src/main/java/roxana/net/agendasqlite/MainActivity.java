package roxana.net.agendasqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final Uri URI_CP = Uri.parse(
            "content://net.ivanvega.sqliteenandroidcurso.provider/usuarios");

    private Uri uri;
    private Cursor c;

    private int id;
    private String nombre;
    private String email;
    private String contrasenia;

    ArrayList<Integer> listaids = new ArrayList<>();
    ArrayList<String> listanombres = new ArrayList<>();
    ArrayList<String> listaemails = new ArrayList<>();
    ArrayList<String> listacontrasenias = new ArrayList<>();

    ListView lst ;

    int operacion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            lst = (ListView) findViewById(R.id.lst);
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                    //String cad = lst.getAdapter().getItem(i).toString();
                    _idUsuario = posicion;
                    // Toast.makeText(getBaseContext(), listaids.get(_idUsuario) + "", Toast.LENGTH_SHORT).show();
                    btnList_click();
                }

            });


            cargarUsuarios();
        }catch (Exception err){}
    }

    public void el(){
        listaids.remove(_idUsuario);
        listanombres.remove(_idUsuario);
        listaemails.remove(_idUsuario);
        listacontrasenias.remove(_idUsuario);
    }


    private ContentValues setVALORES(int id, String nom, String email, String contrasenia) {
        ContentValues valores = new ContentValues();
        //valores.put("_id", id);
        valores.put("nombre", nom);
        valores.put("email", email);
        valores.put("contrasenia", contrasenia);
        return valores;
    }

    public void obtenerIDs(){
        // Recuperamos todos los registros de la tabla
        try {
            ContentResolver CR = getContentResolver();
            listaids.clear();
            listanombres.clear();
            listaemails.clear();
            listacontrasenias.clear();

            String[] valores_recuperar = {"_id,nombre,email,contrasenia"};
            c = CR.query(URI_CP, valores_recuperar, null, null, null);
            c.moveToFirst();
            do {
                id = c.getInt(0);
                nombre = c.getString(1);
                email = c.getString(2);
                contrasenia = c.getString(3);

                listaids.add(id);
                listanombres.add(nombre);
                listaemails.add(email);
                listacontrasenias.add(contrasenia);

            } while (c.moveToNext());
        }catch (Exception err){
            //Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarUsuarios(){

        try {
            Cursor c = getContentResolver().query(Uri.parse(Usuario.CONTENT_URI), null, null,
                    null, null);

            SimpleCursorAdapter sca =
                    new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                            c, new String[]{
                            Usuario.FIELD_ID, Usuario.FIELD_NAME
                    },
                            new int[]{android.R.id.text1, android.R.id.text2},
                            SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                    );



            lst.setAdapter(sca);
            obtenerIDs();
        }catch (Exception err)
        {
            Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    int _idUsuario=0;
    private void eliminar() {

        try {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Elimniar usuario");
            adb.setMessage("Deseas eliminar el usuario?");
            adb.setIcon(R.mipmap.ic_launcher);
            adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver CR = getContentResolver();

                    String ideliminar = listaids.get(_idUsuario).toString();
                    uri = Uri.parse("content://net.ivanvega.sqliteenandroidcurso.provider/usuarios/" + ideliminar);
                    CR.delete(uri, null, null);

                    el();
                    cargarUsuarios();

                }
            });
            adb.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });

            adb.show();
        }catch (Exception err){}
    }


    public void dialogooperacioo(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_datos);


        final Button btnguardar = dialog.findViewById(R.id.btnguardar);
        final TextView txtnombre = dialog.findViewById(R.id.txtnombre);
        final TextView txtemail = dialog.findViewById(R.id.txtemail);
        final TextView txtpass = dialog.findViewById(R.id.txtcontrasenia);

        if(operacion==0) {
            dialog.setTitle("Agregar Usuariio");
            btnguardar.setText("Agregar");

            txtnombre.setText("");
            txtemail.setText("");
            txtpass.setText("");
        }else if(operacion==1){
            dialog.setTitle("Actializar Usuario");
            btnguardar.setText("Actualizar");

            txtnombre.setText(listanombres.get(_idUsuario));
            txtemail.setText(listaemails.get(_idUsuario));
            txtpass.setText(listacontrasenias.get(_idUsuario));
        }



        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentResolver CR = getContentResolver();

                // Insertar Registro en el Content Provider
                try {
                    if(operacion==0) {
                        CR.insert(URI_CP, setVALORES(0, txtnombre.getText().toString(), txtemail.getText().toString(), txtpass.getText().toString()));
                        Toast.makeText(getBaseContext(), "Dato Insertado", Toast.LENGTH_SHORT).show();
                    }else if(operacion==1){
                        String idactualizar = listaids.get(_idUsuario).toString();
                        uri = Uri.parse("content://net.ivanvega.sqliteenandroidcurso.provider/usuarios/"+idactualizar);
                        CR.update(uri, setVALORES(Integer.parseInt(idactualizar), txtnombre.getText().toString(), txtemail.getText().toString(), txtpass.getText().toString()),
                                null, null);
                        Toast.makeText(getBaseContext(), "Dato Actualizado", Toast.LENGTH_SHORT).show();
                    }
                    cargarUsuarios();

                }catch (Exception err){
                    //Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_SHORT).show();


                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add) {
            dialogooperacioo();
            operacion=0;
            return true;
        }else if(id==R.id.editar){
            cargarUsuarios();
        }

        return super.onOptionsItemSelected(item);
    }


    String operaciones[]= {"Eliminar","Actualizar"};
    public void  btnList_click(){

        AlertDialog dialog =
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("")
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                    eliminar();
                                }else if(operaciones[which].equalsIgnoreCase(operaciones[1])){
                                    operacion=1;
                                    dialogooperacioo();

                                }



                                dialog.dismiss();
                            }
                        })
                        .create();

        dialog.show();
    }


}
