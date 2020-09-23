package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUserOrderItem extends AppCompatActivity {

    private ArrayList<Order_Item> userOrderItemList;
    private String OrderID;
    private boolean status;
    private long orderTotal;

    private GridView gridView;
    private ViewUserOrderItem_Adapter adapter;
    private TextView oTotal;
    private Button BtnConfirm;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener getOrderItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_order_item);

        getData();
        initBtn();
        //adaptView();
        clickConfirm();
    }


    /*TODO:
    *  get list of order item from database by orderID*/

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        getOrderItem = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot orders = snapshot.child("OrderItem");
                userOrderItemList.clear();
                for(DataSnapshot tmp: orders.getChildren())
                {

                    String orderID = (String) tmp.child("orderID").getValue();
                    if(OrderID.equals(orderID))
                    {
                        String itemID = (String) tmp.child("itemID").getValue();
                        String itemName = tmp.child("itemName").getValue().toString();
                        String itemLogo = tmp.child("itemLogo").getValue().toString();
                        String orderItemID = tmp.child("orderItemID").getValue().toString();
                        long price = (long) tmp.child("price").getValue();
                        long total = (long)tmp.child("total").getValue();
                        long quantityPurchase = (long)tmp.child("quantityPurchase").getValue();
                        int quantP = (int) quantityPurchase;
                        Order_Item orderItemCur = new Order_Item(itemLogo, orderID, itemID, itemName, 100, quantP, price, total );//dien cai nay di Nhan

                        userOrderItemList.add(orderItemCur);
                    }
                }
                //cai anh dau mat tieu r
                if (userOrderItemList.size() != 0) adaptView();
                else Toast.makeText(ViewUserOrderItem.this, "listOrderItem null", Toast.LENGTH_LONG).show();

                //Toast.makeText(MainActivity.this, String.valueOf(listBranch.size()), Toast.LENGTH_LONG).show();
                  /*
                    Do something with the data

                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(getOrderItem);

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(getOrderItem);
    }

    private void clickConfirm() {
        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == false) {
                    db.child("Orders").child(OrderID).child("status").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ViewUserOrderItem.this, "Success Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                    /*TODO:
                    *  update status in userOrder to TRUE in database*/
                    finish();
                }
                else Toast.makeText(ViewUserOrderItem.this, "You confirmed this order !", Toast.LENGTH_SHORT).show();
            }
        });
    }
  //tu`
    private void adaptView() {
        oTotal.setText(Long.toString(orderTotal));
        //Toast.makeText(ViewUserOrderItem.this, "ItemID = " + userOrderItemList.get(0).getId() + "  logo = " + userOrderItemList.get(0).getItemLogo(), Toast.LENGTH_LONG).show();
        adapter = new ViewUserOrderItem_Adapter(ViewUserOrderItem.this, R.layout.order_item_element, userOrderItemList);
        gridView.setAdapter(adapter);
    }

    private void initBtn() {
        userOrderItemList = new ArrayList<>();
        gridView = findViewById(R.id.orderItemList);
        oTotal = findViewById(R.id.ototal);
        BtnConfirm = findViewById(R.id.btn_confirm_finish);
    }

    private void getData() {
        Intent intent = getIntent();
        OrderID = intent.getStringExtra("orderID");
        status = intent.getBooleanExtra("orderStatus", false);
        orderTotal = intent.getLongExtra("orderTotal", 0);
    }
}