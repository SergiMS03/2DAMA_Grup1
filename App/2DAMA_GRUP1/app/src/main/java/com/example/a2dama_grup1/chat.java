package com.example.a2dama_grup1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class chat extends AppCompatActivity {
    String PRODUCT_ID;
    String SELLER_ID;
    objectUser USER;
    String s1[];
    RecyclerView recyclerViewMissatge;
    String URL = new objectIP().ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        PRODUCT_ID = getIntent().getStringExtra("PRODUCT");
        SELLER_ID = getIntent().getStringExtra("SELLER");
        String HOST = URL+ "3000/getMissatges/"+PRODUCT_ID+"/"+SELLER_ID+"/"+USER.id_usuari;
        new getMessages().execute(HOST);
        setContentView(R.layout.activity_missatge);
        recyclerViewMissatge = findViewById(R.id.recyclerViewMissatge);

        s1 = getResources().getStringArray(R.array.ProductTitles);

        MyAdapterMissatge MyAdapterMissatge = new MyAdapterMissatge(this, s1);
        recyclerViewMissatge.setAdapter(MyAdapterMissatge);
        recyclerViewMissatge.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_profile_id){
            Intent intent = new Intent(this, user_profile.class);
            startActivity(intent);
        } else if(id == R.id.menu_logout_id){
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class getMessages extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return dades(strings[0]);
        }

        private String dades(String queryString){
            HttpURLConnection con = null;
            BufferedReader reader = null;
            String result = null;

            try{
                Log.i("LOGINFO", "dades: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                String url = queryString;
                Uri builtURI = Uri.parse(url).buildUpon().build();
                java.net.URL requestURL = new URL(builtURI.toString());
                con = (HttpURLConnection) requestURL.openConnection();
                con.setRequestMethod("GET");
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
            }finally{
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

        protected void onPostExecute(String s){
            super.onPostExecute(s);
        }
    }
}
