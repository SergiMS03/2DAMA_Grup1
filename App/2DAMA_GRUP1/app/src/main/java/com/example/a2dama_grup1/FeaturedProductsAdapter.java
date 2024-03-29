package com.example.a2dama_grup1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeaturedProductsAdapter extends RecyclerView.Adapter<FeaturedProductsAdapter.MyViewHolder> implements View.OnClickListener {
    ArrayList<objectProduct> ppProducts;
    Context context;
    private View.OnClickListener listener;

    public FeaturedProductsAdapter(Context ct, ArrayList<objectProduct> ppProducts){
        context = ct;
        this.ppProducts = ppProducts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.featured_products, parent, false);
        view.setOnClickListener(this);
        return new FeaturedProductsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.id_producte = ppProducts.get(i).getId_producte();
        holder.productPrice.setText(ppProducts.get(i).priceToString());
        holder.myImage.setImageBitmap(ppProducts.get(i).getImg());
    }

    @Override
    public int getItemCount() {
        return ppProducts.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        int id_producte;
        TextView productPrice;
        ImageView myImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productPrice = itemView.findViewById(R.id.productPrice);
            myImage = itemView.findViewById(R.id.myImageView);
        }
    }
    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
