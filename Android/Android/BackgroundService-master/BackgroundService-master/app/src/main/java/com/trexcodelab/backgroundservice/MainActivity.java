package com.trexcodelab.backgroundservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.trexcodelab.backgroundservice.service.MyService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Variables para los objetos de la vista
    private Button btnIniciar;
    private Button btnDetener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener los servicios de la vista
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnDetener = (Button) findViewById(R.id.btnDetener);

        // Le asignamos el evento al boton
        btnIniciar.setOnClickListener(this);
        btnDetener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnIniciar){
            // Iniciamos el servicio
            startService(new Intent(this, MyService.class));
        } else if(view == btnDetener){
            // Detenemos el servicio
            stopService(new Intent(this, MyService.class));
        }
    }
}
