package com.example.a2dama_grup1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class missatge extends AppCompatActivity {

    String s1[];
    RecyclerView recyclerViewMissatge;
    String URL = new objectIP().ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
