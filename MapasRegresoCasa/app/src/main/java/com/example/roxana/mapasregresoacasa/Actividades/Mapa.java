package com.example.roxana.mapasregresoacasa.Actividades;

import android.Manifest;
import android.content.pm.PackageManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roxana.mapasregresoacasa.BuscarDireccion;
import com.example.roxana.mapasregresoacasa.DirectionFinderListener;
import com.example.roxana.mapasregresoacasa.R;
import com.example.roxana.mapasregresoacasa.Route;
import com.example.roxana.mapasregresoacasa.UbicacionActual;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private Button btntrazarruta;
    private TextView txtorigen;
    private TextView txtdestino;

    private int rutatrazada=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btntrazarruta = (Button) findViewById(R.id.btnruta);
        txtorigen = findViewById(R.id.txtorigen);
        txtdestino = findViewById(R.id.txtdestino);

        btntrazarruta.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               trazarruta();
           }
       });

        try {
            txtorigen.setText(getIntent().getExtras().getString("Origen"));
            txtdestino.setText(getIntent().getExtras().getString("Destino"));

            if (txtorigen.getText().toString().trim().length() > 0 || txtdestino.getText().toString().trim().length() > 0) {
                // txtorigen.setVisibility(View.INVISIBLE);
                //txtdestino.setVisibility(View.INVISIBLE);
                //btntrazarruta.setVisibility(View.INVISIBLE);
                trazarruta();
            }
        }catch (Exception err){}


    }

    private void trazarruta() {
        rutatrazada=0;
        String origin = "20.119209, -101.195887";
        String destination = txtdestino.getText().toString().trim();


            UbicacionActual ubicacion = new UbicacionActual(getBaseContext());
            if(txtorigen.getText().toString().trim().equalsIgnoreCase("Esta es tu Ubicación Actual")){
                origin = ubicacion.getLatitude()+","+ubicacion.getLongitude();
            }else{
                origin = txtorigen.getText().toString().trim();
            }

        if(txtdestino.getText().toString().trim().equalsIgnoreCase("Esta es tu Ubicación Actual")){
            destination = ubicacion.getLatitude()+","+ubicacion.getLongitude();
        }else{
            destination = txtdestino.getText().toString().trim();
        }


        if (origin.isEmpty()) {
            Toast.makeText(this, "Ingresa la Direccion de Origen", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Ingresa la direccion de Destino", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new BuscarDireccion(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
                rutatrazada=0;
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            UbicacionActual ubicar = new UbicacionActual(getBaseContext());
            // Add a marker in Sydney and move the camera

           LatLng ubicacionActual = new LatLng(ubicar.getLatitude(),ubicar.getLongitude());// crearmos las coordenadas de la ubicacion actual
           // agregarmarcador(ubicar.estoyen(), "Punto de Inicio", "Lugar de Inicio del Recorrido");
           // agregarmarcador(ubicar.estoyen(), "Estoy en", "Lugar donde me Encuentro");// agregar marcador en la ubicacion actual
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicar.estoyen(), 19));// hacemos un zoom al mapa en la ubicacion actual

           /* originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title("Mi Ubicacion")
                    .position(ubicacionActual)));*/

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);//activamos la ubicacion actual
            mMap.getUiSettings().setMyLocationButtonEnabled(false);//deshabilita el boton de ubicacion sobre el mapa

        } catch (Exception err) {
            Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Espere...Analizando",
                "Analizando Ruta", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.ubicacioninicial, 16));
            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
           // ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.direccioninicial)
                    .position(route.ubicacioninicial)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                   // .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.direccionfinal)
                    .position(route.ubicacionfin)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.rgb(0,0,200)).
                    width(10);

            for (int i = 0; i < route.puntos.size(); i++)
                polylineOptions.add(route.puntos.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
        rutatrazada=1;
    }







 }
