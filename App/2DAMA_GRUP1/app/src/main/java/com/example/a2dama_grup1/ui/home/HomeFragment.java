package com.example.a2dama_grup1.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2dama_grup1.BarraNav;
import com.example.a2dama_grup1.FeaturedProductsAdapter;
import com.example.a2dama_grup1.Image;
import com.example.a2dama_grup1.ProximityProductsAdapter;
import com.example.a2dama_grup1.R;
import com.example.a2dama_grup1.databinding.FragmentHomeBinding;
import com.example.a2dama_grup1.main_page;
import com.example.a2dama_grup1.objectIP;
import com.example.a2dama_grup1.objectProduct;
import com.example.a2dama_grup1.objectUser;
import com.example.a2dama_grup1.product_info;

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

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;
    ArrayList<objectProduct> ppProducts= new ArrayList<>();
    RecyclerView recyclerViewHoritzontal;
    RecyclerView recyclerViewVertical;
    String URL = new objectIP().ip;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //USER = (objectUser) getIntent().getSerializableExtra("USER");
        String host = URL+"3000/getProducts";
        recyclerViewHoritzontal = view.findViewById(R.id.recyclerHoritzontal);
        recyclerViewVertical = view.findViewById(R.id.recyclerVertical);
        new getProducts().execute(host);
    }

    public class getProducts extends AsyncTask<String, Void, String> {

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
                con.setRequestMethod("POST");
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
                JSONArray productArr = new JSONArray(s);
                for (int i = 0; i < productArr.length(); i++) {
                    JSONObject productObj = productArr.getJSONObject(i);
                    ppProducts.add(new objectProduct(productObj.getInt("id_producte"), productObj.getString("nom_producte"), (float) productObj.getDouble("preu"), productObj.getInt("stock"), productObj.getString("descripcio"), productObj.getString("path_img"), productObj.getInt("id_vendedor")));
                    Bitmap image= new Image().Download(URL+"80/servidor/" + ppProducts.get(i).getPathImg());
                    ppProducts.get(i).setImg(image);
                }
                createRecycler();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createRecycler() {
        ProximityProductsAdapter ProximityProducts = new ProximityProductsAdapter(getContext(), ppProducts);
        FeaturedProductsAdapter FeaturedProducts = new FeaturedProductsAdapter(getContext(), ppProducts);

        ProximityProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), product_info.class);
                intent.putExtra("ID_PRODUCTO", Integer.toString(ppProducts.get(recyclerViewVertical.getChildAdapterPosition(view)).getId_producte()));
                intent.putExtra("ID_VENDEDOR", Integer.toString(ppProducts.get(recyclerViewVertical.getChildAdapterPosition(view)).getId_vendedor()));
                startActivity(intent);
            }
        });

        recyclerViewHoritzontal.setAdapter(FeaturedProducts);
        recyclerViewVertical.setAdapter(ProximityProducts);
        recyclerViewHoritzontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVertical.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewVertical.setFocusable(false);
    }
}