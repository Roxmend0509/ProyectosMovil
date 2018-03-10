package com.example.gerardo.agendacontactos;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


import java.util.Random;

/**
 * Created by Gerardo on 07/02/2018.
 */

public class ServicioEnlazado extends Service {

    private final IBinder mBinder = new LocalBinder();
    private final Random mGenerator = new Random();


    public class LocalBinder extends Binder {
        ServicioEnlazado getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServicioEnlazado.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }






}
