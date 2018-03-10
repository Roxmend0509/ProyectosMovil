package com.example.gerardo.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.example.gerardo.myapplication.Model.DaoNotas;
import com.example.gerardo.myapplication.Model.DaoRecordatorios;
import com.example.gerardo.myapplication.Pojos.Notas;
import com.example.gerardo.myapplication.Pojos.Recordatorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Gerardo on 28/10/2017.
 */

public class Servicio extends Service {

    @Override
    public void onCreate(){

    }

    hilo h;
    @Override
    public int onStartCommand(Intent intent,int flag,int idProcess){

        try{
        if(h==null) {
            h = new hilo();
            h.start();
        }
        }catch (Exception err){

        }

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        try{
        if(h.isAlive()) {
            h.stop();
        }
        }catch (Exception err){

        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArrayList<Recordatorio> listarecordatorios = new ArrayList<>() ;
    int x=0;
    public void btnNoti_click(String Titulo,String Descripcion,int indice) {

        x=x+1;
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        int icono = R.mipmap.calendar;

        long hora = System.currentTimeMillis();

        Intent i = new Intent(this, MainActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle(Titulo)
                .setContentText(Descripcion)
                .setWhen(hora)
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true)
                .setSound(defaultSound);

        mNotifyMgr.notify(x, mBuilder.build());

    }


    public class hilo extends Thread{
        @Override
        public  void  run(){
            while (true){

                try {

                    DaoRecordatorios dao = new DaoRecordatorios(getApplicationContext());
                    DaoNotas daonotas = new DaoNotas(getApplicationContext());


                    final Calendar c= Calendar.getInstance();
                    String fecha = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
                    String hora = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);


                    List<Recordatorio> lista ;
                    lista = dao.notificacionescumplidas(fecha);


                    Recordatorio recordatorio = new Recordatorio();
                    recordatorio.setId(1);
                    recordatorio.setFecha("Fecha");
                    recordatorio.setHora("Hora");
                    recordatorio.setNota(1);
                    lista.add(recordatorio);


                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getHora().equalsIgnoreCase(hora) && ((c.get(Calendar.SECOND)) == 0)) {


                            Recordatorio recordatorio1 = new Recordatorio();
                            recordatorio1.setId(lista.get(i).getId());
                            recordatorio1.setFecha(lista.get(i).getFecha());
                            recordatorio1.setHora(lista.get(i).getHora());

                             listarecordatorios.add(recordatorio1);

                             Notas nota = new Notas();
                             nota = daonotas.obtenerNotaoTarea(lista.get(i).getNota()+"");

                            if(nota.getTitulo().trim().length()>0 && nota.getDescripcion().trim().length()>0) {
                                btnNoti_click(nota.getTitulo(), nota.getDescripcion(), i);
                            }

                             }

                         }

                    Thread.sleep(1000);




                } catch (Exception e) {

                }
            }
        }
    }


}
