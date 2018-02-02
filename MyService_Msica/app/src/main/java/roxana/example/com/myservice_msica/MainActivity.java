package roxana.example.com.myservice_msica;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    Button iniciar;
    Button detener;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciar = (Button) findViewById(R.id.btnStart);
        iniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startService(new Intent(MainActivity.this,
                        ServicioMusica.class));
            }
        });
        detener = (Button) findViewById(R.id.btnStop);
        detener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this,
                        ServicioMusica.class));
            }
        });
    }
}

