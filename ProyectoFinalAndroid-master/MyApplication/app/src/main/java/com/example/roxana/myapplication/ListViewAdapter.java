package com.example.roxana.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roxana.myapplication.Pojos.Notas;

import java.util.List;


public class ListViewAdapter extends ArrayAdapter<Notas> {
    public ListViewAdapter(Context context, int resource, List<Notas> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        Notas nota = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.lbtitulo);
        TextView txtDescription = (TextView) v.findViewById(R.id.lbdescripcion);
        TextView lbfechacreacion = (TextView) v.findViewById(R.id.lbfechacreacion);

        txtTitle.setText(nota.getTitulo());
        txtDescription.setText(nota.getDescripcion());
        lbfechacreacion.setText(getContext().getString(R.string.fechacreacion)+nota.getFecha());

        return v;
    }
}
