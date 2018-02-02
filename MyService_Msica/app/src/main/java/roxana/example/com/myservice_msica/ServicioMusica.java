package roxana.example.com.myservice_msica;

/**
 * Created by Rox on 01/02/2018.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


public class ServicioMusica extends Service implements MediaPlayer.OnPreparedListener {
    MediaPlayer reproductorM;
    String msg = "¡PRECAUCIÓN! Si se mantiene abierto, puede consumir mucha batería";
    int FORE_ID = 1335;
    @Override
    public void onCreate() {
        Toast.makeText(this,"Servicio creado",
                Toast.LENGTH_SHORT).show();
        reproductorM = MediaPlayer.create(this,R.raw.audio);
    }

    // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        // public int onStartCommand(Intent intent, int flags, int startId) {
        Intent noty_intent = new Intent(this,
                MainActivity.class);
        noty_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, noty_intent,
                0);
        Notification n = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                n = new Notification.Builder(this)
                        .setContentTitle("HEYEAYEAHEYEAYEA...")
                        .setContentText(msg).setSmallIcon(R.mipmap.ic_launcher)
                        .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark))
                        .setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(),
                                R.mipmap.ic_launcher))
                        .setContentIntent(pIntent).setAutoCancel(true).setOngoing(true)
                        .build();
            }
        }
        startForeground(FORE_ID, n);
        reproductorM.start();
        Toast.makeText(this,"Servicio iniciado "+ idArranque,
                Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Servicio detenido",
                Toast.LENGTH_SHORT).show();
        reproductorM.stop();
    }

    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }
    public void onPrepared(MediaPlayer reproductor) {
        reproductor.start();
    }
}