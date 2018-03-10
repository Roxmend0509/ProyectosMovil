package com.hlc.serviciosenandroidejerciciosclase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//Clase que va a recibir el broadcast
public class OutgoinCallBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Toast.makeText(context, phoneNumber, Toast.LENGTH_LONG).show();
    }
}
