package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.ecommerce.R.color.colorStatus_finished;
import static com.example.ecommerce.R.color.colorStatus_processing;

public class ViewUserOrderItem_Adapter extends BaseAdapter {

    private Context context;
    private int layout_element;
    private ArrayList<Order_Item> userOrderItemList;

    public ViewUserOrderItem_Adapter(Context context, int layout_element, ArrayList<Order_Item> userOrderItemList) {
        this.context = context;
        this.layout_element = layout_element;
        this.userOrderItemList = userOrderItemList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    private class userOrderItemViewHolder {
        private ImageView itemImage;
        private TextView itemID;
        private TextView itemName;
        private TextView itemQuantity;
        private TextView itemPrice;
        private TextView itemTotal;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (userOrderItemList.size() <= 0) return view;

        userOrderItemViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout_element, null);

            holder = new userOrderItemViewHolder();
            holder.itemImage = view.findViewById(R.id.itemLogo);
            holder.itemID = view.findViewById(R.id.iid);
            holder.itemName = view.findViewById(R.id.itemName);
            holder.itemQuantity = view.findViewById(R.id.iquantity);
            holder.itemPrice = view.findViewById(R.id.iprice);
            holder.itemTotal = view.findViewById(R.id.itotal);


            view.setTag(holder);
        }
        else {
            holder = (userOrderItemViewHolder) view.getTag();
        }


        Order_Item userOrderItem = userOrderItemList.get(i);


        Picasso.get().load(userOrderItem.getItemLogo()).fit().into(holder.itemImage);
        holder.itemID.setText(userOrderItem.getId());
        holder.itemName.setText(userOrderItem.getItemName());
        holder.itemQuantity.setText(Integer.toString(userOrderItem.getQuantityPurchase()));
        holder.itemPrice.setText(Long.toString(userOrderItem.getPrice()));
        holder.itemTotal.setText(Long.toString(userOrderItem.getTotal()));
        return view;
    }

}
