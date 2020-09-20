package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SupermarketAdapter extends RecyclerView.Adapter<SupermarketAdapter.ViewHolder> {

    Context context;
    ArrayList<Supermarket> supermarketArrayList;

    public SupermarketAdapter(Context context, ArrayList<Supermarket> supermarketArrayList) {
        this.context = context;
        this.supermarketArrayList = supermarketArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.supermarket_display, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(supermarketArrayList.get(position).getLogo()!="")
            Picasso.get().load(Uri.parse(supermarketArrayList.get(position).getLogo())).into(holder.imageView);
        holder.textView.setText(supermarketArrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,BranchMenuActivity.class);
                intent.putExtra("supermarketID",supermarketArrayList.get(position).getSupermarketID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return supermarketArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageLogo);
            textView = itemView.findViewById(R.id.textViewSupermarketName);
        }
    }
}
