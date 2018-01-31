package com.example.roxana.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roxana.myapplication.Model.DaoNotas;
import com.example.roxana.myapplication.Pojos.Notas;

import java.util.List;


public class ListViewAdapter1 extends ArrayAdapter<Notas> {
    public ListViewAdapter1(Context context, int resource, List<Notas> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item1, null);
        }
        final Notas tarea = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.lbtitulo);
        TextView txtDescription = (TextView) v.findViewById(R.id.lbdescripcion);
        TextView lbfechacumplimiento = (TextView) v.findViewById(R.id.lbfechacumplimiento);
        TextView lbhoracumplimiento = (TextView) v.findViewById(R.id.lbhoracumplimiento);

        txtTitle.setText(tarea.getTitulo());
        txtDescription.setText(tarea.getDescripcion());
        lbfechacumplimiento.setText(getContext().getString(R.string.fechacumplimiento)+tarea.getFecha());
        lbhoracumplimiento.setText(getContext().getString(R.string.horacumplimiento)+tarea.getHora());


        return v;
    }



}
