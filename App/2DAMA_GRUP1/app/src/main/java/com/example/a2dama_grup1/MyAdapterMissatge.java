package com.example.a2dama_grup1;


import static androidx.constraintlayout.widget.ConstraintProperties.END;
import static androidx.constraintlayout.widget.ConstraintProperties.PARENT_ID;
import static androidx.constraintlayout.widget.ConstraintProperties.RIGHT;
import static androidx.constraintlayout.widget.ConstraintProperties.START;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterMissatge extends RecyclerView.Adapter<MyAdapterMissatge.MyViewHolder> {

    ArrayList<objectMessage> missatgeList;
    Context context;
    int id_usuario;

    public MyAdapterMissatge(Context ct, ArrayList<objectMessage> missatgeList, int id_usuario){
        context = ct;
        this.missatgeList = missatgeList;
        this.id_usuario = id_usuario;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.missatge_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.myText1.setText(missatgeList.get(i).missatge);
        if(missatgeList.get(i).id_emisor == id_usuario){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(0);
            params.setMarginStart(450);
            params.setMargins(0, 37, 0, 0);
            //int resource = R.drawable.speech_balloon_right;
            Drawable drawable = context.getResources().getDrawable(R.drawable.speech_balloon_right);
            holder.layoutMsg.setBackgroundDrawable(drawable);
            holder.constaint.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return missatgeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1;
        ConstraintLayout constaint;
        LinearLayout layoutMsg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.myText1missatge);
            layoutMsg = itemView.findViewById(R.id.missatgeLayout);
            constaint = itemView.findViewById(R.id.missatgeConstraint);
        }
    }
}

