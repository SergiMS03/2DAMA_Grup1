package com.example.a2dama_grup1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class sign_up extends AppCompatActivity implements View.OnClickListener{

    private EditText nom;
    private EditText cognoms;
    private EditText email;
    private EditText pwd;
    private EditText tlf;
    private EditText descripcio;
    private RadioButton solicitar_artista;
    private Button registrarse;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        nom = (EditText) (findViewById(R.id.editText_nom));
        cognoms = (EditText) (findViewById(R.id.editText_cognoms);
        email = (EditText) (findViewById(R.id.editText_email));
        pwd = (EditText) (findViewById(R.id.editText_pwd));
        tlf = (EditText) (findViewById(R.id.editText_tlf));
        descripcio = (EditText) (findViewById(R.id.editText_nom));
        solicitar_artista = (RadioButton) (findViewById(R.id.btn_solicitar_artista));
        registrarse = (Button) findViewById(R.id.registerButton);
        registrarse.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        new signUp();
    }


    private class signUp extends AsyncTask<String, Void, String>{

        HttpURLConnection con;

        @Override
        protected String doInBackground(String... strings) {
            dades();
            return null;
        }

        private void dades(){
            try{

                URL url = new URL ("http://192.168.207.154:3000/signUp/"+nom+"/"+cognoms+"/"+email+"/"+pwd+"/"+descripcio+"/"+tlf+"/"+solicitar_artista);
                con = (HttpURLConnection) url.openConnection();
                con.connect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                con.disconnect();
            }

        }

    }
}