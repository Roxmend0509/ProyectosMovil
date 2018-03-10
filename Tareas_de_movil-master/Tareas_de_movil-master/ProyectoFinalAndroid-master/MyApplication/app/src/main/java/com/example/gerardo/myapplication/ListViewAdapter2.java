package com.example.gerardo.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerardo.myapplication.Pojos.Notas;
import com.example.gerardo.myapplication.Pojos.Recordatorio;

import java.util.List;


public class ListViewAdapter2 extends ArrayAdapter<Recordatorio> {
    public ListViewAdapter2(Context context, int resource, List<Recordatorio> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item2, null);
        }
        final Recordatorio recordatorio = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView lbfecha = (TextView) v.findViewById(R.id.lbfecha);
        TextView lbhora = (TextView) v.findViewById(R.id.lbhora);

        lbfecha.setText(getContext().getString(R.string.fecha)+recordatorio.getFecha());
        lbhora.setText(getContext().getString(R.string.hora)+recordatorio.getHora());


        return v;
    }



}
