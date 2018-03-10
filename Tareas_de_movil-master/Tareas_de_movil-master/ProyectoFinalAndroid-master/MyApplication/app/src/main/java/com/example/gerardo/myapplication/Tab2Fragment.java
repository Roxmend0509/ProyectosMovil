package com.example.gerardo.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gerardo.myapplication.Model.DaoNotas;
import com.example.gerardo.myapplication.Model.DaoRecordatorios;
import com.example.gerardo.myapplication.Pojos.Notas;
import com.example.gerardo.myapplication.Pojos.Recordatorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private ViewStub stubList;
    private ListView listView;
    private ListViewAdapter1 listViewAdapter;
    private List<Notas> listatareas;

    EditText txtbuscar;
    private Button btneditar;

    private Notas nota;

    private int operacion = 0;
    private int indice = 0;

    private String operaciones[];

    public void armarlistaoperaciones(){
        armartarea();
        String operacionescad="";
        String estado = nota.isEstado();


        if(estado.equalsIgnoreCase("0")){
            operacionescad+=getString(R.string.marcarcomocumplida)+",";
        }else{
            operacionescad+=getString(R.string.marcarcomonocumplida)+",";
        }

        operacionescad+=getString(R.string.mostrararchivos)+",";
        operacionescad+=getString(R.string.mostrarrecordatorios)+",";
        operacionescad+=getString(R.string.agregarrecordatorio)+",";
        operacionescad+=getString(R.string.actualizar)+",";
        operacionescad+=getString(R.string.eliminar)+"";


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
                listatareas = new ArrayList<>();
                listatareas = daonotas.getnotas();
                if(txtbuscar.getText().length()>0) {
                    listViewAdapter = new ListViewAdapter1(getContext(), R.layout.list_item1, daonotas.buscarnotaotarea("2", txtbuscar.getText() + ""));
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);


        stubList = (ViewStub) view.findViewById(R.id.stub_list1);


        //Inflate ViewStub before get view
        stubList.inflate();


        listView = (ListView) view.findViewById(R.id.mylistview);



        //Register item lick
        //listView.setOnItemLongClickListener(onItemClick);
        //getTareaslist();
        clic();

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
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operacion=1;
                dialogtareas();
                //Toast.makeText(getContext(),"FloatButton2",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapters() {
        DaoNotas daotareas = new DaoNotas(getContext());
        listatareas = new ArrayList<>();
        listatareas = daotareas.gettareas();

        listViewAdapter = new ListViewAdapter1(getContext(), R.layout.list_item1, daotareas.gettareas());
        listView.setAdapter(listViewAdapter);

    }

    /*
    public List<Notas> getTareaslist() {
        //pseudo code to get product, replace your code to get real product here
        listatareas = new ArrayList<>();
        listatareas.add(new Notas("Tarea de Web", "Investigar Sobre LocalStorage",false));
        listatareas.add(new Notas( "Tarea de Movil", "Proyecto Final",true));
        listatareas.add(new Notas("Tarea de Programacion Logica", "Realizar un Ejemplo de Functures y Monadas",false));
        listatareas.add(new Notas("Tarea de Redes", "Realizar la Practica 3.5",false));
        listatareas.add(new Notas("Tared de Gestion de Proyectos", "Resumen y Avance del proyecto",true));
        listatareas.add(new Notas("Tarea de Taller de Investigacion", "Investigacion Recomendaciones para la presentacion de un Proyecto",false));
        listatareas.add(new Notas("Tarea de Actividad Complementaria", "Simulacion Proteus y Arduino del Proyecto",false));
        listatareas.add(new Notas("Tarea de Simulacion", "Ejercio 6.5 de libro",false));
        listatareas.add(new Notas("Tarea de Redes2", "Realizar el resumen del capitulo 10",true));
        listatareas.add(new Notas("tarea de Ingles", "Realizar una presentacion donde se describa a la Familia",false));

        //Toast.makeText(getContext(),listanotas.get(listanotas.size()-1).getTitulo(),Toast.LENGTH_SHORT).show();
        return listatareas;
    }
    */

    AdapterView.OnItemLongClickListener onItemClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            indice = i;
            btnList_click();
            return false;
        }
    };



    public void confirmacion(){

        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.deceaeliminar)+"")
                        .setIcon(android.R.drawable.ic_delete)
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
                                //Toast.makeText(getContext(),"Cancelar",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();

        dialog.show();


    }

    public void  btnList_click(){
        armarlistaoperaciones();
        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.operacionarealizar)+"")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                  if(operaciones[0].equalsIgnoreCase(getString(R.string.marcarcomonocumplida)+"")){
                                      nota.setEstado("0");
                                  }
                                   else if(operaciones[0].equalsIgnoreCase(getString(R.string.marcarcomocumplida)+"")){
                                        nota.setEstado("1");
                                    }

                                    actualizar(nota);
                                    setAdapters();


                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[1])){
                                    Intent multimedia = new Intent(getContext(),actividadmultimedia.class);
                                    multimedia.putExtra("id", nota.getId());
                                    multimedia.putExtra("titulo", nota.getTitulo());


                                    startActivityForResult(multimedia,1004);

                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[2])){
                                    Intent recordatorios = new Intent(getContext(),ActivityRecordatorios.class);
                                    recordatorios.putExtra("id", nota.getId());
                                    recordatorios.putExtra("titulo", nota.getTitulo());
                                    // Toast.makeText(getContext(),nota.getId()+ " " + nota.getTitulo(),Toast.LENGTH_SHORT).show();

                                    startActivityForResult(recordatorios,1003);

                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[3])){
                                    dialogorecordatorio();

                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[4])){
                                    operacion=2;
                                    nota = new Notas(listViewAdapter.getItem(indice).getTitulo(),listViewAdapter.getItem(indice).getDescripcion(),listViewAdapter.getItem(indice).getFecha(),listViewAdapter.getItem(indice).getHora(),listViewAdapter.getItem(indice).isEstado(),"1");
                                    dialogtareas();

                                }

                                if(operaciones[which].equalsIgnoreCase(operaciones[5])){
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


    public void insertar(Notas nota){
        DaoNotas dao = new DaoNotas(getContext());
        if(dao.Insert(nota)>1){
            Toast.makeText(getContext(),getString(R.string.tareainsertada)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),getString(R.string.tareanoinsertada)+"",Toast.LENGTH_LONG).show();
        }
    }


    public void insertar(Recordatorio recordatorio){
        DaoRecordatorios dao = new DaoRecordatorios(getContext());
        if(dao.Insert(recordatorio)>1){
            Toast.makeText(getContext(),getString(R.string.recordatorioinsertado)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),getString(R.string.recordatorionoinsertado)+"",Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getContext(),dao.registros()+"",Toast.LENGTH_LONG).show();
    }

    public void actualizar(Notas nota){
        DaoNotas dao = new DaoNotas(getContext());
        if(dao.Update(nota)>1){
            Toast.makeText(getContext(),getString(R.string.tareanoactualizada)+"",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),getString(R.string.tareaactualizada)+"",Toast.LENGTH_LONG).show();
        }
    }

    public void eliminar(String id){
        DaoNotas dao = new DaoNotas(getContext());
        if(dao.delete(id)>1){
            Toast.makeText(getContext(),getString(R.string.tareanoeliminada)+"",Toast.LENGTH_LONG).show();
        }else{
            setAdapters();
            Toast.makeText(getContext(),getString(R.string.tareaeliminada)+"",Toast.LENGTH_LONG).show();
        }
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

    public void deshabilitareditext(EditText txt){

        txt.setFocusable(false);
        txt.setFocusableInTouchMode(true);

        //deshabilitar edicion del editext
        txt.setInputType(InputType.TYPE_NULL);
        txt.setInputType(InputType.TYPE_NULL);

    }

    public String validar(EditText txt,EditText txt1,EditText txt2,EditText txt3){
        String inconvenientes="";
        if(txt.getText().toString().length()==0){
            inconvenientes+="A";
        }
        if(txt1.getText().toString().length()==0){
            inconvenientes+="A";
        }

        if(txt2.getText().toString().length()==0){
            inconvenientes+="A";
        }

        if(txt3.getText().toString().length()==0){
            inconvenientes+="A";
        }

        if(inconvenientes.length()>0) {
            Toast.makeText(getContext(), getString(R.string.cuatrocamposobligatorios)+"", Toast.LENGTH_SHORT).show();
        }

        return inconvenientes;
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

    public String fechasistema(){
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


    private  int dia,mes,year,h,minutos;
    private String fecha,hora;
    final Calendar c= Calendar.getInstance();

    public void dialogtareas(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_appupdatetareas);


        final EditText txttitulo = (EditText)dialog.findViewById(R.id.txttitulo) ;
        final EditText txtdescripcion = (EditText)dialog.findViewById(R.id.txtdescripcion) ;
        final EditText txtfecha = (EditText)dialog.findViewById(R.id.txtfecha) ;
        final EditText txthora = (EditText)dialog.findViewById(R.id.txthora) ;

        Button btnoperacion = (Button) dialog.findViewById(R.id.btnoperacion);

        //deshabilitar edicion del editext
        deshabilitareditext(txtfecha);
        deshabilitareditext(txthora);


        // if button is clicked, close the custom dialog

        if(operacion==1) {
            dialog.setTitle(getString(R.string.agregartarea)+"");
        }else if (operacion==2){
            dialog.setTitle(getString(R.string.actualizartarea)+"");
            txttitulo.setText(nota.getTitulo());
            txtdescripcion.setText(nota.getDescripcion());
            txtfecha.setText(nota.getFecha());
            txthora.setText(nota.getHora());
        }


        btnoperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(operacion==1) {
                    if (validar(txttitulo,txtdescripcion,txtfecha,txthora).length()==0){
                    insertar(new Notas(txttitulo.getText() + "", txtdescripcion.getText() + "", txtfecha.getText() + "", txthora.getText() + "", "0", "2"));

                        DaoNotas daoNotas = new DaoNotas(getContext());
                        DaoRecordatorios daorecordatorios = new DaoRecordatorios(getContext());

                        Recordatorio recordatorio = new Recordatorio(txtfecha.getText()+"", txthora.getText()+"",Integer.parseInt(daoNotas.getultimoelemento().getId()+""));
                        daorecordatorios.Insert(recordatorio);

                        //daoNotas.getultimoelemento();
                        setAdapters();

                        dialog.dismiss();
                    }
                }else if(operacion==2){
                    if (validar(txttitulo,txtdescripcion,txtfecha,txthora).length()==0) {
                        nota.setId(listViewAdapter.getItem(indice).getId());
                        nota.setTipo("2");
                        nota.setTitulo(txttitulo.getText() + "");
                        nota.setDescripcion(txtdescripcion.getText() + "");
                        nota.setFecha(txtfecha.getText() + "");
                        nota.setHora(txthora.getText() + "");
                        nota.setEstado(nota.isEstado());
                        actualizar(nota);
                        setAdapters();
                        dialog.dismiss();
                        //Toast.makeText(getContext(),nota.getId()+"",Toast.LENGTH_SHORT).show();
                        //actualizar(nota);
                    }
                }



            }
        });

        txtfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);

                txtfecha.setFocusable(false);
                txtfecha.setInputType(InputType.TYPE_NULL);
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

        txthora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                h=c.get(Calendar.HOUR_OF_DAY);
                minutos=c.get(Calendar.MINUTE);

                txthora.setFocusable(false);
                txthora.setInputType(InputType.TYPE_NULL);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       txthora.setText(hourOfDay+":"+minute);
                    }
                },h,minutos,false);
                timePickerDialog.show();

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
        // if button is clicked, close the custom dialog
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
