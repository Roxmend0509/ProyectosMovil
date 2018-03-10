package com.proyecto.carlos.demogeolocalizacionmovil;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final Long MIN_TIME_LOCATION_UPDATE = 2000L; // 2 seg
    public  static final Float MIN_DISTANCE_LOCATION_UPDATE = 0f;

    private LocationManager locationManager;

    private Location currentLocation;

    private Button btnObtener;
    private Button btnGuardar;
    private Button btnVer;
    private TextView tvdireccion;
    private TextView tvlatitud;
    private TextView tvlongitud;

    private double latitud;
    private double longitud;
    private String direccion;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnObtener = (Button) findViewById(R.id.btn_obtener);
        btnGuardar = (Button) findViewById(R.id.btn_guardar);
        btnVer = (Button) findViewById(R.id.btn_ver);
        tvlatitud = (TextView) findViewById(R.id.tv_latitud);
        tvlongitud = (TextView) findViewById(R.id.tv_longitud);
        tvdireccion = (TextView) findViewById(R.id.tv_direccion);

        usuario=getIntent().getExtras().getString("usuario");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnObtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocation == null) {
                    tvdireccion.setText("Aún no se logra obtener las coordenadas.");
                } else {
                    latitud=currentLocation.getLatitude();
                    longitud=currentLocation.getLongitude();

                    tvlatitud.setText("Lat: "+latitud);
                    tvlongitud.setText("Long :"+longitud);

                    direccion=setUbicacion(latitud,longitud);
                    tvdireccion.setText("Mi dirección es: " + direccion);

                    direccion=direccion.replace(" ","");
                }
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread tr=new Thread(){
                    @Override
                    public void run() {

                        final String resultado=registrarLugar();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(resultado.length()>0){
                                    Toast.makeText(getApplicationContext(),"UBICACION REGISTRADA CON EXITO",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"ERROR AL ENVIAR DATOS",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread tr=new Thread(){
                    @Override
                    public void run() {

                        final String resultado=obtenerLugares(usuario);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent nuevoForm=new Intent(MainActivity.this,LugaresActivity.class);
                                nuevoForm.putExtra("resultado", resultado);
                                startActivity(nuevoForm);
                            }
                        });
                    }
                };
                tr.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_LOCATION_UPDATE,
                    MIN_DISTANCE_LOCATION_UPDATE,
                    this
            );
            Toast.makeText(this, "GPS activado!", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "No se han otorgado los permisos para localización", Toast.LENGTH_SHORT).show();
        }
    }

    public String setUbicacion(double latitud, double longitud) {
        //Obtener la dirección de la calle a partir de la latitud y la longitud

        if (latitud != 0.0 && longitud != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    direccion=address.getAddressLine(0).toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return direccion;
    }

    @Override
    protected void onPause() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Toast.makeText(this, "No se han otorgrado los permisos para localización", Toast.LENGTH_SHORT).show();
        }
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Do nothing
    }

    @Override
    public void onProviderEnabled(String s) {
        // Do nothing
    }

    @Override
    public void onProviderDisabled(String s) {
        // Do nothing
    }

    public String obtenerLugares(String user){
        URL url=null;
        String linea="";
        int resp=0;
        StringBuilder result=null;

        try{
            url=new URL("http://192.168.43.124:84/DemoGeolocalizacion/consultarLugares.php?usuario="+user);
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

    public String registrarLugar(){
        URL url=null;
        String linea="";
        int resp=0;
        StringBuilder result=null;

        try{
            url=new URL("http://192.168.43.124:84/DemoGeolocalizacion/registrarLugar.php?latitude=" + latitud +
                    "&longitude=" + longitud + "&address=" + direccion+"&usuario="+usuario);
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
}
