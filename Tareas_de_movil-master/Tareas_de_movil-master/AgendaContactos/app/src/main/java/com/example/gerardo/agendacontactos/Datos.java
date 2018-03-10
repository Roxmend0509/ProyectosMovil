package com.example.gerardo.agendacontactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Datos extends AppCompatActivity {

    Button Guardar;
    EditText nombre;
    EditText email;
    EditText twiter;
    EditText tel;
    EditText fec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);




            Guardar = (Button) findViewById(R.id.btnguardar);
            Guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nombre = (EditText) findViewById(R.id.txtnombre);
                    email = (EditText) findViewById(R.id.txtemail);
                    twiter = (EditText) findViewById(R.id.txttwiter);
                    tel = (EditText) findViewById(R.id.txttel);
                    fec = (EditText) findViewById(R.id.txtfecha);

                    if(validacion().length()==0) {
                        Contacto alum = new Contacto();
                        alum.setNombre(nombre.getText().toString());
                        alum.setemail(email.getText().toString());
                        alum.settwiter(twiter.getText().toString());
                        alum.settel(tel.getText().toString());
                        alum.setfec(fec.getText().toString());

                        Intent atras = new Intent();

                        atras.putExtra("micontacto", alum);

                        setResult(RESULT_OK, atras);
                        finish();

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
