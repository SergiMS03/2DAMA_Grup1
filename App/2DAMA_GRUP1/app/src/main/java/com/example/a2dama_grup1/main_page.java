package com.example.a2dama_grup1;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class main_page extends AppCompatActivity{
    ArrayList<objectProduct> ppProducts= new ArrayList<>();
    RecyclerView recyclerViewHoritzontal;
    RecyclerView recyclerViewVertical;
    String URL = "http://192.168.17.135:";

    private DrawerLayout drawer;

    public class ScreenSlidePageFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.activity_main_page, container, false);

            return rootView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        String host = URL+"3000/getProducts";
        new getProducts().execute(host);
    }

    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    public void createRecycler() {
        setContentView(R.layout.activity_main_page);
        recyclerViewHoritzontal = findViewById(R.id.recyclerHoritzontal);
        recyclerViewVertical = findViewById(R.id.recyclerVertical);

        ProximityProductsAdapter ProximityProducts = new ProximityProductsAdapter(this, ppProducts);
        FeaturedProductsAdapter FeaturedProducts = new FeaturedProductsAdapter(this, ppProducts);

        ProximityProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_page.this, product_info.class);
                intent.putExtra("ID_PRODUCTO", Integer.toString(ppProducts.get(recyclerViewVertical.getChildAdapterPosition(view)).getId_producte()));
                startActivity(intent);
            }
        });

        recyclerViewHoritzontal.setAdapter(FeaturedProducts);
        recyclerViewVertical.setAdapter(ProximityProducts);
        recyclerViewHoritzontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVertical.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewVertical.setFocusable(false);
    }


    public class getProducts extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return dades(strings[0]);
        }

        private String dades(String queryString) {
            HttpURLConnection con = null;
            BufferedReader reader = null;
            String result = null;

            try {
                String url = queryString;
                Uri builtURI = Uri.parse(url).buildUpon().build();
                URL requestURL = new URL(builtURI.toString());
                con = (HttpURLConnection) requestURL.openConnection();
                con.setRequestMethod("POST");
                con.connect();

                InputStream inputStream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
                if (builder.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }

                result = builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null)
                    con.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray productArr = new JSONArray(s);
                for (int i = 0; i < productArr.length(); i++) {
                    JSONObject productObj = productArr.getJSONObject(i);
                    ppProducts.add(new objectProduct(productObj.getInt("id_producte"), productObj.getString("nom_producte"), (float) productObj.getDouble("preu"), productObj.getInt("stock"), productObj.getString("descripcio"), productObj.getString("path_img"), productObj.getInt("id_vendedor")));
                    ppProducts.get(i).setImg(new Image().Download("http://192.168.17.135:5500/servidor/" + ppProducts.get(i).getPathImg()));
                }
                createRecycler();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}