package com.example.roxana.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;


public class menuActivity extends AppCompatActivity {
    Intent parametros;
    Button es,cancelar;
    private Locale locale;
    private Configuration config = new Configuration();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menus);
        parametros = getIntent();
        es = (Button) findViewById(R.id.btnOp);
        cancelar=(Button)findViewById(R.id.btnCancelar);

        es.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        showDialog();
                    }});


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
        private void showDialog(){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle(getResources().getString(R.string.change));
            //obtiene los idiomas del array de string.xml
            String[] types = getResources().getStringArray(R.array.languages);
            b.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    switch(which){
                        case 0:
                            locale = new Locale("en");
                            config.locale =locale;
                            break;
                        case 1:
                            locale = new Locale("es");
                            config.locale =locale;
                            break;
                    }
                    getResources().updateConfiguration(config, null);
                    Intent refresh = new Intent(menuActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                }

            });

            b.show();
        }
    }
