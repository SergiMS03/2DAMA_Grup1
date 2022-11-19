package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;


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

public class chatList extends AppCompatActivity {
    RecyclerView recyclerChats;
    ArrayList<objectChat> ListaChat = new ArrayList<>();
    objectUser USER;
    String URL = new objectIP().ip;
    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        String host = URL+"3000/getChat/"+USER.id_usuari;
        new getChats().execute(host);
    }

    public void createRecycler() {
        setContentView(R.layout.activity_chat_list);
        recyclerChats = findViewById(R.id.recyclerChats);

        ChatListAdapter chatAdapter = new ChatListAdapter(this, ListaChat);
        chatAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chatList.this, missatge.class);
                intent.putExtra("USER", (Serializable) USER);
                intent.putExtra("ID_CHAT", Integer.toString(ListaChat.get(recyclerChats.getChildAdapterPosition(view)).getId_chat()));
                startActivity(intent);
            }
        });
        recyclerChats.setAdapter(chatAdapter);
        recyclerChats.setLayoutManager(new LinearLayoutManager(this));
        recyclerChats.setFocusable(false);
    }

    public class getChats extends AsyncTask<String, Void, String> {

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
                JSONArray chatArr = new JSONArray(s);
                for (int i = 0; i < chatArr.length(); i++) {
                    JSONObject chatObj = chatArr.getJSONObject(i);
                    ListaChat.add(new objectChat(chatObj.getInt("id_chat"), chatObj.getInt("id_venedor"), chatObj.getInt("id_comprador"), chatObj.getInt("id_producte"), chatObj.getString("nom_producte"), chatObj.getString("path_img")));
                    Bitmap image= new Image().Download(URL+"5500/servidor/" + ListaChat.get(i).getPath_img());
                    ListaChat.get(i).setImg(image);
                }
                createRecycler();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}