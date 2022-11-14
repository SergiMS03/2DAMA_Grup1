package com.example.a2dama_grup1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class main_page extends AppCompatActivity {
    ArrayList<objectProduct> ppProducts= new ArrayList<>();

    RecyclerView recyclerViewHoritzontal;
    RecyclerView recyclerViewVertical;

    public class ScreenSlidePageFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.activity_main_page, container, false);

            return rootView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String host = "http://192.168.207.155:3000/getProducts";
        String method = "POST";
        apiConnection api = new apiConnection();
        api.execute(host, method);
        while(api.getResult() == null){};
        createRecycler();
    }

    public void createRecycler() {
        setContentView(R.layout.activity_main_page);
        recyclerViewHoritzontal = findViewById(R.id.recyclerHoritzontal);
        recyclerViewVertical = findViewById(R.id.recyclerVertical);

        ProximityProductsAdapter ProximityProducts = new ProximityProductsAdapter(this, ppProducts);
        FeaturedProductsAdapter FeaturedProducts = new FeaturedProductsAdapter(this, ppProducts);

        ProximityProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_page.this, product_info.class);
                intent.putExtra("ID_PRODUCTO", ppProducts.get(recyclerViewVertical.getChildAdapterPosition(view)).getId_producte());
                startActivity(intent);
            }
        });

        recyclerViewHoritzontal.setAdapter(FeaturedProducts);
        recyclerViewVertical.setAdapter(ProximityProducts);
        recyclerViewHoritzontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVertical.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewVertical.setFocusable(false);
    }

    /*private void demoProducts(){//8 productos
        ppProducts.add(new objectProduct(15, "a", (float)1.20, 1, "a", R.drawable.a, 4));
        ppProducts.add(new objectProduct(23, "b", (float)10.20, 1, "b", R.drawable.b, 4));
        ppProducts.add(new objectProduct(36, "c", (float)100.20, 1, "c", R.drawable.c, 4));
        ppProducts.add(new objectProduct(40, "d", (float)2.20, 1, "d", R.drawable.d, 4));
        ppProducts.add(new objectProduct(56, "e", (float)20.20, 1, "e", R.drawable.e, 4));
        ppProducts.add(new objectProduct(64, "f", (float)200.20, 1, "f", R.drawable.f, 4));
        ppProducts.add(new objectProduct(73, "g", (float)3.20, 1, "g", R.drawable.g, 4));
        ppProducts.add(new objectProduct(81, "h", (float)30.20, 1, "h", R.drawable.h, 4));
    }*/

}