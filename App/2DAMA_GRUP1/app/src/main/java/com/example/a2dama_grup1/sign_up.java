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
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class sign_up extends AppCompatActivity implements View.OnClickListener{

    private EditText nom;
    private EditText cognoms;
    private EditText email;
    private EditText pwd;
    private EditText tel;
    private EditText descripcio;
    private RadioButton artist_req;
    private Button registrarse;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        nom = (EditText) (findViewById(R.id.editText_nom));
        cognoms = (EditText) (findViewById(R.id.editText_cognoms));
        email = (EditText) (findViewById(R.id.editText_email));
        pwd = (EditText) (findViewById(R.id.editText_pwd));
        tel = (EditText) (findViewById(R.id.editText_tlf));
        descripcio = (EditText) (findViewById(R.id.editText_descripcio));
        artist_req = (RadioButton) (findViewById(R.id.btn_solicitar_artista));
        registrarse = (Button) findViewById(R.id.registerButton_signUp);
        registrarse.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {//Probar POST!!!
        Log.i("LOGINFO", "onClick: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String HOST = "http://192.168.1.45:3000/signUp/"+nom.getText()+"/"+cognoms.getText()+"/"
                +email.getText()+"/"+pwd.getText()+"/"+descripcio.getText()+"/"+tel.getText()+"/"+ artist_req.isChecked();
        new signUp().execute(HOST);
    }

    public void displayToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public class signUp extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.i("LOGINFO", "do in Background: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

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
                if(s.equals("0\n")){
                    displayToast("Registre correcte");
                    Intent intent = new Intent(sign_up.this, main_page.class);
                    startActivity(intent);
                }
                else{
                    displayToast("Alguna dada no és correcte");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}