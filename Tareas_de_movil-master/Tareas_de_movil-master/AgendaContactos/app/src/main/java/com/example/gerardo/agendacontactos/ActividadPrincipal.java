package com.example.gerardo.agendacontactos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActividadPrincipal extends AppCompatActivity {

    private Button agregarContacto;
    private ListView viewcontactos;
    private ArrayList<String> listacontactos = new ArrayList<String>();
    private ArrayAdapter<String> adaptador;

    private TextView lb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);



        agregarContacto =(Button)findViewById(R.id.AGREGARCONTACTO);
        agregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siguiente = new Intent(getApplication(),Datos.class);
                startActivityForResult(siguiente,1000);
                //Toast.makeText(getApplicationContext(),"HOLA",Toast.LENGTH_LONG).show();
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK)
        {

            try {


                //obtener el objeto contacto
                Contacto objcontacto = (Contacto) data.getSerializableExtra("micontacto");
                String armarcontacto = "Nombre:"+objcontacto.getNombre()+"\nEmail:"+objcontacto.getemail()+"\nTwiter:"+objcontacto.gettwiter()+"\nTelefono:"+objcontacto.gettel()+"\nFecha:"+objcontacto.getfec()+"\n";
                listacontactos.add(armarcontacto);

                adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listacontactos);

                viewcontactos = (ListView) findViewById(R.id.listcontactos);
                viewcontactos.setAdapter(adaptador);
                adaptador.notifyDataSetChanged();


            }catch (Exception err){
                Toast.makeText(getApplicationContext(), err.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

/*
// RECUPERAR ESTADO AL GIRAR LA PANTALLA
    private String datos="";
    @Override
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        try{
        for (int i=0;i<listacontactos.size();i++){
            datos+=listacontactos.get(i)+"%";
        }
        datos = datos.substring(0,datos.length()-1);
        guardarEstado.putString("contactos", datos);
        }catch (Exception err){}
    }


    @Override
    protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        try {
            datos = recEstado.getString("contactos");

            String[] lista = datos.split("%");
            for (int i=0;i<datos.length();i++){
               listacontactos.add(lista[i]);
            }

        }catch (Exception err){}
    }
*/


}
