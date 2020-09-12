package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    Context context;
    ArrayList<Item> itemArrayList;

    public ItemAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
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
        holder.imageView.setImageResource(itemArrayList.get(position).getImageArrayList().get(position));
        holder.txtName.setText(itemArrayList.get(position).getName());
        holder.txtPrice.setText(String.valueOf(itemArrayList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ItemInfoActivity.class);
                intent.putExtra("item",itemArrayList.get(position));
                context.startActivity(intent);
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
