package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup payGroup, receiveGroup;
    private Button confirmButton;
    private TextView textViewSubtotal, textViewShipFee, textViewTotal;
    private TextView textViewSupermarket, textViewAddress;
    private RecyclerView recyclerView;
    private PurchaseAdapter adapter;

    private Branch branch;
    private ArrayList<Order_Item> orderItemArrayList;
    private long subtotal=0, shipFee=0, total=0;
    private int COD=1, take=1;
    private boolean itemCheckOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        catchIntent();
        mapping();
        createListener();
        createBranchInfo();
        createRecyclerView();
        initPrice();
    }

    private void initPrice() {
        subtotal=0;
        total=0;
    }

    private void createListener() {
        payGroup.setOnCheckedChangeListener(payHelper);
        receiveGroup.setOnCheckedChangeListener(receiveHelper);
        confirmButton.setOnClickListener(confirmHelper);
    }

    private void catchIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        orderItemArrayList = (ArrayList<Order_Item>) bundle.getSerializable("orderItems");
        branch = (Branch) bundle.getSerializable("branch");
    }

    private void mapping(){
        payGroup = findViewById(R.id.radioGroupPay);
        receiveGroup = findViewById(R.id.radioGroupReceive);
        confirmButton = findViewById(R.id.buttonConfirm);
        textViewSubtotal = findViewById(R.id.textViewSubtotal);
        textViewShipFee = findViewById(R.id.textViewShipFee);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewSupermarket = findViewById(R.id.textViewOrderSupName);
        textViewAddress = findViewById(R.id.textViewOrderBranchAddress);
        recyclerView = findViewById(R.id.recyclerViewOrderItems);
    }

    private void createBranchInfo(){
        textViewSupermarket.setText(branch.getName());
        textViewAddress.setText(branch.getAddress());
    }

    private void createRecyclerView(){
        adapter = new PurchaseAdapter(this,orderItemArrayList,itemCheckOk);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private RadioGroup.OnCheckedChangeListener payHelper = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i==R.id.radioButtonBank)
                COD=2;
            else if(i==R.id.radioButtonCOD)
                COD=1;
        }
    };

    private RadioGroup.OnCheckedChangeListener receiveHelper = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i==R.id.radioButtonShip) {
                shipFee = 20000;
                take = 2;
            }
            else if(i==R.id.radioButtonTake) {
                shipFee = 0;
                take = 1;
            }
            setShipFee();
            calculateTotal();
        }
    };

    private Button.OnClickListener confirmHelper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!itemCheckOk) {
                calculateSubtotal();
                calculateTotal();
                if(total!=shipFee) {
                    itemCheckOk = true;
                    adapter.setCheckItemOk(itemCheckOk);

                    RelativeLayout layout = findViewById(R.id.relativeLayoutOrderMoney);
                    layout.setAlpha(1);
                    confirmButton.setText("CONFIRM ORDER");
                }
                else
                    Toast.makeText(PaymentActivity.this,"No Items",Toast.LENGTH_LONG).show();
                return;
            }


//            if(!updateItem(orderItemArrayList)) {
//                getOrderItem(orderItemArrayList);
//                return;
//            }

