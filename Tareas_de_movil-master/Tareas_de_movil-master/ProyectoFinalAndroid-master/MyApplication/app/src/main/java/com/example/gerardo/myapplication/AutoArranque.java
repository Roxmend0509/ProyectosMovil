package com.example.gerardo.myapplication;

/**
 * Created by Gerardo on 05/02/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoArranque extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context,  Servicio.class);
        context.startService(service);

       /*Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/

        /*Intent serviceIntent = new Intent();
        serviceIntent.setAction("Servicio");
        context.startService(serviceIntent);*/
    }

}