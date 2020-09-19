package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {
    Context context;
    ArrayList<Branch> branchArrayList;

    public BranchAdapter(Context context, ArrayList<Branch> branchArrayList) {
        this.context = context;
        this.branchArrayList = branchArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.branch_display,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(branchArrayList.get(position).getLogo()!="")
            Picasso.get().load(Uri.parse(branchArrayList.get(position).getLogo())).into(holder.imageView);
        holder.txtName.setText(branchArrayList.get(position).getName());
        holder.txtID.setText(String.valueOf(branchArrayList.get(position).getBranchID()));
        holder.txtAddress.setText(branchArrayList.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainScreenActivity.class);
                intent.putExtra("category",branchArrayList.get(position).getCategoryArrayList());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return branchArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtID, txtAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageLogoBranch);
            txtName = itemView.findViewById(R.id.textViewBranchName);
            txtID = itemView.findViewById(R.id.textViewBranchID);
            txtAddress = itemView.findViewById(R.id.textViewAddress);
        }
    }
}
