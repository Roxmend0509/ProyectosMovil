package com.proyecto.carlos.demogeolocalizacionmovil;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LugaresActivity extends AppCompatActivity {

    String resultado;
    ListView listlugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugares);

        listlugares = (ListView)findViewById(R.id.listlugares);


        resultado=getIntent().getExtras().getString("resultado");

        mostrarLugares(resultado);


    }

    public void mostrarLugares(String resultado){
        try {
            JSONArray r = obtenerDatosJSON(resultado);

            ArrayList<String> arreglo = new ArrayList<String>();

            for (int i = 0; i < r.length(); i++) {
                arreglo.add(r.getString(i));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arreglo);
            listlugares.setAdapter(adapter);
        } catch (Exception e) {}
    }

    public JSONArray obtenerDatosJSON(String response){
        JSONArray res=null;
        try{
            if(response.length()>0){
                res=new JSONArray(response);
            }
        }catch (JSONException e){}
        return res;
    }
}
