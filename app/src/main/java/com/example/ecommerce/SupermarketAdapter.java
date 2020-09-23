package com.example.ecommerce;

import android.app.Activity;
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

    private Context context;
    private ArrayList<Supermarket> supermarketArrayList;
    private final int REQUEST_CODE_BRANCH = 123;

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
        String logo = supermarketArrayList.get(position).getLogo();
        if(logo!=null && logo!="")
            Picasso.get().load(Uri.parse(logo)).into(holder.imageView);
        else if (supermarketArrayList.get(position).getSupermarketID().equals("-1"))
            holder.imageView.setImageResource(R.drawable.logo);
        else holder.imageView.setImageResource(R.drawable.noimage);
        holder.textView.setText(supermarketArrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,BranchMenuActivity.class);
                intent.putExtra("supermarketID",supermarketArrayList.get(position).getSupermarketID());
                ((Activity)context).startActivityForResult(intent,REQUEST_CODE_BRANCH);
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
