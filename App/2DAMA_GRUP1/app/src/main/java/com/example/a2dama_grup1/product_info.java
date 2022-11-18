package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class product_info extends AppCompatActivity {
    objectUser USER;
    TextView title;
    TextView description;
    TextView price;
    TextView stock;
    ImageView img;
    objectProduct product = new objectProduct();
    String URL = new objectIP().ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        setContentView(R.layout.activity_product_info);
        String idProduct = getIntent().getStringExtra("ID_PRODUCTO");
        title = findViewById(R.id.productInfoName);
        description = findViewById(R.id.productInfoDescription);
        price = findViewById(R.id.productInfoPrice);
        stock = findViewById(R.id.productInfoStock);
        img = findViewById(R.id.productInfoImage);

        String host = URL+"3000/getProduct/"+ idProduct;
        new getProduct().execute(host);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public void openChat(View view){
        Intent intent = new Intent(product_info.this, missatge.class);
        intent.putExtra("USER", (Serializable) USER);
        intent.putExtra("seller", product.idVenedorToString());
        startActivity(intent);
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
                product.setImg(new Image().Download(URL+"5500/servidor/"+ product.getPathImg()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            buildProductInfo(product);
        }
    }

    private void buildProductInfo(objectProduct product) {
        title.setText(product.getNom_producte());
        description.setText(product.getDescripcio());
        price.setText(product.priceToString());
        stock.setText(product.stockToString());
        img.setImageBitmap(product.getImg());
    }
}