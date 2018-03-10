package net.ivanvega.misserviciosandroida;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ServicioEnlazado mService;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, ServicioEnlazado.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnI_click(View v){
        Toast.makeText(this,"INTERACTUANDO HILO PRINCIPAL",
                Toast.LENGTH_SHORT).show();
    }

    public void btnIS_click(View v){
        Toast.makeText(this,"arrancando intent servicio",
                Toast.LENGTH_SHORT).show();

        Intent is = new Intent(this,MiIntentService.class);

        startService(is);
    }

    public void btnS_click(View v){
        Toast.makeText(this,"arrancando intent servicio",
                Toast.LENGTH_SHORT).show();

        Intent is = new Intent(this,HelloService.class);

        startService(is);
    }

    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServicioEnlazado.LocalBinder binder = (ServicioEnlazado.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



}