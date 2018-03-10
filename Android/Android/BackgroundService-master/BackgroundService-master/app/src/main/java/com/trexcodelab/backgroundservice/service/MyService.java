package com.trexcodelab.backgroundservice.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by t-rex on 09/09/17.
 */

public class MyService extends Service {

    // Creamos un objeto mediaplayer
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // Obtenemos los ringtones por default del sistema
        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        // Configuramos un ciclo play a true
        // Esto hara que el ringtone se reproduzca continuamente
        player.setLooping(true);
        // Iniciamos la reproduccion
        player.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Detenemos la reproduccion cuando el servicio se termine
        player.stop();
    }
}
