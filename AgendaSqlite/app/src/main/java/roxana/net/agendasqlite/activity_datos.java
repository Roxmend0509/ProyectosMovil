package roxana.net.agendasqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Rox on 27/10/2017.
 */

public class activity_datos extends AppCompatActivity {

    Button guardar;
    Button actualizar;
    Button eliminar;
    EditText id;
    EditText nombre;
    EditText email;
    EditText twiter;
    EditText tel;
    EditText fec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        actualizar = (Button)findViewById(R.id.btnactualizar);
        eliminar = (Button)findViewById(R.id.btneliminar);
        id = (EditText) findViewById(R.id.txtid);
        nombre = (EditText) findViewById(R.id.txtnombre);
        email = (EditText) findViewById(R.id.txtemail);
        twiter = (EditText) findViewById(R.id.txttwiter);
        tel = (EditText) findViewById(R.id.txttel);
        fec = (EditText) findViewById(R.id.txtfecha);


        insertar();
        buscarcontacto();
        actualizar();
        EliminarContacto();

    }

    public void buscarcontacto(){

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Pattern p = Pattern.compile("[0-9]+");
                DaoContactos dao = new DaoContactos(activity_datos.this);
                ArrayList<Contacto> contacto;

                contacto = (ArrayList<Contacto>) dao.obtenercontacto(id.getText().toString());
                if(id.getText().length()>0 && p.matcher(id.getText().toString()).matches()==true && contacto.size()>0) {
                    /*id.setText(contacto.get(0).getId());*/
                    nombre.setText(contacto.get(0).getNombre());
                    email.setText(contacto.get(0).getCorreo_electronico());
                    twiter.setText(contacto.get(0).getTwitter());
                    tel.setText(contacto.get(0).getTelefono());
                    fec.setText(contacto.get(0).getFecha_nacimiento());
                }else{
                    nombre.setText("");
                    email.setText("");
                    twiter.setText("");
                    tel.setText("");
                    fec.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    public void actualizar(){
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DaoContactos buscar = new DaoContactos(activity_datos.this);

                    Contacto contacto1 = new Contacto(Integer.parseInt(id.getText().toString()),nombre.getText().toString(),email.getText().toString(),twiter.getText().toString(),tel.getText().toString(),fec.getText().toString());
                    if(validacion().length()==0) {
                        if (buscar.update(contacto1) > 0) {
                            Toast.makeText(getBaseContext(), "Contacto Actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Ocurrio un Error al Actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception err){
                    Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void insertar(){
        guardar=(Button)findViewById(R.id.btnguardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent atras = new Intent();

                if(validacion().length()==0) {
                    Contacto alum = new Contacto();
                    alum.setNombre(nombre.getText().toString());
                    alum.setCorreo_electronico(email.getText().toString());
                    alum.setTwitter(twiter.getText().toString());
                    alum.setTelefono(tel.getText().toString());
                    alum.setFecha_nacimiento(fec.getText().toString());

                    atras.putExtra("micontacto", alum);

                    setResult(RESULT_OK, atras);
                    finish();
                }

            }
        });
    }

    public void EliminarContacto(){

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DaoContactos dao = new DaoContactos(activity_datos.this);
                    Pattern p = Pattern.compile("[0-9]+");
                    if(id.getText().length()>0 && p.matcher(id.getText().toString()).matches()==true) {
                        if(dao.delete(id.getText().toString())>0){
                            Toast.makeText(getBaseContext(),"Contacto Eliminado",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getBaseContext(),"Contacto no se pudo Eliminado",Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch (Exception err){
                    Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public String validacion(){

        String inconvenientes="";


        if(nombre.getText().toString().length()>0){
            nombre.setError(null);
        }else{
            inconvenientes+=">Nombre Obligatorio";
            nombre.setError("Nombre Obligatorio");

        }

        if(email.getText().toString().length()>0) {
            if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() == true) {
                email.setError(null);
            } else {
                inconvenientes += ">Correo Electronico Invalid";
                email.setError("Correo Invalido");
            }
        }

        if(tel.getText().toString().length()>0){
            tel.setError(null);
        }else{
            inconvenientes+=">Telefono Obligatorio";
            tel.setError("Telefono Obligatorio");

        }

        Pattern p = Pattern.compile("([0-9]{2})[/]([0-9]{2})[/]([0-9]{2})");
        if(p.matcher(fec.getText().toString()).matches()==true){
            fec.setError(null);
        }else{
            inconvenientes+=">Formato de Fecha Incorrecto";
            fec.setError("Formato de Fecha Incorrecto");
        }



        return inconvenientes;
    }

}

