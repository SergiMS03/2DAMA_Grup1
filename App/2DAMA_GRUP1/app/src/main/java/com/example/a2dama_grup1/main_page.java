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
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class main_page extends AppCompatActivity{
    objectUser USER;
    ArrayList<objectProduct> ppProducts= new ArrayList<>();
    RecyclerView recyclerViewHoritzontal;
    RecyclerView recyclerViewVertical;
    String URL = new objectIP().ip;

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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        String host = URL+"3000/getProducts";
        new getProducts().execute(host);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_profile_id){
            Intent intent = new Intent(main_page.this, user_profile.class);
            intent.putExtra("USER", (Serializable) USER);
            startActivity(intent);
        }else if(id == R.id.chats){
            Intent intent = new Intent(main_page.this, chatList.class);
            intent.putExtra("USER", (Serializable) USER);
            startActivity(intent);
        }
        else if(id == R.id.menu_logout_id){
            Intent intent = new Intent(main_page.this, login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                intent.putExtra("USER", (Serializable) USER);
                intent.putExtra("ID_PRODUCTO", Integer.toString(ppProducts.get(recyclerViewVertical.getChildAdapterPosition(view)).getId_producte()));
                intent.putExtra("ID_VENDEDOR", Integer.toString(ppProducts.get(recyclerViewVertical.getChildAdapterPosition(view)).getId_vendedor()));
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
                    Bitmap image= new Image().Download(URL+"80/servidor/" + ppProducts.get(i).getPathImg());
                    ppProducts.get(i).setImg(image);
                }
                createRecycler();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}