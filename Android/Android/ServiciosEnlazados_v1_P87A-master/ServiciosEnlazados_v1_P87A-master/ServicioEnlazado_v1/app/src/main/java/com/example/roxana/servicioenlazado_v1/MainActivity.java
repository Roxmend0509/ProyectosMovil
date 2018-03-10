package com.example.roxana.servicioenlazado_v1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.roxana.servicioenlazado_v1.BoundService.MyBinder;

public class MainActivity extends AppCompatActivity {

    BoundService mBoundService;
    boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timestampText = (TextView) findViewById(R.id.timestamp_text);
        Button printTimestampButton = (Button) findViewById(R.id.print_timestamp);
        Button stopServiceButon = (Button) findViewById(R.id.stop_service);

        printTimestampButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    timestampText.setText(mBoundService.getTimestamp());
                }
            }

        });

        stopServiceButon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                Intent intent = new Intent(MainActivity.this, BoundService.class);
                stopService(intent);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mServiceBound = false;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MyBinder myBinder = (MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;

        }

    };






}
