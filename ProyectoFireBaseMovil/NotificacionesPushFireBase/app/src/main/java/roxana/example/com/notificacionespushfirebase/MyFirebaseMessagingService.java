package roxana.example.com.notificacionespushfirebase;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG="Mensajes";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from=remoteMessage.getFrom();
        Log.d(TAG,"Mensaje recibido de: "+from);

       if (remoteMessage.getNotification()!=null){

           Log.d(TAG,"Notificación: "+remoteMessage.getNotification().getBody());

           mostrarNotificacion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
       }

       if (remoteMessage.getData().size()>0){

           Log.d(TAG,"Data: "+remoteMessage.getData());

       }
    }

    public void mostrarNotificacion(String titulo,String body){

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intent=new Intent(this, mensajes.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_favorite_true)
                .setContentTitle(titulo)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                    .setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}