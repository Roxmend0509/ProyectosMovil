package com.losdespatarraos.serviciosdelsistema;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sm;
    private float acel1;
    private float acel2;
    private float shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acel1=SensorManager.GRAVITY_EARTH;
        acel2=SensorManager.GRAVITY_EARTH;
        shake=0.00f;
    }
private final SensorEventListener sensorListener=new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        acel2=acel1;
        acel1=(float)Math.sqrt((double)(x*x+y*y+z*z));
        float delta=acel1-acel2;
        shake=shake*0.9f+delta;

        if(shake>12){
            Toast.makeText(MainActivity.this, "no me agites >_< ", Toast.LENGTH_SHORT).show();
                    
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
};
}

