package com.example.roxana.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.roxana.myapplication.Model.DaoMultimedia;
import com.example.roxana.myapplication.Model.DaoRecordatorios;
import com.example.roxana.myapplication.Pojos.Multimedia;
import com.example.roxana.myapplication.Pojos.Notas;
import com.example.roxana.myapplication.Pojos.Recordatorio;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.text.TextUtils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class actividadmultimedia extends AppCompatActivity {

    private ViewStub stubGrid;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private List<Multimedia> listamultimedia;



    private Notas nota;

    private String tipoarchivo = "0";
    private String ejecutoaccion = "";
    private static final int SOLICITUD_FOTO = 2;
    private final String CARPETA_RAIZ = "multimedia/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "fotos";
    private final String RUTA_VIDEO = CARPETA_RAIZ + "videos";
    final int COD_FOTO = 20;
    final int COD_VIDEO = 30;
    String path="",path2="",path3="",path4="";
    private boolean bandera=false;


    //audio

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    //audio


    private Multimedia archivo;


    String operaciones[] =
            new String[]
                    {"Eliminar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividadmultimedia);



        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        stubGrid.inflate();

        gridView = (GridView) findViewById(R.id.mygridview);

        getProductList();



        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                bandera=true;
                archivo = new Multimedia(gridViewAdapter.getItem(i).getArchivo(),gridViewAdapter.getItem(i).getDescripcion(),gridViewAdapter.getItem(i).getNota());
                archivo.setId((gridViewAdapter.getItem(i).getId()));
                btnList_click();

                return false;
            }
        });




        try {
            Bundle bundle = getIntent().getExtras();
            nota = new Notas();

            //Toast.makeText(getBaseContext(),bundle.getInt("id")+"",Toast.LENGTH_SHORT).show();
            nota.setId(bundle.getInt("id"));
            nota.setTitulo(bundle.getString("titulo"));

            setAdapters(nota.getId()+"");
        }catch (Exception err){}


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (bandera == false) {


                        String ruta = gridViewAdapter.getItem(i).getArchivo();
                        String archivo = "";


                        if (ruta.endsWith(".png") ||
                                ruta.endsWith(".jpeg") ||
                                ruta.endsWith(".jpg")) {
                            abrirarchivo("file://"+ruta,"image/*");
                        } else if (ruta.endsWith(".mp4") ||
                                ruta.endsWith(".avi")||
                                ruta.endsWith(".3gp") ||
                                ruta.endsWith(".flv")) {
                            abrirarchivo("file://"+ruta,"video/*");
                        } else if (ruta.endsWith(".aac")||
                                ruta.endsWith(".mp3")||
                                ruta.endsWith(".wav")||
                                ruta.endsWith(".m4a")) {
                            abrirarchivo("file://"+ruta,"audio/*");

//                            mediaPlayer = new MediaPlayer();
//                            try {
//                                mediaPlayer.setDataSource(ruta);
//                                mediaPlayer.prepare();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            mediaPlayer.start();
//
//                            Toast.makeText(actividadmultimedia.this, "Grabación En Reproducción",
//                                    Toast.LENGTH_LONG).show();

                        }


                    }
                    bandera=false;
                    }catch(Exception err){
                        Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
                    }

            }
        });



        operaciones[0] = getString(R.string.eliminar);

        botonflotante();
    }

    public void abrirarchivo(String ruta,String archivo){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(ruta), archivo);
        startActivity(intent);
    }

    public void  btnList_click(){
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.operacionarealizar))
                        .setIcon(R.mipmap.opc)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                   confirmacion();
                                }



                                dialog.dismiss();
                            }
                        })
                        .create();

        dialog.show();
    }

    public void confirmacion(){

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.deceaeliminar))
                        .setIcon(R.mipmap.bot)
                        .setMessage(getString(R.string.advertenciaeliminar))
                        .setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                eliminar(archivo.getId()+"");

                            }
                        })
                        .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();

        dialog.show();


    }

    private void botonflotante(){
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabmultimedia);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogomultimedia();
                //Toast.makeText(getBaseContext(),"FloatButton1",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void insertar(Multimedia multimedia){
        DaoMultimedia dao = new DaoMultimedia(getBaseContext());
        if(dao.Insert(multimedia)>1){
            Toast.makeText(getBaseContext(),getString(R.string.archivoinsertado)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(),getString(R.string.archivonoinsertado)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void eliminar(String id){
        DaoMultimedia dao = new DaoMultimedia(getBaseContext());
        if(dao.delete(id)>1){
            Toast.makeText(getBaseContext(),getString(R.string.archivonoeliminado)+"",Toast.LENGTH_LONG).show();
            eliminararchivo();
        }else{
            setAdapters(nota.getId()+"");
            Toast.makeText(getBaseContext(),getString(R.string.archivoeliminado)+"",Toast.LENGTH_LONG).show();

        }
    }

    public void eliminararchivo(){
        try{
            File arc = new File(archivo.getArchivo());
            if(arc.exists()){
                arc.delete();
            }
        }catch (Exception err){

        }
    }

    private void setAdapters(String id) {
        DaoMultimedia dao = new DaoMultimedia(getBaseContext());
        listamultimedia = new ArrayList<>();
        listamultimedia = dao.getmultimedia(id);

        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, dao.getmultimedia(id));
        gridView.setAdapter(gridViewAdapter);
    }

    public List<Multimedia> getProductList() {
        //pseudo code to get product, replace your code to get real product here
        listamultimedia = new ArrayList<>();
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 1", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.video, "Titulo 2", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.imagen1, "Titulo 3", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.video, "Titulo 4", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.imagen1, "Titulo 5", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.video, "Titulo 6", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.imagen1, "Titulo 7", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 8", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 9", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.imagen1, "Titulo 10", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 11", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.video, "Titulo 12", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.imagen1, "Titulo 13", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 14", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 15", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.imagen1, "Titulo 16", "Descripcion del Archivo",1));
        listamultimedia.add(new Multimedia(R.mipmap.audio, "Titulo 17", "Descripcion del Archivo",1));
        return listamultimedia;
    }




    int VALOR_RETORNO=1;

    public void dialogomultimedia(){

        final Dialog dialog = new Dialog(actividadmultimedia.this);
        dialog.setContentView(R.layout.activity_multimedia);


        //botones audio
        final Button btnrecord = (Button) dialog.findViewById(R.id.btnRecord);
        final Button btnstoprecord = (Button) dialog.findViewById(R.id.btnStopRecord);
        final Button ultimo = (Button) dialog.findViewById(R.id.btnPlayLast);


        random=new Random();
        final EditText txtdescripcion = (EditText)dialog.findViewById(R.id.txtdescripcion) ;

        final Button btnoperacion = (Button) dialog.findViewById(R.id.btnoperacion);
        final ImageButton btnimgfoto = (ImageButton) dialog.findViewById(R.id.btntomarfoto);
        final ImageButton btnvideo = (ImageButton) dialog.findViewById(R.id.btngrabarvideo);

        ImageButton btnaudio = (ImageButton) dialog.findViewById(R.id.btnaudio);
        // if button is clicked, close the custom dialog




            dialog.setTitle(getString(R.string.addmultimedia)+"");


        btnimgfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tipoarchivo="1";
                //tomarFotografia();
                pedirHacerFoto();


            }
        });

        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tipoarchivo="2";
                pedirHacerVideo();
                //grabarVideo();


            }
        });

        btnaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tipoarchivo="3";


                btnvideo.setVisibility(View.INVISIBLE);
                btnimgfoto.setVisibility(View.INVISIBLE);

                btnrecord.setVisibility(View.VISIBLE);
                btnstoprecord.setVisibility(View.VISIBLE);
                ultimo.setVisibility(View.VISIBLE);

                btnstoprecord.setEnabled(false);
                ultimo.setEnabled(false);


            }
        });




        ultimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(path3);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(actividadmultimedia.this, "Grabación En Reproducción",
                        Toast.LENGTH_LONG).show();


            }
        });

        btnstoprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mediaRecorder.stop();
                    btnstoprecord.setEnabled(false);
                    btnrecord.setEnabled(true);
                ultimo.setEnabled(true);
                ejecutoaccion="si";


                    Toast.makeText(actividadmultimedia.this, "Grabación completada",
                            Toast.LENGTH_LONG).show();


            }
        });

        btnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()){
                AudioSavePathInDevice =
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                CreateRandomAudioFileName(5) + "AudioRecording.aac";

                path3=AudioSavePathInDevice.toString();


                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                btnrecord.setEnabled(false);
                btnstoprecord.setEnabled(true);
                    ultimo.setEnabled(false);

//                Toast.makeText(actividadmultimedia.this, "Recording started",
//                        Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }




            }
        });












        btnoperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (txtdescripcion.getText().length() > 0 && (ejecutoaccion.length()>0)) {


                        Multimedia archivo = null;
                        if (tipoarchivo.equalsIgnoreCase("1")) {
                            archivo = new Multimedia(path, txtdescripcion.getText() + "", nota.getId());

                        } else if (tipoarchivo.equalsIgnoreCase("2")) {
                            archivo = new Multimedia(path2, txtdescripcion.getText() + "", nota.getId());

                        } else if (tipoarchivo.equalsIgnoreCase("3")) {
                            archivo = new Multimedia(path3, txtdescripcion.getText() + "", nota.getId());
                        }else if (tipoarchivo.equalsIgnoreCase("4")) {
                            if(path4.length()>0) {
                                archivo = new Multimedia(path4, txtdescripcion.getText() + "", nota.getId());
                            }
                            }

                        if(archivo!=null) {
                            insertar(archivo);
                        }

                        

                        setAdapters(nota.getId() + "");

                        dialog.dismiss();
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.archivoadjuntar), Toast.LENGTH_LONG).show();
                    }

                }catch (Exception err){
                    //Toast.makeText(getBaseContext(),err.getMessage()+"jaja", Toast.LENGTH_LONG).show();
                }finally {
                    reiniciar();
                }


            }
        });


        Button btnsalir = (Button) dialog.findViewById(R.id.btnsalir);
        // if button is clicked, close the custom dialog
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File archivo = null;
                    if (tipoarchivo.equalsIgnoreCase("1")) {
                        if (path.length() > 0) {
                            archivo = new File(path);
                            if(archivo.exists()) {
                                archivo.delete();
                            }
                        }
                    } else if (tipoarchivo.equalsIgnoreCase("2")) {
                        if (path2.length() > 0) {
                            archivo = new File(path2);
                            if(archivo.exists()) {
                                archivo.delete();
                            }
                        }
                    }else if (tipoarchivo.equalsIgnoreCase("3")) {
                        if (path3.length() > 0) {
                            archivo = new File(path3);
                            if(archivo.exists()) {
                                archivo.delete();
                            }
                        }
                    }
                }catch (Exception err){}

                reiniciar();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // audio
    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(actividadmultimedia.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }


