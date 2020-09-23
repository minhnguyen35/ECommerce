package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.ecommerce.R.color.colorStatus_finished;
import static com.example.ecommerce.R.color.colorStatus_processing;
import static com.example.ecommerce.R.color.colorTrans;
import static com.example.ecommerce.R.color.colorWord;

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
        return userOrderList.size();
    }

    @Override
    public Object getItem(int i) {
        return userOrderList.get(i);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d("AAA", "ADAPTER CAME HERE");
        if (userOrderList.size() <= 0) {
            Toast.makeText(context, "adapt fail: userOrderList null", Toast.LENGTH_SHORT).show();
            return view;
        }

        ViewUserOrder_Adapter.userOrderViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout_element, null);

            holder = new userOrderViewHolder();
            holder.id = view.findViewById(R.id.supermarketID);
            holder.superMarketName = view.findViewById(R.id.supermarketName);
            holder.date = view.findViewById(R.id.orderDate);
            holder.checkOutType = view.findViewById(R.id.checkOutType);
            holder.shipType = view.findViewById(R.id.shipType);
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
            holder.status.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), colorStatus_finished));
            holder.status.setTextColor(ContextCompat.getColor(context.getApplicationContext(), colorStatus_processing));
        }
        else {
            holder.status.setText("Processing");
            holder.status.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), colorStatus_processing));
            holder.status.setTextColor(ContextCompat.getColor(context.getApplicationContext(), colorWord));
        }

        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         view = inflater.inflate(layout_element, null);


        TextView id = view.findViewById(R.id.supermarketID);
        TextView superMarketName = view.findViewById(R.id.supermarketName);
        TextView date = view.findViewById(R.id.orderDate);
        TextView checkOutType = view.findViewById(R.id.checkOutType);
        TextView shipType = view.findViewById(R.id.shipType);
        TextView total = view.findViewById(R.id.orderTotal);
        TextView status = view.findViewById(R.id.status);

        User_Order userOrder = userOrderList.get(i);

        id.setText(userOrder.getId());
        superMarketName.setText(userOrder.getSuperMarketName());
        date.setText(userOrder.getDate());

        if (userOrder.getCheckOutType() == 1) checkOutType.setText("COD");
        else checkOutType.setText("Bank");

        if (userOrder.getShipType() == 1) shipType.setText("Take over");
        else shipType.setText("Delivery");

        total.setText(Long.toString(userOrder.getTotal()));

        if (userOrder.isStatus()) {
            status.setText("Finished");
            status.setBackgroundColor(colorStatus_finished);
        }
        else {
            status.setText("Processing");
            status.setBackgroundColor(colorStatus_processing);
        }*/

        return view;
    }
}
