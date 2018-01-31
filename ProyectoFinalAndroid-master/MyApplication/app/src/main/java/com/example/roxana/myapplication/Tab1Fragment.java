package com.example.roxana.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class Tab1Fragment extends Fragment{
    private static final String TAG = "Tab1Fragment";

    private ViewStub stubList;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<Notas> listanotas;
    private Notas nota;
    private Context context;

    private EditText txttitulo;
    private EditText txtdescripcion;

    private int operacion = 0;
    private int indice = 0;
    private EditText txtbuscar;


    String operaciones[];


    public void armarlistaoperaciones(){
        armartarea();
        String operacionescad="";

        operacionescad+= getString(R.string.mostrararchivos)+",";
        operacionescad+=getString(R.string.mostrarrecordatorios)+",";
        operacionescad+=getString(R.string.actualizar)+",";
        operacionescad+= getString(R.string.eliminar)+"";


        operaciones =  operacionescad.split(",");

    }

    public void buscar(View view){

        txtbuscar = (EditText)view.findViewById(R.id.txtbuscar);
        txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DaoNotas daonotas = new DaoNotas(getContext());
                listanotas = new ArrayList<>();
                listanotas = daonotas.getnotas();
                if(txtbuscar.getText().length()>0) {
                    listViewAdapter = new ListViewAdapter(getContext(), R.layout.list_item, daonotas.buscarnotaotarea("1", txtbuscar.getText() + ""));
                    listView.setAdapter(listViewAdapter);
                }else{
                    setAdapters();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtbuscar.setInputType(InputType.TYPE_CLASS_TEXT);
                txtbuscar.setFocusable(true);
            }
        });

        txtbuscar.setInputType(InputType.TYPE_NULL);

    }

    public void armartarea(){
        nota = new Notas();
        nota.setId(listViewAdapter.getItem(indice).getId());
        nota.setTitulo(listViewAdapter.getItem(indice).getTitulo());
        nota.setDescripcion(listViewAdapter.getItem(indice).getDescripcion());
        nota.setFecha(listViewAdapter.getItem(indice).getFecha());
        nota.setHora(listViewAdapter.getItem(indice).getHora());
        nota.setEstado(listViewAdapter.getItem(indice).isEstado());
        nota.setTipo("2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);


        stubList = (ViewStub) view.findViewById(R.id.stub_list);


        //inflar ViewStub antes de obtener la vista
        stubList.inflate();


        listView = (ListView) view.findViewById(R.id.mylistview);


        clic();
       //listView.setOnItemLongClickListener(onItemClick);
        //getnotaslist();

            setAdapters();
            botonflotante(view);
            buscar(view);


        return view;
    }

    public void clic(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indice = i;
                btnList_click();
            }
        });
    }
    private void botonflotante(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operacion=1;

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_appupdatenotas);

                final EditText txttitulo = (EditText) dialog.findViewById(R.id.txttitulo) ;
                final EditText txtdescripcion = (EditText) dialog.findViewById(R.id.txtdescripcion) ;
                Button btnoperacion = (Button) dialog.findViewById(R.id.btnoperacion);

                if(operacion==1) {
                    dialog.setTitle(getString(R.string.agregarnota)+"");
                }else if (operacion==2){
                    dialog.setTitle(getString(R.string.actualizarnota)+"");
                    txttitulo.setText(nota.getTitulo());
                    txtdescripcion.setText(nota.getDescripcion());
                }


                btnoperacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(operacion==1) {
                            if (validar(txttitulo,txtdescripcion).length()==0){
                                insertar(new Notas(txttitulo.getText() + "", txtdescripcion.getText() + "", fecha(), horasistema(), "0", "1"));
                                setAdapters();
                                dialog.dismiss();
                            }
                        }else if(operacion==2){
                            if (validar(txttitulo,txtdescripcion).length()==0) {
                                nota.setTitulo(txttitulo.getText() + "");
                                nota.setDescripcion(txtdescripcion.getText() + "");
                                nota.setId(listViewAdapter.getItem(indice).getId());
                                //Toast.makeText(getContext(),nota.getId()+"",Toast.LENGTH_SHORT).show();
                                actualizar(nota);
                                setAdapters();
                                dialog.dismiss();
                            }
                        }



                    }
                });


                Button btnsalir = (Button) dialog.findViewById(R.id.btnsalir);
                // if button is clicked, close the custom dialog
                btnsalir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                //Toast.makeText(getContext(),"FloatButton1",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapters() {
           DaoNotas daonotas = new DaoNotas(getContext());
            listanotas = new ArrayList<>();
            listanotas = daonotas.getnotas();

            listViewAdapter = new ListViewAdapter(getContext(), R.layout.list_item, daonotas.getnotas());
            listView.setAdapter(listViewAdapter);

    }

    public List<Notas> getnotaslist() {
        //cargar tareas estaticas
        listanotas = new ArrayList<>();
        listanotas.add(new Notas("Tarea de Web", "Investigar Sobre LocalStorage"));
        listanotas.add(new Notas( "Tarea de Movil", "Proyecto Final"));
        listanotas.add(new Notas("Tarea de Programacion Logica", "Realizar un Ejemplo de Functures y Monadas"));
        listanotas.add(new Notas("Tarea de Redes", "Realizar la Practica 3.5"));
        listanotas.add(new Notas("Tared de Gestion de Proyectos", "Resumen y Avance del proyecto"));
        listanotas.add(new Notas("Tarea de Taller de Investigacion", "Investigacion Recomendaciones para la presentacion de un Proyecto"));
        listanotas.add(new Notas("Tarea de Actividad Complementaria", "Simulacion Proteus y Arduino del Proyecto"));
        listanotas.add(new Notas("Tarea de Simulacion", "Ejercio 6.5 de libro"));
        listanotas.add(new Notas("Tarea de Redes2", "Realizar el resumen del capitulo 10"));
        listanotas.add(new Notas("tarea de Ingles", "Realizar una presentacion donde se describa a la Familia"));

        //Toast.makeText(getContext(),listanotas.get(listanotas.size()-1).getTitulo(),Toast.LENGTH_SHORT).show();
        return listanotas;
    }


    AdapterView.OnItemLongClickListener onItemClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            indice = i;
            btnList_click();
            return false;
        }
    };

    public void  btnList_click(){
        armarlistaoperaciones();

        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.operacionarealizar)+"")
                        .setIcon(R.mipmap.opc)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                    Intent multimedia = new Intent(getContext(),actividadmultimedia.class);
                                    multimedia.putExtra("id", nota.getId());
                                    multimedia.putExtra("titulo", nota.getTitulo());


                                    startActivityForResult(multimedia,1004);

                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[1])){
                                    Intent recordatorios = new Intent(getContext(),ActivityRecordatorios.class);
                                    recordatorios.putExtra("id", nota.getId());
                                    recordatorios.putExtra("titulo", nota.getTitulo());
                                   // Toast.makeText(getContext(),nota.getId()+ " " + nota.getTitulo(),Toast.LENGTH_SHORT).show();


                                    startActivityForResult(recordatorios,1003);

                                }



                                if(operaciones[which].equalsIgnoreCase(operaciones[2])){
                                    operacion=2;
                                    nota = new Notas(listViewAdapter.getItem(indice).getTitulo(),listViewAdapter.getItem(indice).getDescripcion(),listViewAdapter.getItem(indice).getFecha(),listViewAdapter.getItem(indice).getHora(),"1");
                                    dialogonotas();

                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[3])){
                                    nota = new Notas();
                                    nota.setId(listViewAdapter.getItem(indice).getId());
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
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.deceaeliminar)+"")
                        .setIcon(R.mipmap.bot)
                        .setMessage(getString(R.string.advertenciaeliminar)+"")
                        .setPositiveButton(getString(R.string.aceptar)+"", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                           eliminar(nota.getId()+"");

                            }
                        })
                        .setNegativeButton(getString(R.string.cancelar)+"", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // Toast.makeText(getContext(),"Cancelar",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();

        dialog.show();

    }

    public void insertar(Notas nota){
        DaoNotas dao = new DaoNotas(getContext());
        if(dao.Insert(nota)>1){
            Toast.makeText(getContext(),getString(R.string.notainsertada)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),getString(R.string.notanoinsertada)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void insertar(Recordatorio recordatorio){
        DaoRecordatorios dao = new DaoRecordatorios(getContext());
        if(dao.Insert(recordatorio)>1){
            Toast.makeText(getContext(),getString(R.string.recordatorioinsertado)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),getString(R.string.recordatorionoinsertado)+"",Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(getContext(),dao.registros()+"",Toast.LENGTH_LONG).show();
    }

    public void actualizar(Notas nota){
        DaoNotas dao = new DaoNotas(getContext());
        if(dao.Update(nota)>1){
            Toast.makeText(getContext(),getString(R.string.notanoactualizada)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),getString(R.string.notaactualizada)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void eliminar(String id){
        DaoNotas dao = new DaoNotas(getContext());
        if(dao.delete(id)>1){
            Toast.makeText(getContext(),getString(R.string.notanoeliminada)+"",Toast.LENGTH_LONG).show();
        }else{
            setAdapters();
            Toast.makeText(getContext(),getString(R.string.notaeliminada)+"",Toast.LENGTH_LONG).show();
        }
    }

    public String fecha(){
        final Calendar c= Calendar.getInstance();
        int dia=c.get(Calendar.DAY_OF_MONTH);
        int mes=c.get(Calendar.MONTH)+1;
        int year=c.get(Calendar.YEAR);
        return year+"/"+mes+"/"+dia;
    }
    public String horasistema(){
        final Calendar c= Calendar.getInstance();
        int h=c.get(Calendar.HOUR_OF_DAY);
        int minutos=c.get(Calendar.MINUTE);
        return h+":"+minutos;
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
            Toast.makeText(getContext(), getString(R.string.doscamposobligatorios)+"", Toast.LENGTH_SHORT).show();
        }

        return inconvenientes;
    }
    public void dialogonotas(){


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_appupdatenotas);




        final EditText txttitulo = (EditText) dialog.findViewById(R.id.txttitulo) ;
        final EditText txtdescripcion = (EditText) dialog.findViewById(R.id.txtdescripcion) ;
        Button btnoperacion = (Button) dialog.findViewById(R.id.btnoperacion);
        // if button is clicked, close the custom dialog

        if(operacion==1) {
            dialog.setTitle(getString(R.string.agregarnota)+"");
        }else if (operacion==2){
            dialog.setTitle(getString(R.string.actualizarnota)+"");
            txttitulo.setText(nota.getTitulo());
            txtdescripcion.setText(nota.getDescripcion());
        }


        btnoperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(operacion==1) {
                    if (validar(txttitulo,txtdescripcion).length()==0){
                        insertar(new Notas(txttitulo.getText() + "", txtdescripcion.getText() + "", fecha(), horasistema(), "0", "1"));
                        setAdapters();
                        dialog.dismiss();
                    }
                }else if(operacion==2){
                    if (validar(txttitulo,txtdescripcion).length()==0) {
                        nota.setTitulo(txttitulo.getText() + "");
                        nota.setDescripcion(txtdescripcion.getText() + "");
                        nota.setId(listViewAdapter.getItem(indice).getId());
                        //Toast.makeText(getContext(),nota.getId()+"",Toast.LENGTH_SHORT).show();
                        actualizar(nota);
                        setAdapters();
                        dialog.dismiss();
                    }
                }



            }
        });


        Button btnsalir = (Button) dialog.findViewById(R.id.btnsalir);
        // if button is clicked, close the custom dialog
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void deshabilitareditext(EditText txt){

        txt.setFocusable(false);
        //txt.setFocusableInTouchMode(true);//habilitar edicion del editext

        //deshabilitar edicion del editext
        txt.setInputType(InputType.TYPE_NULL);
        txt.setInputType(InputType.TYPE_NULL);

    }
    public void dialogorecordatorio(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_recordatorios);



        final EditText txtfecha = (EditText)dialog.findViewById(R.id.txtfecha) ;
        final EditText txthora = (EditText)dialog.findViewById(R.id.txthora) ;

        //deshabilitar edicion del editext
        deshabilitareditext(txtfecha);
        deshabilitareditext(txthora);


        Button btnagregar = (Button) dialog.findViewById(R.id.btnagregar);

        dialog.setTitle(getString(R.string.agregarrecordatorio)+"");

        txtfecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar c= Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
                    Recordatorio recordatorio = new Recordatorio(txtfecha.getText() + "", txthora.getText() + "", listViewAdapter.getItem(indice).getId());
                    insertar(recordatorio);
                    dialog.dismiss();
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







}//
