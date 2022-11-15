package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

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

public class product_info extends AppCompatActivity {

    TextView title;
    TextView description;
    TextView price;
    TextView stock;
    objectProduct product = new objectProduct();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Intent intent = getIntent();
        String idProduct = intent.getStringExtra("ID_PRODUCTO");
        title = findViewById(R.id.productInfoName);
        description = findViewById(R.id.productInfoDescription);
        price = findViewById(R.id.productInfoPrice);
        stock = findViewById(R.id.productInfoStock);

        String host = "http://192.168.17.135:3000/getProduct/"+ idProduct;
        new getProduct().execute(host);
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
    }
}