package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.ecommerce.BranchMenuActivity.cart;
import static com.example.ecommerce.MainMenuActivity.acc;

public class ItemInfoActivity extends AppCompatActivity {
    Item item;
    ArrayList<Item> suggestionItemList;
    RecyclerView recyclerViewArrayImage, recyclerViewArraySuggestion;
    TextView textViewName, textViewPrice, textViewQuantity,textViewID,textViewCateID, textViewDescr;
    Button btnAdd;

    ItemAdapter suggestionAdapter;
    ImageAdapter imageAdapter;

    private String categoryID;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private int REQUEST_CODE_CART = 10000, REQUEST_CODE_ACCOUNT = 20000, REQUEST_CODE_ORDERS = 30000;
    private int REQUEST_CODE_ITEM = 789;
    private int RESULT_LOGOUT = 88888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(botNavBarListener);

        mapping();
        catchIntent();
        init();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener botNavBarListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.userInfo:
                            intent = new Intent(ItemInfoActivity.this, ViewUserInfo.class);
                            intent.putExtra("account",acc);
                            startActivity(intent);
                            return true;
                        case R.id.inCart:
                            intent = new Intent(ItemInfoActivity.this,PaymentActivity.class);
                            intent.putExtra("bundle",cart);
                            startActivityForResult(intent,REQUEST_CODE_CART);
                            return true;
                        case R.id.order:
                            intent = new Intent(ItemInfoActivity.this, ViewUserOrder.class);
                            intent.putExtra("account",acc);
                            startActivity(intent);
                            return true;
                        case R.id.logout:
                            logout();
                            return true;
                    }
                    return false;
                }
            };

    private void logout() {
        SharedPreferences sharedPref = getSharedPreferences("checkbox", Context.MODE_PRIVATE); // cai nay la de bo cai ghi nho dang nhap
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("remember", "false");
        editor.apply();
        setResult(RESULT_LOGOUT);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if((requestCode==REQUEST_CODE_CART || requestCode==REQUEST_CODE_ITEM) && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        else if(resultCode== RESULT_LOGOUT)
            logout();

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.addValueEventListener(newEvent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(newEvent);
    }

    ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            suggestionItemList = new ArrayList<>();
            DataSnapshot itemList = snapshot.child("Items");
            for(DataSnapshot it: itemList.getChildren())
            {
                String catID = it.child("categoryID").getValue().toString();
                if(categoryID.equals(catID)) {
                    String ID = it.child("id").getValue().toString();
                    String Name= it.child("name").getValue().toString();
                    long Price = it.child("price").getValue(Long.class);
                    ArrayList<String> imageUrl = new ArrayList<>();
                    for(DataSnapshot url : it.child("imageArrayList").getChildren())
                    {
                        String curUrl = url.getValue().toString();
                        imageUrl.add(curUrl);
                    }
                    int Quantity = it.child("quantity").getValue(Long.class).intValue();
                    String Description= (String) it.child("description").getValue();
                    Item newItem = new Item(ID, catID, Name, Price, imageUrl, Quantity, Description);
                    if(!ID.equals(item.getID()))
                        suggestionItemList.add(newItem);
                    else{
                        item = newItem;
                        imageAdapter.setImageArray(item.getImageArrayList());
                        updateItemInfo();
                    }
                }
            }

            suggestionAdapter.setItemArrayList(suggestionItemList);
            suggestionAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void catchIntent() {
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        categoryID = item.getCategoryID();


    }

    private void mapping(){
        recyclerViewArrayImage=findViewById(R.id.recyclerViewArrayImage);
        recyclerViewArraySuggestion=findViewById(R.id.recyclerViewArraySuggestion);
        textViewName=findViewById(R.id.textViewItemName);
        textViewID=findViewById(R.id.textViewItemID);
        textViewCateID=findViewById(R.id.textViewCategoryID);
        textViewPrice=findViewById(R.id.textViewItemPrice);
        textViewQuantity=findViewById(R.id.textViewItemQuantity);
        textViewDescr=findViewById(R.id.textViewItemDescription);
        btnAdd=findViewById(R.id.buttonAddToCart);
    }

    private void init(){
        recyclerViewArrayImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewArrayImage.setHasFixedSize(true);
        imageAdapter = new ImageAdapter(this,item.getImageArrayList());
        recyclerViewArrayImage.setAdapter(imageAdapter);
        //imageAdapter.notifyDataSetChanged();

        recyclerViewArraySuggestion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewArraySuggestion.setHasFixedSize(true);
        suggestionItemList = new ArrayList<>();
        suggestionAdapter = new ItemAdapter(this, suggestionItemList, false);
        recyclerViewArraySuggestion.setAdapter(suggestionAdapter);
        //suggestionAdapter.notifyDataSetChanged();

        updateItemInfo();
        btnAdd.setOnClickListener(add);
    }

    private void updateItemInfo(){
        textViewName.setText(item.getName());
        textViewID.setText(item.getID());
        textViewCateID.setText(item.getCategoryID());
        textViewPrice.setText(String.valueOf(item.getPrice())+"VND");
        textViewQuantity.setText(String.valueOf(item.getQuantity()));
        textViewDescr.setText(item.getDescription());
        imageAdapter.notifyDataSetChanged();
    }

    Button.OnClickListener add = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(ItemInfoActivity.this,"Add to Cart",Toast.LENGTH_LONG).show();
            String imageUri="";
            if (item.getImageArrayList()!=null && item.getImageArrayList().size()>0)
                imageUri=item.getImageArrayList().get(0);
            ArrayList<Order_Item> orderItemArrayList = (ArrayList<Order_Item>) cart.getSerializable("orderItems");
            for(int i=0; i<orderItemArrayList.size();++i) {
                Order_Item temp = orderItemArrayList.get(i);
                if (temp.getId().equals(item.getID())) {
                    temp.setQuantityPurchase(temp.getQuantityPurchase() + 1);
                    temp.setTotal(temp.getTotal()+item.getPrice());
                    cart.putSerializable("orderItems",orderItemArrayList);
                    return;
                }
            }

            Order_Item orderItem = new Order_Item(imageUri,null,item.getID(),item.getName(),item.getQuantity(),1,item.getPrice(),item.getPrice());
            orderItemArrayList.add(orderItem);
            cart.putSerializable("orderItems",orderItemArrayList);
        }
    };




}