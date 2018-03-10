package net.ivanvega.notificacionesydialogos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String colores[] =
            new String[]
                    {"verde", "blanco", "rojo"};

    String generos_musicales[] =
            new String[]
                    {"rock", "clasica", "jazz", "pop", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnInfo_click(View v){

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Cuadro de dialogo")
                        .setIcon(android.R.drawable.ic_btn_speak_now)
                        .setMessage("Hola Mubndo desde Android")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(MainActivity.this,
                                        "Presiono OK", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,
                                        "Presiono OK", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .create();

        dialog.show();


    }

    public void  btnList_click(View v){
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Cuadro de dialogo")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(colores, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,
                                        colores[which], Toast.LENGTH_SHORT)
                                        .show();
                                dialog.dismiss();
                            }
                        })
                        .create();

        dialog.show();
    }


    public void  btnCheckList_click(View v){
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Cuadro de dialogo")
                        .setIcon(R.mipmap.ic_launcher)
                        
                        .setMultiChoiceItems(generos_musicales,
                                new boolean[]{true, false, true, false}
                                , new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        Toast.makeText(MainActivity.this,
                                                generos_musicales[which] + (isChecked? " Verificado": "No Verificado"),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .create();

        dialog.show();
    }



    public void btnNoti_click(View v){

        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        int icono = R.mipmap.ic_launcher;

        long hora = System.currentTimeMillis();

        Intent i=new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);

        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("Notificacion")
                .setContentText("Esto es un ejemplo de notificacion")
                .setWhen(hora)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true)
                .setSound(defaultSound);

        mNotifyMgr.notify(1, mBuilder.build());

    }



}
