package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Category> categoryArrayList;
    private boolean isAdminSite;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList, boolean isAdminSite) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.isAdminSite = isAdminSite;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
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

        ItemAdapter itemAdapter = new ItemAdapter(context, categoryArrayList.get(position).getItemArrayList(), false);
        holder.listItem.setAdapter(itemAdapter);

        if(isAdminSite) {
            holder.removeButton.setAlpha(1);
            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    todo: remove cái category đi huynh
                     */
                    removeCategory(categoryArrayList.get(position));
                    /* ko cần remove nó ra khỏi array vì data thay đổi thì bên act chính nhận ra và load lại*/
                }
            });
        }
    }

    private void removeCategory(Category category)
    {
        if(removeItem(category)) {
            db.child("Category").child(category.getCategoryID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Remove Successfully", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean removeItem(Category category) {
        final boolean[] success = {true};
        for (int i = 0; i < category.getItemArrayList().size(); ++i) {
            db.child("Items").child(category.getItemArrayList().get(i).getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    success[0] = false;
                }
            });
        }
        return success[0];
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory;
        RecyclerView listItem;
        ImageButton removeButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.categoryName);
            listItem = itemView.findViewById(R.id.recyclerViewListItem);
            removeButton = itemView.findViewById(R.id.removeCategoryButton);
        }
    }

}
