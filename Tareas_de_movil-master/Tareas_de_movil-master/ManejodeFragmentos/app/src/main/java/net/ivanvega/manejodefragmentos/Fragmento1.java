package net.ivanvega.manejodefragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by alcohonsilver on 23/10/17.
 */

public class Fragmento1 extends Fragment {

    Button btn1;
    Button btn2;
    Button btn3;
    private final String LOG_TAG = "log_fragment";


    Comucarfragmentos comunicacion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragmento_uno, container);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menucito, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDetach () {

        super.onDetach();
        Log.v(LOG_TAG, "onDetach");
    }

    public void onStart () {
        super.onStart();
        Log.v(LOG_TAG, "onStart");
    }

    @Override
    public void onResume () {
        super.onResume();
        Log.v(LOG_TAG, "onResume");
    }

    @Override
    public void onPause () {
        super.onPause();
        Log.v(LOG_TAG, "onPause");
    }

    @Override
    public void onStop () {
        super.onStop();
        Log.v(LOG_TAG, "onStop");
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        Log.v(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }





    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "onActivityCreated");

        comunicacion = (Comucarfragmentos) getActivity();

        btn1 = (Button) getActivity().findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                comunicacion.responder("Texto1");
            }

        });

        btn2 = (Button) getActivity().findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                comunicacion.responder("Texto2");
            }

        });

        btn3 = (Button) getActivity().findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                comunicacion.responder("Texto3");

            }

        });



    }
}
