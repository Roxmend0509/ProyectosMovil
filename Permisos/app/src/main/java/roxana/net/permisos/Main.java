package roxana.net.permisos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main extends AppCompatActivity {
    private static final int MI_PERMISO_CALL_PHONE = 1;
    Button bt_llamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_llamar = (Button) findViewById(R.id.bt_llamar);

        bt_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("4451091363"));
                if (ActivityCompat.checkSelfPermission(Main.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Main.this, Manifest.permission.CALL_PHONE)) {
                        new SweetAlertDialog(Main.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Atención")
                                .setConfirmText("Deber otorgar permisos para realizar la llamada")
                                .setConfirmText("Aceptar Permiso")
                                .setCancelText("Cancelar Permiso")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {


                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                        ActivityCompat.requestPermissions(Main.this,
                                                new String[]{Manifest.permission.CALL_PHONE},
                                                MI_PERMISO_CALL_PHONE);
                                    }
                                })
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(Main.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MI_PERMISO_CALL_PHONE);
                    }
                }else{
                    startActivity(callIntent);

                }

            }
        });
    }
}
