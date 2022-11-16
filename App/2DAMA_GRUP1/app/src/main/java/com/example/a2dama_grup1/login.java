package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity {

    private EditText email;
    private EditText pwd;
    String URL = "http://192.168.17.135:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = (EditText) (findViewById(R.id.inputUserLogin));
        pwd = (EditText) (findViewById(R.id.inputPasswordLogin));
    }

    public void openSignUp(View view){
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
    }

    public void checkLogIn(View view){
        String HOST = URL+ "3000/logInClient/" +email.getText()+"/"+pwd.getText();
        new verifyLogIn().execute(HOST);
    }

    public void displayToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public class verifyLogIn extends AsyncTask<String, Void, String> {

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
            super.onPostExecute(s);
            try {
                if(s.equals("{}\n") || s == null || s.equals("false\n")){
                    displayToast("Alguna dada no és correcte");
                }
                else{
                    JSONArray userArr = new JSONArray(s);
                    for (int i = 0; i < userArr.length(); i++) {
                        JSONObject userObj = userArr.getJSONObject(i);
                        }
                    displayToast("Benvigut!");
                    Intent intent = new Intent(login.this, main_page.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}