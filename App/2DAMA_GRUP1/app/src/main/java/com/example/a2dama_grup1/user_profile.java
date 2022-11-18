package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;

public class user_profile extends AppCompatActivity {
    objectUser USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        setContentView(R.layout.activity_user_profile);
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

    public void clickUpload (View view){
        Intent intent = new Intent(user_profile.this, upload_product.class );
        intent.putExtra("USER", (Serializable) USER);
        startActivity(intent);
    }
}