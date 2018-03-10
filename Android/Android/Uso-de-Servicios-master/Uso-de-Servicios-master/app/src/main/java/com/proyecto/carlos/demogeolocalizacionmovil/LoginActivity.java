package com.proyecto.carlos.demogeolocalizacionmovil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button botonIngresar=(Button)findViewById(R.id.btnIngresar);

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread tr=new Thread(){
                    @Override
                    public void run() {

                       final String user= ((EditText)findViewById(R.id.editTextUser)).getText().toString();
                       final String password= ((EditText)findViewById(R.id.editTextPassword)).getText().toString();

                        final String resultado=login(user,password);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=obtenerDatosJSON(resultado);
                                if(r>0){
                                    Intent nuevoForm=new Intent(LoginActivity.this,MainActivity.class);
                                    nuevoForm.putExtra("usuario",user);
                                    startActivity(nuevoForm);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Credenciales Incorrectos",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });
    }

    public String login(String user, String password){
        URL url=null;
        String linea="";
        int resp=0;
        StringBuilder result=null;

        try{
            url=new URL("http://192.168.43.124:84/DemoGeolocalizacion/login.php?usuario="+user+"&clave="+password);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            resp=conn.getResponseCode();//Si hay rpta resp=200

            result=new StringBuilder();
            if(resp==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conn.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while((linea=reader.readLine())!=null){
                    result.append(linea);
                }
            }
        }catch (Exception e){

        }
        return result.toString();
    }

    public int obtenerDatosJSON(String response){
        int res=0;
        try{
            JSONArray jsonArray=new JSONArray(response);
            if(jsonArray.length()>0){
                res=1;
            }
        }catch (Exception e){}
        return res;
    }

}
