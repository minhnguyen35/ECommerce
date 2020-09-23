package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUserOrder extends AppCompatActivity {

    private ArrayList<User_Order> listOrder;
    private Boolean added =false;
    private String userID;

    private GridView gridView;
    private ViewUserOrder_Adapter adapter;
    
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener getOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_order);


        getData();
        initView();
        //adaptView();
        clickOrderElement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //final ArrayList<User_Order> listOrder = new ArrayList<>();
        getOrder = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot orders = snapshot.child("Orders");
                listOrder.clear();
                for(DataSnapshot tmp: orders.getChildren())
                {
                    //get branch id
                    String account = (String) tmp.child("account").getValue();
                    if(userID.equals(account))
                    {
                        User_Order orderCur = tmp.getValue(User_Order.class);
                        listOrder.add(orderCur);
                    }
                }

                if (listOrder.size() != 0) {
                    //Toast.makeText(ViewUserOrder.this, "listOrder size = " + listOrder.size(), Toast.LENGTH_LONG).show();
                    /*Toast.makeText(ViewUserOrder.this, listOrder.get(0).getId() +
                            "    " + listOrder.get(0).getSuperMarketName() +
                            "   " + listOrder.get(0).getTotal() +
                            "   " + listOrder.get(0).getShipType() +
                            "   " + listOrder.get(0).getCheckOutType() +
                            "   " + listOrder.get(0).isStatus(), Toast.LENGTH_LONG).show();
                    Toast.makeText(ViewUserOrder.this, listOrder.get(1).getId() +
                            "    " + listOrder.get(1).getSuperMarketName() +
                            "   " + listOrder.get(1).getTotal() +
                            "   " + listOrder.get(1).getShipType() +
                            "   " + listOrder.get(1).getCheckOutType() +
                            "   " + listOrder.get(1).isStatus(), Toast.LENGTH_LONG).show();*/
                    if (added == false ) {
                        adaptView();
                        added = true;
                    }
                    else {
                        loadOldAdaptView();
                    }
                    //Toast.makeText(ViewUserOrder.this, "Adapted data", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(ViewUserOrder.this, "listOrder null", Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, String.valueOf(listBranch.size()), Toast.LENGTH_LONG).show();
                  /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(getOrder);
    }

    private void loadOldAdaptView() {
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(getOrder);
    }

    private void getData() {
        Intent getAccount = getIntent();
        userID = getAccount.getStringExtra("account");
        //Toast.makeText(ViewUserOrder.this, "userID = " + userID , Toast.LENGTH_LONG).show();
    }

    private void clickOrderElement() {
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent viewOrder = new Intent(ViewUserOrder.this, ViewUserOrderItem.class);
                viewOrder.putExtra("orderID", listOrder .get(i).getId());
                viewOrder.putExtra("orderStatus", listOrder .get(i).isStatus());
                viewOrder.putExtra("orderTotal", listOrder .get(i).getTotal());
                startActivity(viewOrder);
            }
        });
    }

    private void adaptView() {
        //Toast.makeText(ViewUserOrder.this, "listOrder size = " + listOrder.size(), Toast.LENGTH_LONG).show();
        adapter = new ViewUserOrder_Adapter(ViewUserOrder.this, R.layout.order_element, listOrder);
        //adapter = new ViewUserOrder_Adapter(this, R.layout.order_element, userOrderList);
        gridView.setAdapter(adapter);
        //Toast.makeText(ViewUserOrder.this, "Adapted data", Toast.LENGTH_LONG).show();
    }

    private void initView() {
        listOrder = new ArrayList<User_Order>();
        //userOrderList = new ArrayList<User_Order>();
        gridView = (GridView) findViewById(R.id.orderList);

        //User_Order userOrder = new User_Order("3", "VinMart", "22/09", 1, 1, 50000, false, "anotheradmin");
        //userOrderList.add(userOrder);
    }

}