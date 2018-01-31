package com.example.roxana.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.roxana.myapplication.Model.DaoNotas;
import com.example.roxana.myapplication.Model.DaoRecordatorios;
import com.example.roxana.myapplication.Pojos.Notas;
import com.example.roxana.myapplication.Pojos.Recordatorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityRecordatorios extends AppCompatActivity {

    private ViewStub stubList;
    private ListView listView;
    private ListViewAdapter2 listViewAdapter;
    private List<Recordatorio> listarrecordatorios;
    private Recordatorio recordatorio;

    private int operacion = 0;
    private int indice = 0;
    private Notas nota;


    String operaciones[] =
            new String[]
                    {"Eliminar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios2);

        stubList = (ViewStub)findViewById(R.id.stub_list1);


        //Inflate ViewStub before get view
        stubList.inflate();


        listView = (ListView)findViewById(R.id.mylistview);



        //Register item lick

        //listView.setOnItemLongClickListener(onItemClick);
        clic();


        try {
            Bundle bundle = getIntent().getExtras();
            nota = new Notas();

            //Toast.makeText(getBaseContext(),bundle.getInt("id")+"",Toast.LENGTH_SHORT).show();
            nota.setId(bundle.getInt("id"));
            nota.setTitulo(bundle.getString("titulo"));

            setAdapters(nota.getId()+"");
        }catch (Exception err){}


        botonflotante();


        operaciones[0] = getString(R.string.eliminar);

    }

    public void clic(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indice = i;
                try {
                    btnList_click();
                }catch (Exception err){}
            }
        });
    }




    private void botonflotante(){
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                operacion=1;

                dialogorecordatorio();
                }catch (Exception err){Toast.makeText(getBaseContext(),err.getMessage()+"",Toast.LENGTH_SHORT).show();}
            }
        });

    }

    private void setAdapters(String id) {
        DaoRecordatorios daoRecordatorios = new DaoRecordatorios(getBaseContext());
        listarrecordatorios = new ArrayList<>();
        listarrecordatorios = daoRecordatorios.getrecordatoriosnota(id);

        listViewAdapter = new ListViewAdapter2(getBaseContext(), R.layout.list_item2, daoRecordatorios.getrecordatoriosnota(id));
        listView.setAdapter(listViewAdapter);

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
                                    try {
                                        recordatorio = new Recordatorio();
                                        recordatorio.setId(listViewAdapter.getItem(indice).getId());

                                        confirmacion();
                                    } catch(Exception err){
                                        Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
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

                                eliminar(recordatorio.getId()+"");
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

    public void eliminar(String id){
        DaoRecordatorios dao = new DaoRecordatorios(getBaseContext());
        if(dao.delete(id)>1){
            Toast.makeText(getBaseContext(),getString(R.string.recordatorionoeliminado)+"",Toast.LENGTH_LONG).show();
        }else{
            setAdapters(nota.getId()+"");
            Toast.makeText(getBaseContext(),getString(R.string.recordatorioeliminado)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void insertar(Recordatorio recordatorio){
        DaoRecordatorios dao = new DaoRecordatorios(getBaseContext());
        if(dao.Insert(recordatorio)>1){
            Toast.makeText(getBaseContext(),getString(R.string.recordatorioinsertado)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(),getString(R.string.recordatorionoinsertado)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void actualizar(Recordatorio recordatorio){
        DaoRecordatorios dao = new DaoRecordatorios(getBaseContext());
        if(dao.Update(recordatorio)>1){
            Toast.makeText(getBaseContext(),getString(R.string.recordatorionoactualizado)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(),getString(R.string.recordatorioactualizado)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void deshabilitareditext(EditText txt){

        txt.setFocusable(false);
        //txt.setFocusableInTouchMode(true);//habilitar edicion del editext

        //deshabilitar edicion del editext
        txt.setInputType(InputType.TYPE_NULL);
        txt.setInputType(InputType.TYPE_NULL);

    }

    public String validar(EditText txt,EditText txt1){
        String inconvenientes="";
        if(txt.getText().toString().length()==0){
            inconvenientes+="A";
        }
        if(txt1.getText().toString().length()==0){
            inconvenientes+="A";
        }

        if(inconvenientes.length()>0) {
            Toast.makeText(getBaseContext(), getString(R.string.doscamposobligatorios)+"", Toast.LENGTH_SHORT).show();
        }

        return inconvenientes;
    }


    public void dialogorecordatorio(){
        final Dialog dialog = new Dialog(ActivityRecordatorios.this);
        dialog.setContentView(R.layout.activity_recordatorios);


        final EditText txtfecha = (EditText)dialog.findViewById(R.id.txtfecha) ;
        final EditText txthora = (EditText)dialog.findViewById(R.id.txthora) ;

        //deshabilitar edicion del editext
        deshabilitareditext(txtfecha);
        deshabilitareditext(txthora);


        Button btnagregar = (Button) dialog.findViewById(R.id.btnagregar);

        if(operacion==1) {
            dialog.setTitle(getString(R.string.agregarrecordatorio)+"");
        }else if(operacion==2){
            dialog.setTitle(getString(R.string.actualizarrecordatorio)+"");
            txtfecha.setText(recordatorio.getFecha());
            txthora.setText(recordatorio.getHora());
        }

        txtfecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar c= Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityRecordatorios.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtfecha.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                    }
                }
                        ,year,mes,dia);
                datePickerDialog.show();
            }
        });


        txthora.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar c= Calendar.getInstance();
                int h=c.get(Calendar.HOUR_OF_DAY);
                int minutos=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityRecordatorios.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txthora.setText(hourOfDay+":"+minute);
                    }
                },h,minutos,false);
                timePickerDialog.show();
            }
        });



        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar(txtfecha,txthora).length()==0) {
                    if(operacion==1) {
                        Recordatorio recordatorio = new Recordatorio(txtfecha.getText() + "", txthora.getText() + "", nota.getId());
                        insertar(recordatorio);
                        setAdapters(nota.getId() + "");
                        dialog.dismiss();
                    }else if(operacion==2){
                        recordatorio.setFecha(txtfecha.getText() + "");
                        recordatorio.setHora(txthora.getText() + "");
                        recordatorio.setId(listViewAdapter.getItem(indice).getId());

                        actualizar(recordatorio);
                        setAdapters(nota.getId()+"");
                        dialog.dismiss();
                    }
                }

            }
        });


        Button btnsalir = (Button) dialog.findViewById(R.id.btnsalir);

        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    AdapterView.OnItemLongClickListener onItemClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            indice = i;
            try {
               btnList_click();
            }catch (Exception err){}
            return false;
        }
    };

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == 1) {

            return true;
        }else if (id==2) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

}//
