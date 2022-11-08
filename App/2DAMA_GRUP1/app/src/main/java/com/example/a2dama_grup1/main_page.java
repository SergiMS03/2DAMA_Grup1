package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class main_page extends AppCompatActivity {

    String s1[];
    int images[] = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h};
    RecyclerView recyclerViewHoritzontal;
    RecyclerView recyclerViewVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        recyclerViewHoritzontal = findViewById(R.id.recyclerHoritzontal);
        recyclerViewVertical = findViewById(R.id.recyclerVertical);

        s1 = getResources().getStringArray(R.array.ProductTitles);

        MyAdapter myAdapter = new MyAdapter(this, s1, images);
        recyclerViewHoritzontal.setAdapter(myAdapter);
        recyclerViewVertical.setAdapter(myAdapter);
        recyclerViewHoritzontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVertical.setFocusable(false);
    }

}