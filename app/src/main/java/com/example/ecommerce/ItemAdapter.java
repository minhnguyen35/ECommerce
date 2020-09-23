package com.example.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Item> itemArrayList;
    private int REQUEST_CODE_ITEM = 789;
    public ItemAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_display,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ArrayList<String> images = itemArrayList.get(position).getImageArrayList();
        if(images!=null && !images.isEmpty() && images.get(0)!="")
            Picasso.get().load(Uri.parse(itemArrayList.get(position).getImageArrayList().get(0))).into(holder.imageView);
        else holder.imageView.setImageResource(R.drawable.noimage);
        holder.txtName.setText(itemArrayList.get(position).getName());
        holder.txtPrice.setText(String.valueOf(itemArrayList.get(position).getPrice())+" VND");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ItemInfoActivity.class);
                intent.putExtra("item",itemArrayList.get(position));
                ((Activity)context).startActivityForResult(intent, REQUEST_CODE_ITEM);
            }
        });


    }


    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.itemImage);
            txtName = (TextView)itemView.findViewById(R.id.textViewItemName);
            txtPrice = (TextView)itemView.findViewById(R.id.itemPrice);
        }
    }
}
