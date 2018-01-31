5package net.rox.agendacontactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Rox on 07/09/2017.
 */

public class AgendaCon extends AppCompatActivity{
    EditText nm,em,tw,tl,nc;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        nm=(EditText)findViewById(R.id.nombre);
        em=(EditText)findViewById(R.id.email);
        tw=(EditText)findViewById(R.id.twitter);
        tl=(EditText)findViewById(R.id.tel);
        nc=(EditText)findViewById(R.id.fechanac);
        bt=(Button)findViewById(R.id.guar);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Metos user=new Metos(nm.getText().toString(),em.getText().toString(),tw.getText().toString(),tl.getText().toString(),nc.getText().toString());
                myIntent.putExtra("rox",user);
                setResult(RESULT_OK,myIntent);
                finish();
            }
        });
    }
}
