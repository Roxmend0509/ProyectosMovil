/*
 * Copyright (C) 2017 Anton Kaliturin <kaliturin@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.kaliturin.blacklist.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.kaliturin.blacklist.R;
import com.kaliturin.blacklist.fragments.ContactsFragment;
import com.kaliturin.blacklist.fragments.FragmentArguments;
import com.kaliturin.blacklist.fragments.JournalFragment;
import com.kaliturin.blacklist.fragments.SettingsFragment;
import com.kaliturin.blacklist.utils.DatabaseAccessHelper.Contact;
import com.kaliturin.blacklist.utils.Permissions;
import com.kaliturin.blacklist.utils.Settings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_JOURNAL = "com.kaliturin.blacklist.ACTION_JOURNAL";
    public static final String ACTION_SETTINGS = "com.kaliturin.blacklist.ACTION_SETTINGS";

    private static final String CURRENT_ITEM_ID = "CURRENT_ITEM_ID";
    private FragmentSwitcher fragmentSwitcher = new FragmentSwitcher();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private int selectedMenuItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permisos
        Permissions.checkAndRequest(this);

        //Iniciar los Ajustes Predeterminados
        Settings.initDefaults(this);

        // Barra
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Muestra información de la barra
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            View view = findViewById(R.id.toolbar_shadow);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }

        // cajón
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.Open_navigation_drawer, R.string.Close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Menu de Navegación
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Si tuvo una rotación
        int itemId;
        if (savedInstanceState != null) {
            //Obtener el menu guardado de navegación
            itemId = savedInstanceState.getInt(CURRENT_ITEM_ID);
        } else {
            //Escoge el fragmento de la acción de la actividad
            String action = getIntent().getAction();
            action = (action == null ? "" : action);
            switch (action) {

                case ACTION_SETTINGS:
                    //Cambiar información en el fragmento
                    itemId = R.id.nav_settings;
                    break;
                case ACTION_JOURNAL:
                    //Cambiar historial en el fragmento
                    itemId = R.id.nav_journal;
                    break;
                default:
                    if (Settings.getBooleanValue(this, Settings.GO_TO_JOURNAL_AT_START)) {
                        //Cambiar historial en el fragmento
                        itemId = R.id.nav_journal;
                    } else {
                        //Cambiar conversacion de lista negra en el fragmento
                        itemId = R.id.nav_black_list;
                    }
                    break;
            }
            //Cambiar al fragmento elegido
            fragmentSwitcher.switchFragment(itemId);
        }

        //Seleccionar menu de elementos de navegación
        selectNavigationMenuItem(itemId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_ITEM_ID, selectedMenuItemId);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!fragmentSwitcher.onBackPressed()) {
                if (!Settings.getBooleanValue(this, Settings.DONT_EXIT_ON_BACK_PRESSED)) {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        //Salir al elemento presionado
        if (itemId == R.id.nav_exit) {
            finish();
            return true;
        }

        // Cambia a un nuevo Fragmento
        fragmentSwitcher.switchFragment(itemId);
        drawer.closeDrawer(GravityCompat.START);


        selectNavigationMenuItem(itemId);

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Checamos codigo de resultado para la actividad hijo(puede ser un dialogo-actividad)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            fragmentSwitcher.updateFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        //Procesa un resultado de permiso
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Checa si tiene permisos y notifica acerca de que no lo tiene
        Permissions.notifyIfNotGranted(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

//----------------------------------------------------------------------------

    private void selectNavigationMenuItem(int itemId) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.getMenu().findItem(itemId).setChecked(true);
        // Guarda elemento seleccionado
        selectedMenuItemId = itemId;
    }
    //Cambia de actividades en el fragmento
    private class FragmentSwitcher implements FragmentArguments {
        private final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
        private ContactsFragment blackListFragment = new ContactsFragment();
        private JournalFragment journalFragment = new JournalFragment();
        private SettingsFragment settingsFragment = new SettingsFragment();

        boolean onBackPressed() {
            return journalFragment.dismissSnackBar() ||
                    blackListFragment.dismissSnackBar();
        }

        //Cambia el fragmento de menu de elementos
        void switchFragment(@IdRes int itemId) {
            Intent intent = getIntent();
            //Pasa los intents extras a el fragmento
            Bundle extras = intent.getExtras();
            Bundle arguments = (extras != null ? new Bundle(extras) : new Bundle());
            switch (itemId) {
                case R.id.nav_journal:
                    arguments.putString(TITLE, getString(R.string.Journal));
                    switchFragment(journalFragment, arguments);
                    break;
                case R.id.nav_black_list:
                    arguments.putString(TITLE, getString(R.string.Black_list));
                    arguments.putInt(CONTACT_TYPE, Contact.TYPE_BLACK_LIST);
                    switchFragment(blackListFragment, arguments);
                    break;
                case R.id.nav_settings:
                    arguments.putString(TITLE, getString(R.string.Settings));
                    switchFragment(settingsFragment, arguments);
                    break;
            }

            // remueve el uso de Extra
            intent.removeExtra(LIST_POSITION);
        }

        //Cambia a los fragmentos aprobados
        private void switchFragment(Fragment fragment, Bundle arguments) {
            //Remplaza la información mostrada en el fragmento
            Fragment current = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);
            if (current != fragment) {
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_layout, fragment, CURRENT_FRAGMENT).commit();
            }
        }

        // Editar la informacion del fragmento
        private void updateFragment() {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(fragment).attach(fragment).commit();
            }
        }
    }

}
