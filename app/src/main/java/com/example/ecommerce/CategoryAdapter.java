package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtCategory.setText(categoryArrayList.get(position).getName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        holder.listItem.setLayoutManager(layoutManager);
        holder.listItem.setHasFixedSize(true);
//
//        itemArrayList = new ArrayList<>();
//        ArrayList<Integer> imageList = new ArrayList<>();
//        imageList.add(R.drawable.vietnam);
//        imageList.add(R.drawable.usa);
//        imageList.add(R.drawable.uk);
//        imageList.add(R.drawable.japan);
//        imageList.add(R.drawable.eur);
//
//        itemArrayList.add(new Item("1",2,"VN",10000,imageList,100,"vietnamese flag"));
//        itemArrayList.add(new Item("2",2,"USA Flag",5.1,imageList,51,"nothing"));
//        itemArrayList.add(new Item("3",2,"uk",10000,imageList,100,"vietnamese flag"));
//        itemArrayList.add(new Item("4",2,"jp",10000,imageList,100,"vietnamese flag"));
//        itemArrayList.add(new Item("5",2,"eur",10000,imageList,100,"vietnamese flag"));


        ItemAdapter itemAdapter = new ItemAdapter(context, categoryArrayList.get(position).getItemArrayList());
        holder.listItem.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ListItemActivity.class);
//                intent.putExtra("category",categoryArrayList.get(position));
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory;
        RecyclerView listItem;
        Button btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.categoryName);
            listItem = itemView.findViewById(R.id.recyclerViewListItem);
            btnMore = itemView.findViewById(R.id.seeMoreItemButton);
        }
    }

}
