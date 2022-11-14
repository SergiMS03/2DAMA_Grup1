package com.example.a2dama_grup1;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.a2dama_grup1.R;
import com.example.a2dama_grup1.objectProduct;

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

public  class apiConnection extends AsyncTask<String, Void, String> {

    String result;

    @Override
    protected String doInBackground(String... strings) {
        return dades(strings[0], strings[1]);
    }

    private String dades(String host, String method){
        HttpURLConnection con = null;
        BufferedReader reader = null;
        String result = null;

        try{
            String url = host;
            Uri builtURI = Uri.parse(url).buildUpon().build();
            URL requestURL = new URL(builtURI.toString());
            con = (HttpURLConnection) requestURL.openConnection();
            con.setRequestMethod(method);
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
        result = s;
    }

}
