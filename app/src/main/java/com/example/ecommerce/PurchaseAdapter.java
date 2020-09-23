package com.example.ecommerce;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Order_Item> orderItemArrayList;
    private boolean checkItemOk = false;

    public PurchaseAdapter(Context context, ArrayList<Order_Item> userOrderArrayList, boolean checkItemOk) {
        this.context = context;
        this.orderItemArrayList = userOrderArrayList;
        this.checkItemOk = checkItemOk;
    }

    public void setCheckItemOk(boolean checkItemOk) {
        this.checkItemOk = checkItemOk;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.purchase_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String logo = orderItemArrayList.get(position).getItemLogo();
        if(logo!=null && logo!="")
            Picasso.get()
                    .load(Uri.parse(logo))
                    .into(holder.imageView);
        else holder.imageView.setImageResource(R.drawable.noimage);

        holder.name.setText(orderItemArrayList.get(position).getItemName());
        holder.price.setText(String.valueOf(orderItemArrayList.get(position).getPrice())+" VND");
        holder.quantity.setText(String.valueOf(orderItemArrayList.get(position).getQuantityPurchase()));
        holder.total.setText(String.valueOf(orderItemArrayList.get(position).getTotal()));

        holder.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkItemOk) {
                    int number = orderItemArrayList.get(position).getQuantityPurchase() + 1;
                    if (number <= orderItemArrayList.get(position).getQuantity()) {
                        orderItemArrayList.get(position).setQuantityPurchase(number);
                        holder.quantity.setText(String.valueOf(number));

                        orderItemArrayList.get(position).setTotal(
                                orderItemArrayList.get(position).getTotal() +
                                        orderItemArrayList.get(position).getPrice()
                        );
                        holder.total.setText(String.valueOf(orderItemArrayList.get(position).getTotal()));
                    }
                }
            }
        });

        holder.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkItemOk) {
                    int number = orderItemArrayList.get(position).getQuantityPurchase() - 1;
                    if (number >= 0) {
                        orderItemArrayList.get(position).setQuantityPurchase(number);
                        holder.quantity.setText(String.valueOf(number));

                        orderItemArrayList.get(position).setTotal(
                                orderItemArrayList.get(position).getTotal() -
                                        orderItemArrayList.get(position).getPrice()
                        );
                        holder.total.setText(String.valueOf(orderItemArrayList.get(position).getTotal()));
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price, quantity, total;
        Button inc, dec;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.orderItemImage);
            name = itemView.findViewById(R.id.orderItemName);
            price = itemView.findViewById(R.id.orderItemPrice);
            quantity = itemView.findViewById(R.id.orderItemQuantity);
            total = itemView.findViewById(R.id.orderItemTotalPrice);
            inc = itemView.findViewById(R.id.orderMoreButton);
            dec = itemView.findViewById(R.id.orderLessButton);
        }
    }
}