//            Date calendar = Calendar.getInstance().getTime();
//            String date=calendar.toString();
//
//            String name = branch.getName()+" "+branch.getAddress();
//            String id = branch.getSupermarketID()+branch.getBranchID()+date;
//            User_Order userOrder = new User_Order(id,name,date,COD,take,total,false,orderItemArrayList);
//
//            /*
//            todo: thay đổi data trên firebase ->>>> còn bug vui lòng ko ai dụng vào thay đổi trên activity này
//             */
//
            Toast.makeText(PaymentActivity.this, "Confirmed",Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);
            finish();
        }
    };


    private void calculateSubtotal(){
        for(int i=0; i<orderItemArrayList.size();++i){
            subtotal+=orderItemArrayList.get(i).getTotal();
        }
        textViewSubtotal.setText(String.valueOf(subtotal));
    }

    private void setShipFee(){
        textViewShipFee.setText(String.valueOf(shipFee));
    }

    private void calculateTotal(){
        total=subtotal+shipFee;
        textViewTotal.setText(String.valueOf(total));
    }



    public boolean updateItem(final ArrayList<Order_Item> orderItems)
    {
        final boolean[] ret = {true};
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference itemObject = db.child("Items");
        if(itemObject != null)
        {
            itemObject.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                    boolean isAbort = false;
                    ArrayList<Integer> listQuantityInDB = new ArrayList<>();
                    for(int i = 0; i < orderItems.size(); i++)
                    {
                        Order_Item newItem = orderItems.get(i);

                        Log.d("AAA","getItem");
                        int quantityOrd = newItem.getQuantityPurchase();
                        String id = newItem.getId();
                        Log.d("AAA",id);

                        int quantityReal=0;
                        quantityReal = (int) currentData.child(id).child("quantity").getValue(Long.class).intValue();

                        Log.d("AAA","get quantity");
                        listQuantityInDB.add(quantityReal);
                        if(quantityOrd > quantityReal)
                        {
//                            Toast.makeText(PaymentActivity.this, "Quantity is not enough for the item " +
//                                    newItem.getItemName(), Toast.LENGTH_LONG).show();
                            Log.d("AAA",String.valueOf(quantityReal) + " lack quantity " + String.valueOf(quantityOrd));

                            return Transaction.abort();
                        }

                        Log.d("AAA","get price");

                        long priceOrd = newItem.getPrice();
                        long priceReal = (long) currentData.child(id).child("price").getValue();
                        if(priceOrd != priceReal)
                        {
                            Toast.makeText(PaymentActivity.this, "Sorry there's something wrong with Item "
                                    + newItem.getItemName(), Toast.LENGTH_LONG).show();
                            Log.d("AAA","different price");

                            return Transaction.abort();
                        }
                    }
                    Log.d("AAA","b4 update");
                    for(int i = 0; i < orderItems.size();i++)
                    {
                        String id = orderItems.get(i).getId();
                        int quantityOrd = orderItems.get(i).getQuantityPurchase();
                        int quantityReal = listQuantityInDB.get(i);
                        HashMap<String, Object> itemAfter = new HashMap<>();
                        itemAfter.put("quantity", quantityReal - quantityOrd);
                        //db.child("Items").child().updateChildren(itemAfter);
                    }
                    Log.d("AAA","update");

                    ret[0]=false;
                    Toast.makeText(PaymentActivity.this, "Success Transaction", Toast.LENGTH_SHORT).show();
                    return  Transaction.success(currentData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                }
            });
        }
        return ret[0];
    }

    public void getOrderItem(final ArrayList<Order_Item> items)
    {
        final ArrayList<Order_Item> listOrderItem = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Items");
                for(int i = 0; i <items.size(); i++) {
                    String id = items.get(i).getId();

                    if (itemList.child(id).exists()) {
                        String ID = itemList.child(id).getValue().toString();
                        String CategoryID = itemList.child(id).child("categoryID").getValue().toString();

                        String Name= itemList.child(id).child("name").getValue().toString();
                        long Price = itemList.child(id).child("price").getValue(Long.class);

                        int Quantity = itemList.child(id).child("quantity").getValue(Long.class).intValue();
                        String Description= (String) itemList.child(id).child("description").getValue();
                        String tmp = "";
                        if(itemList.child(id).child("imageArrayList").child("0").getValue()!=null)
                            tmp=itemList.child(id).child("imageArrayList").child("0").getValue().toString();

                        int purchaseQuantity = items.get(i).getQuantityPurchase();
                        if(purchaseQuantity>Quantity)
                            purchaseQuantity=Quantity;
                        Order_Item addItem= new Order_Item(tmp, null,ID, Name, Quantity, purchaseQuantity,Price,
                                Price*purchaseQuantity);
                        listOrderItem.add(addItem);

                    } else {
//                        CharSequence s = "Item out of stock";
//                        Toast toast = Toast.makeText(PaymentActivity.this, s, Toast.LENGTH_SHORT);
//                        toast.show();
                    }
                }
                orderItemArrayList = listOrderItem;
                reload();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void reload() {
        itemCheckOk=false;
        createRecyclerView();
        RelativeLayout layout = findViewById(R.id.relativeLayoutOrderMoney);
        layout.setAlpha(0);
        confirmButton.setText("CONFIRM ITEMS");
        initPrice();
    }

}