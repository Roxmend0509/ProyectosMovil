package com.example.roxana.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roxana.myapplication.Pojos.Multimedia;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Multimedia> {
    public GridViewAdapter(Context context, int resource, List<Multimedia> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }
        Multimedia multimedia = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
//        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);

        if(multimedia.getArchivo().toUpperCase().endsWith(".jpg".toUpperCase()) ||
        multimedia.getArchivo().toUpperCase().endsWith(".png".toUpperCase())
                || multimedia.getArchivo().toUpperCase().endsWith(".jpeg".toUpperCase())) {
            img.setImageResource(R.mipmap.otrologuitoimagen);
        }else if(multimedia.getArchivo().toUpperCase().endsWith(".aac".toUpperCase())
                || multimedia.getArchivo().toUpperCase().endsWith(".mp3".toUpperCase())
                ||multimedia.getArchivo().toUpperCase().endsWith(".wav".toUpperCase())||
                multimedia.getArchivo().toUpperCase().endsWith(".m4a".toUpperCase())){
            img.setImageResource(R.mipmap.audio);
        }else if(multimedia.getArchivo().toUpperCase().endsWith(".mp4".toUpperCase())||
                multimedia.getArchivo().toUpperCase().endsWith(".flv".toUpperCase())||
                multimedia.getArchivo().toUpperCase().endsWith(".avi".toUpperCase()) ||
               multimedia.getArchivo().toUpperCase().endsWith(".3gp".toUpperCase())){
            img.setImageResource(R.mipmap.video);
        }


//        txtTitle.setText("Archivo n");
        txtDescription.setText(multimedia.getDescripcion());

        return v;
    }
}
