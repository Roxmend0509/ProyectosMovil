package roxana.example.com.notificacionespushfirebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button regi, ingresar;
    EditText usuario, password;
    public static boolean isAppRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regi = (Button) findViewById(R.id.btnRegistrar);
        usuario = (EditText) findViewById(R.id.textUsuario);
        password = (EditText) findViewById(R.id.textPass);
        ingresar = (Button) findViewById(R.id.btnIngresar);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1",NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2",NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);

        }

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(getApplicationContext(), NuevoUsuario.class);
                startActivity(registro);

            }
        });


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                Toast toast1=Toast.makeText(getApplicationContext(),"Ingresa los datos",Toast.LENGTH_SHORT);
                toast1.show();
                } else {
                    if (usuario.getText().toString() == "123" && password.getText().toString() == "123") {

                        Intent ingresar = new Intent(getApplicationContext(), mensajes.class);
                        startActivity(ingresar);

                    }else{
                        Toast toast1=Toast.makeText(getApplicationContext(),"Ingresa los datos correctos",Toast.LENGTH_SHORT);
                        toast1.show();

                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }



}