public void reiniciar(){
    path ="";
    path2="";
    path3="";
    path4="";
    ejecutoaccion="";
}

    public void pedirHacerVideo() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            grabarVideo();


        } else {
            explicarUsoPermiso();
        }

    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    ////////////
    public void pedirHacerFoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            tomarFotografia();


        } else {
            explicarUsoPermiso();
        }

    }

    private void explicarUsoPermiso() {
        alertDialogBasico();
        solicitarPermisoCamara();

    }

    private void solicitarPermisoCamara() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                SOLICITUD_FOTO);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SOLICITUD_FOTO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                tomarFotografia();

            }

        }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_FOTO:

                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    break;
                case COD_VIDEO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                }
                            });



                default:
                    if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, path4, Toast.LENGTH_SHORT).show();
                    }
                    if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO )) {
                        //Procesar el resultado


                        Uri selectedImageURI = data.getData();
                        path4 = getRealPathFromURI(selectedImageURI);


                      //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();


                    }


                    break;
            }

        }


    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void grabarVideo() {
        File fileVid = new File(Environment.getExternalStorageDirectory(), RUTA_VIDEO);
        boolean isCreada2 = fileVid.exists();
        String nombrevid = "";
        if (!isCreada2) {
            isCreada2 = fileVid.mkdirs();
        }

        if (isCreada2) {
            nombrevid = (System.currentTimeMillis() / 1000) + ".mp4";
        }

        path2 = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_VIDEO + File.separator + nombrevid;


        File vid = new File(path2);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(vid));

        startActivityForResult(intent, COD_VIDEO);
        ejecutoaccion="si";



        ////
    }


    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (!isCreada) {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }

        path = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + nombreImagen;




        File imagen = new File(path);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getApplicationContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, COD_FOTO);

        ejecutoaccion="si";


        ////
    }

    public void alertDialogBasico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.permisonecesario));


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        builder.show();

    }


    ///////////




}//
