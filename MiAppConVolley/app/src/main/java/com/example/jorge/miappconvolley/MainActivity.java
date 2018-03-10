package com.example.jorge.miappconvolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {



    /*String url ="http://www.google.com";
    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            // Display the first 500 characters of the response string.
            Log.d("SUPERVOLLEY","Response is: "+ response.substring(0,500));
            Toast.makeText(MainActivity.this, "Response is: "+ response.substring(0,500), Toast.LENGTH_LONG).show();

        }

        }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

            Log.d("SUPERVOLEYY","That didn't work!");

        }

    });*/



    // Instantiate the RequestQueue.
    RequestQueue mRequestQueue;
    // Instantiate the cache
    Cache cache;
    // Set up the network to use HttpURLConnection as the HTTP client.
    Network network;

    private NetworkImageView imageView;
    private ImageLoader imageLoader;


    private Spinner spn_categories;
    private Spinner spn_styles;

    private EditText txt_X;
    private EditText txt_Y;

    //private EditText editTextUrl;
    private Button buttonLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cache = new DiskBasedCache(this.getCacheDir(), 1024 * 1024); // 1MB cap

        network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
        // Add the request to the RequestQueue.
        //mRequestQueue.add(stringRequest);



        //editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        buttonLoad = (Button) findViewById(R.id.buttonLoad);
        imageView = (NetworkImageView) findViewById(R.id.imageView);
        //editTextUrl.setText("https://placeimg.com/640/480/any");

        txt_X = (EditText) findViewById(R.id.txt_X);
        txt_X.setText("100");
        txt_Y = (EditText) findViewById(R.id.txt_Y);
        txt_Y.setText("100");


        spn_categories = (Spinner) findViewById(R.id.spn_categories);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_categories.setAdapter(adapter);

        spn_styles = (Spinner) findViewById(R.id.spn_style);

        ArrayAdapter<CharSequence> adapter_2 = ArrayAdapter.createFromResource(this, R.array.array_styles, android.R.layout.simple_spinner_item);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_styles.setAdapter(adapter_2);

    }



    private void loadImage(String url){

        imageLoader = MyVolleySingleton.getInstance(this.getApplicationContext()).getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView, R.drawable.image, android.R.drawable.ic_dialog_alert));
        imageView.setImageUrl(url, imageLoader);

    }

    public void click_busca(View view) {

        String nX = txt_X.getText().toString();
        String nY = txt_Y.getText().toString();

        int index_cat = spn_categories.getSelectedItemPosition();
        String categoria = "";

        if(index_cat == 0){

            categoria = "any";

        }else if(index_cat == 1){

            categoria = "tech";

        }else if(index_cat == 2){

            categoria = "arch";

        }else if(index_cat == 3){

            categoria = "animals";

        }

        int index_style = spn_styles.getSelectedItemPosition();
        String url_fin = "";

        if(index_style == 0){

            url_fin = "https://placeimg.com/"+nX+"/"+nY+"/"+categoria;;

        }else if(index_style == 1){

            url_fin = "https://placeimg.com/"+nX+"/"+nY+"/"+categoria+"/sepia";

        }else if(index_style == 2){

            url_fin = "https://placeimg.com/"+nX+"/"+nY+"/"+categoria+"/grayscale";

        }


        String url = url_fin;

        Toast.makeText(MainActivity.this, "URL: "+url, Toast.LENGTH_LONG).show();

        //loadImage(editTextUrl.getText().toString());
        loadImage(url);


    }



}
