package com.example.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<String> arrayList;
    Context context;
    ArrayList<Item> itemArrayList;
    public CategoryAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCategory.setText(arrayList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        holder.listItem.setLayoutManager(layoutManager);
        holder.listItem.setHasFixedSize(true);

        itemArrayList = new ArrayList<>();
        ArrayList<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.vietnam);
        imageList.add(R.drawable.usa);
        imageList.add(R.drawable.uk);
        imageList.add(R.drawable.japan);
        imageList.add(R.drawable.eur);

        itemArrayList.add(new Item("1",2,"VN",10000,imageList,100,"vietnamese flag"));
        itemArrayList.add(new Item("2",2,"USA Flag",5.1,imageList,51,"nothing"));
        itemArrayList.add(new Item("3",2,"uk",10000,imageList,100,"vietnamese flag"));
        itemArrayList.add(new Item("4",2,"jp",10000,imageList,100,"vietnamese flag"));
        itemArrayList.add(new Item("5",2,"eur",10000,imageList,100,"vietnamese flag"));


        ItemAdapter itemAdapter = new ItemAdapter(itemArrayList,context);
        holder.listItem.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory;
        RecyclerView listItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.categoryName);
            listItem = itemView.findViewById(R.id.recyclerViewListItem);
        }
    }

}
