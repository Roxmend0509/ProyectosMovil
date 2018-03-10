package net.ivanvega.miaudiolibros;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import net.ivanvega.miaudiolibros.fragments.DetalleFragment;
import net.ivanvega.miaudiolibros.fragments.SelectorFragment;

import static net.ivanvega.miaudiolibros.R.id.fab;

public class MainActivity extends AppCompatActivity {
    RecyclerView r_v;
    AdaptadorLibros adp;
    private AdaptadorLibrosFiltro adaptador;
    RecyclerView.LayoutManager lmrv;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_preferencias) {
            Toast.makeText(this, "Preferencias", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menu_ultimo) {
            irUltimoVisitado();
            return true;
        } else if (id == R.id.menu_buscar) {
            return true;
        } else if (id == R.id.menu_acerca) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mensaje de Acerca De");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void irUltimoVisitado() {
        SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if (id >= 0) {
            mostrarDetalle(id);
        } else {
            Toast.makeText(this, "Sin última vista", Toast.LENGTH_LONG).show();
        }
    }
    //d


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.containt_main);
        adaptador = ((Aplicacion) getApplicationContext()).getAdaptador();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(
//                R.id.fab);
//
//
//        fab.setOnClickListener(new View.OnClickListener() {@Override
//        public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action",
//                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
//        }     });

        if ((findViewById(R.id.contenedor_pequeno) != null)
                &&
                (getSupportFragmentManager().findFragmentById(
                        R.id.contenedor_pequeno) == null)) {
            SelectorFragment primerFragment = new SelectorFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contenedor_pequeno, primerFragment)
                    .commit();
        }


        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Todos"));
        tabs.addTab(tabs.newTab().setText("Nuevos"));
        tabs.addTab(tabs.newTab().setText("Leidos"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        adaptador.setNovedad(false);
                        adaptador.setLeido(false);
                        break;
                    case 1:

                        adaptador.setNovedad(true);
                        adaptador.setLeido(false);
                        break;             case 2:

                            //Leidos
                              adaptador.setNovedad(false);
                        adaptador.setLeido(true);
                        break;         }
                adaptador.notifyDataSetChanged();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {} });


                }

            public void mostrarDetalle(int id) {
                DetalleFragment detalleFragment = (DetalleFragment)
                        getSupportFragmentManager().findFragmentById(R.id.detalle_fragment);
                if (detalleFragment != null) {
                    detalleFragment.ponInfoLibro(id);
                } else {
                    DetalleFragment nuevoFragment = new DetalleFragment();
                    Bundle args = new Bundle();
                    args.putInt(DetalleFragment.ARG_ID_LIBRO, id);
                    nuevoFragment.setArguments(args);
                    FragmentTransaction transaccion = getSupportFragmentManager()
                            .beginTransaction();
                    transaccion.replace(R.id.contenedor_pequeno, nuevoFragment);
                    transaccion.addToBackStack(null);
                    transaccion.commit();
                    SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("ultimo", id);
                    editor.commit();
                }
            }
        }
