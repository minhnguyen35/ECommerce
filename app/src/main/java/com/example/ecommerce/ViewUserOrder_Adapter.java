package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.ecommerce.R.color.colorStatus_finished;
import static com.example.ecommerce.R.color.colorStatus_processing;

public class ViewUserOrder_Adapter extends BaseAdapter {

    private Context context;
    private int layout_element;
    private ArrayList<User_Order> userOrderList;


    public ViewUserOrder_Adapter(Context context, int layout_element, ArrayList<User_Order> userOrderList) {
        this.context = context;
        this.layout_element = layout_element;
        this.userOrderList = userOrderList;
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

    private class userOrderViewHolder {
        private TextView id;
        private TextView superMarketName;
        private TextView date;
        private TextView checkOutType; /* 1 for COD, 2 for Bank */
        private TextView shipType; /* 1 for take over, 2 for delivery */
        private TextView total;
        private TextView status;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (userOrderList.size() <= 0) return view;

        ViewUserOrder_Adapter.userOrderViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout_element, null);

            holder = new userOrderViewHolder();
            holder.id = view.findViewById(R.id.supermarketID);
            holder.superMarketName = view.findViewById(R.id.supermarketName);
            holder.date = view.findViewById(R.id.orderDate);
            holder.checkOutType = view.findViewById(R.id.checkOutType); /* 1 for COD, 2 for Bank */
            holder.shipType = view.findViewById(R.id.shipType); /* 1 for take over, 2 for delivery */
            holder.total = view.findViewById(R.id.orderTotal);
            holder.status = view.findViewById(R.id.status);

            view.setTag(holder);
        }
        else {
            holder = (userOrderViewHolder) view.getTag();
        }


        User_Order userOrder = userOrderList.get(i);

        holder.id.setText(userOrder.getId());
        holder.superMarketName.setText(userOrder.getSuperMarketName());
        holder.date.setText(userOrder.getDate());

        if (userOrder.getCheckOutType() == 1) holder.checkOutType.setText("COD");
        else holder.checkOutType.setText("Bank");

        if (userOrder.getShipType() == 1) holder.shipType.setText("Take over");
        else holder.shipType.setText("Delivery");

        holder.total.setText(Long.toString(userOrder.getTotal()));

        if (userOrder.isStatus()) {
            holder.status.setText("Finished");
            holder.status.setBackgroundColor(colorStatus_finished);
        }
        else {
            holder.status.setText("Processing");
            holder.status.setBackgroundColor(colorStatus_processing);
        }
        return view;
    }
}
