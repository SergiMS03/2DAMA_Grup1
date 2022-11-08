package com.example.a2dama_grup1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class missatge extends AppCompatActivity {

    String s1[];
    RecyclerView recyclerViewMissatge;

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
}
