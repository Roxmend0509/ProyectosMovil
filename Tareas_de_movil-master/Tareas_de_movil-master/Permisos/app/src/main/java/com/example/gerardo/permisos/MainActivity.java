package com.example.gerardo.permisos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {


    private static final int SOLICITUD_PERMISO_CALL_PHONE = 1;
    private Intent intentLLamada;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentLLamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4451228560"));
        Button btnLlamada = (Button) findViewById(R.id.btnpermiso1);




        btnLlamada.setOnClickListener(new View.OnClickListener() { // hago clic en el botón
            @Override
            public void onClick(View v) {
                //solicitarPermisoHacerLlamada();
                pedirHacerllamada();

            }
        });




    }






    public void pedirHacerllamada() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            startActivity(intentLLamada);

        } else {
            explicarUsoPermiso();
            //solicitarPermisoHacerLlamada();

        }

    }



    private void explicarUsoPermiso() {

        //Para ver si se marco no volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            alertDialogBasico();
            solicitarPermisoHacerLlamada();
        }

    }

    private void solicitarPermisoHacerLlamada() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                SOLICITUD_PERMISO_CALL_PHONE);


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SOLICITUD_PERMISO_CALL_PHONE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity(intentLLamada);

            }
        }
    }



    public void alertDialogBasico() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("DEBES CONCEDER EL PERMISO");


        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        builder.show();

    }


}
