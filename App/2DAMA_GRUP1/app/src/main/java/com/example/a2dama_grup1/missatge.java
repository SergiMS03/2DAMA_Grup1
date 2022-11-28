package com.example.a2dama_grup1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class missatge extends AppCompatActivity {
    String ID_CHAT;
    String id_product;
    objectUser USER;
    objectProduct product;
    TextView product_name;
    ImageView image_product;
    ArrayList<objectMessage> missatgeList = new ArrayList<>();
    RecyclerView recyclerViewMissatge;
    String URL = new objectIP().ip;
    private EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        id_product = getIntent().getStringExtra("ID_PRODUCT");
        ID_CHAT = getIntent().getStringExtra("ID_CHAT");

        String HOST = URL+ "3000/getMissatge/"+ID_CHAT;
        new getMessages().execute(HOST);
    }

    public void createRecycler() {
        setContentView(R.layout.activity_missatge);
        recyclerViewMissatge = findViewById(R.id.recyclerViewMissatge);

        MyAdapterMissatge chatAdapter = new MyAdapterMissatge(this, missatgeList, USER.id_usuari);
        recyclerViewMissatge.setAdapter(chatAdapter);
        recyclerViewMissatge.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMissatge.setFocusable(false);
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

    public void displayToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void clickSendMessage(View view){
        msg = (EditText) (findViewById(R.id.input_msg));
        if(msg.getText().length() <= 0){
            displayToast("El missatge és buit!");
        }else{
            String HOST = URL+"3000/sendMessage/"+ID_CHAT+"/"+USER.id_usuari+"/"
                    +msg.getText();
            new sendMessages().execute(HOST);
        }
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
            try {
                JSONArray missatgeArr = new JSONArray(s);
                for (int i = 0; i < missatgeArr.length(); i++) {
                    JSONObject missatgeObj = missatgeArr.getJSONObject(i);
                    missatgeList.add(new objectMessage(missatgeObj.getInt("id_missatge"), missatgeObj.getInt("id_chat"), missatgeObj.getInt("id_emisor"), missatgeObj.getString("missatge")));
                }
                new getProduct().execute( URL+"3000/getProduct/"+ id_product);
                createRecycler();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return dades(strings[0]);
        }

        private String dades(String queryString){
            HttpURLConnection con = null;
            BufferedReader reader = null;
            String result = null;

            try{
                String url = queryString;
                Uri builtURI = Uri.parse(url).buildUpon().build();
                URL requestURL = new URL(builtURI.toString());
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
            try {
                JSONArray productArr = new JSONArray(s);
                JSONObject productObj = productArr.getJSONObject(0);
                product = new objectProduct(productObj.getInt("id_producte"), productObj.getString("nom_producte"), (float)productObj.getDouble("preu"), productObj.getInt("stock"), productObj.getString("descripcio"), productObj.getString("path_img"), productObj.getInt("id_vendedor"));
                product.setImg(new Image().Download(URL+"80/servidor/"+ product.getPathImg()));
                product_name = findViewById(R.id.nomProductMissatge);
                image_product = findViewById(R.id.missatgeImage);
                product_name.setText(product.nom_producte);
                image_product.setImageBitmap(product.img);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class sendMessages extends AsyncTask<String, Void, String> {

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
            missatgeList.clear();
            String HOST = URL+ "3000/getMissatge/"+ID_CHAT;
            new getMessages().execute(HOST);
        }
    }
}
