package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;


public class sign_up extends AppCompatActivity{

    final String URL="http://192.168.207.154/signUp";
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

    }
}