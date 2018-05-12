package com.example.roxana.mapasregresoacasa;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class UbicacionActual {

    private double latitude=0d;//valor de x
    private double longitude=0d;//valor y


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void coordenadas(Context contexto) {
        LocationManager locationManager = (LocationManager)
                contexto.getSystemService(contexto.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    public LatLng estoyen(){
        LatLng cordenadas = new LatLng(latitude,longitude);
        return cordenadas;
    }

    public UbicacionActual(Context contexto){
        coordenadas(contexto);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }




}
