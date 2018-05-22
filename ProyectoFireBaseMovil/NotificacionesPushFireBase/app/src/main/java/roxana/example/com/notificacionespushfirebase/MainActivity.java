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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView infoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmensajes);

        infoTextView=(TextView) findViewById(R.id.infoText);

       if (getIntent().getExtras()!=null){
           for (String key:getIntent().getExtras().keySet()){

               String value =getIntent().getExtras().getString(key);
               infoTextView.append("\n"+key+": "+value);

           }
       }
    }



}