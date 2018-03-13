package com.example.roxana.volleylorem;

import android.app.VoiceInteractor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    Spinner spinner,spinner2;
    SeekBar ancho, alto,id;
    TextView lblAlto, lblAncho,lblId;
    ImageView imagen;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alto = findViewById(R.id.seekAlto);
        id = findViewById(R.id.seekId);
        lblId=findViewById(R.id.lblid);
        lblAlto = findViewById(R.id.lblalto);
        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinnerGravity);
        imagen = findViewById(R.id.imagen);
        alto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblAlto.setText("Alto " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ancho = findViewById(R.id.seekAncho);
        lblAncho = findViewById(R.id.lblancho);
        ancho.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblAncho.setText("Ancho " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        id = findViewById(R.id.seekId);
        lblId = findViewById(R.id.lblid);
        id.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblId.setText("Id " + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
             }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[]{
                "image",
                "gravity",
                "blur"
        });
        spinner.setAdapter(adapter);

        spinner2 = findViewById(R.id.spinnerGravity);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[]{
                "north",
                "east",
                "south",
                "west",
                "center"
        });
        spinner2.setAdapter(adapter2);



    }

    public void clic(View v){
        String url = "https://picsum.photos/";

            url += "g/";
        url += ancho.getProgress() + "/";
        url += alto.getProgress() + "/";
        if (spinner.getSelectedItem()=="image") {
            url += "?" + spinner.getSelectedItem() + "=" + id.getProgress();
        }else if (spinner.getSelectedItem()=="gravity"){

            url += "?" + spinner.getSelectedItem() + "=" + spinner2.getSelectedItem();
        }else if (spinner.getSelectedItem()=="blur"){
            url += "?" + spinner.getSelectedItem();

        }
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest peticion = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imagen.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, // maxWidth, maxHeight, decodeConfig
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No funciona", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(peticion);

    }

}
