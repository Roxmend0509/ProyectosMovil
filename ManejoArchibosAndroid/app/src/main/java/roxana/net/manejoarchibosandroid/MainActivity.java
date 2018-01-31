package roxana.net.manejoarchibosandroid;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText txt ;
    RadioGroup optg ;
    RadioButton optSelecccionado;

    private  final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();






        txt = (EditText)findViewById(R.id.txt);
        optg = (RadioGroup)findViewById(R.id.optgAlmacenamiento);
        Log.d("CICLOVIDA", "onCreate");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){


                }
        }


    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){



            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){



            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new  String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE );
            }
        }
    }



    public void btnGuardar_click(View v) {
        if(	optg.getCheckedRadioButtonId()
                == R.id.optExterna){
            saveExternal();
        }else{
            saveInternal();
        }
    }

    public void btnAbrir_click(View v) {
        if(optg.getCheckedRadioButtonId()
                == R.id.optExterna ){
            openExternal();
        }else
        {openInternal();}
    }

    public void saveExternal() {



        File dirAppPath =
                getExternalFilesDir(null);

        Toast.makeText(getBaseContext(),
                Environment.getExternalStorageState(),
                Toast.LENGTH_LONG).show();


//        File dirAppPath =
//                Environment.getExternalStoragePublicDirectory
//                        (Environment.DIRECTORY_MUSIC);



        Toast.makeText(getBaseContext(),
                dirAppPath.getAbsolutePath(),
                Toast.LENGTH_LONG).show();



        File miArchivo =
                new File(dirAppPath, "notita.txt");

        try {

            FileOutputStream fos =
                    new FileOutputStream(miArchivo, true);

            OutputStreamWriter osw = new
                    OutputStreamWriter(fos);

            osw.write(txt.getText().toString());

            osw.flush();
            osw.close();

            Toast.makeText(getBaseContext(),
                    "GUARDADO EXTERNA",
                    Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public void saveInternal() {
        File dirAppPath =
                getFilesDir();

        Toast.makeText(getBaseContext(),
                dirAppPath.getAbsolutePath(),
                Toast.LENGTH_LONG).show();



        File miArchivo =
                new File(dirAppPath, "notita.txt");

        try {

            FileOutputStream fos =
                    new FileOutputStream(miArchivo);

            OutputStreamWriter osw = new
                    OutputStreamWriter(fos);

            osw.write(txt.getText().toString());

            osw.flush();
            osw.close();

            Toast.makeText(getBaseContext(),
                    "GUARDADO INTERNA",
                    Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void openInternal() {


        File rutaAppFile =  getFilesDir();

        File miArchivo = new File(rutaAppFile, "notita.txt");

        try {
            FileInputStream fis = new FileInputStream(miArchivo);

            InputStreamReader isr =
                    new InputStreamReader(fis);

            char[] bloque = new char[100];
            String texto ="";
            while(isr.read(bloque)!=-1){

                texto += String.valueOf(bloque);
                bloque = new char[100];
            }

            txt.setText(texto);

            isr.close();
            fis.close();


            Toast.makeText(getBaseContext(),
                    "LEIDO Interna",
                    Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void openExternal() {

        File rutaAppFile =  getExternalFilesDir(null);

        File miArchivo = new File(rutaAppFile, "notita.txt");

        try {
            FileInputStream fis = new FileInputStream(miArchivo);

            InputStreamReader isr =
                    new InputStreamReader(fis);

            char[] bloque = new char[100];
            String texto ="";
            while(isr.read(bloque)!=-1){

                texto += String.valueOf(bloque);
                bloque = new char[100];
            }

            txt.setText(texto);

            isr.close();
            fis.close();


            Toast.makeText(getBaseContext(),
                    "LEIDO EXTERNA",
                    Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
}

