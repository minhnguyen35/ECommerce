package com.example.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.example.ecommerce.BranchMenuActivity.cart;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Branch> branchArrayList;
    private boolean isAdminSite;

    private final int REQUEST_CODE_CATEGORY = 456;

    public BranchAdapter(Context context, ArrayList<Branch> branchArrayList, boolean isAdminSite) {
        this.context = context;
        this.branchArrayList = branchArrayList;
        this.isAdminSite = isAdminSite;
    }

    public void setBranchArrayList(ArrayList<Branch> branchArrayList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(branchArrayList.get(position).getLogo()!="")
            Picasso.get().load(Uri.parse(branchArrayList.get(position).getLogo())).into(holder.imageView);
        holder.txtName.setText(branchArrayList.get(position).getName());
        holder.txtAddress.setText(branchArrayList.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(!isAdminSite){
                    intent = new Intent(context, MainScreenActivity.class);
                    cart.putSerializable("branch",branchArrayList.get(position));
                }
                else
                    intent = new Intent(context, AdminCategoryActivity.class);

                intent.putExtra("category", branchArrayList.get(position).getCategoryArrayList());
                intent.putExtra("branchID", branchArrayList.get(position).getBranchID());
                ((Activity)context).startActivityForResult(intent, REQUEST_CODE_CATEGORY);
            }
        });

        if(!isAdminSite) {
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("branch", branchArrayList);
                    intent.putExtra("from", branchArrayList.get(position).getLatLng());
                    context.startActivity(intent);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return branchArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName;
        TextView txtAddress;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageLogoBranch);
            txtName = itemView.findViewById(R.id.textViewBranchName);
            txtAddress = itemView.findViewById(R.id.textViewAddress);
            imageButton = itemView.findViewById(R.id.buttonDirect);
        }
    }
}
