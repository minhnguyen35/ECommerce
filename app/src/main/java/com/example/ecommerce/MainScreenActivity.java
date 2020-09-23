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

public class MainScreenActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private CategoryAdapter adapter;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Integer> categoryIDList;
    private String branchID;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private final int REQUEST_CODE_CART = 10000, REQUEST_CODE_ACCOUNT = 20000, REQUEST_CODE_ORDERS = 30000;
    private final int REQUEST_CODE_ITEM = 789;
    private final int RESULT_LOGOUT = 88888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        catchIntent();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(botNavBarListener);

        createRecyclerView();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener botNavBarListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()){
                case R.id.userInfo:
                    intent = new Intent(MainScreenActivity.this, ViewUserInfo.class);
                    intent.putExtra("account",acc);
                    startActivity(intent);
                    return true;
                case R.id.inCart:
                    intent = new Intent(MainScreenActivity.this,PaymentActivity.class);
                    intent.putExtra("bundle",cart);
                    startActivityForResult(intent,REQUEST_CODE_CART);
                    return true;
                case R.id.order:
                    intent = new Intent(MainScreenActivity.this, ViewUserOrder.class);
                    intent.putExtra("account",acc);
                    startActivity(intent);
                    //Toast.makeText(MainScreenActivity.this,item.getTitle(),Toast.LENGTH_LONG).show();
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
        if((requestCode==REQUEST_CODE_CART || requestCode==REQUEST_CODE_ITEM ) && resultCode == RESULT_OK) {
            finish();
        }
        else if(resultCode==RESULT_LOGOUT)
            logout();
        super.onActivityResult(requestCode, resultCode, data);
    }

    void catchIntent(){
        Intent intent = getIntent();
        categoryIDList = intent.getIntegerArrayListExtra("category");
        branchID = intent.getStringExtra("branchID");
    }

    private void createRecyclerView(){
        recyclerView = findViewById(R.id.listCategory);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        categoryArrayList = new ArrayList<>();
        adapter=new CategoryAdapter(this,categoryArrayList, false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.addListenerForSingleValueEvent(newEvent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(newEvent);
    }

    private ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            categoryArrayList = new ArrayList<>();
            DataSnapshot categoryList = snapshot.child("Category");

            for(DataSnapshot cate: categoryList.getChildren())
            {
                String brID = cate.child("branchID").getValue().toString();
                if(branchID.equals(brID)) {
                    Category newCate;
                    String categoryID = cate.child("id").getValue().toString();
                    String name = cate.child("name").getValue().toString();

                    ArrayList<Item>  itemArrayList = new ArrayList<>();
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
                            itemArrayList.add(newItem);
                        }
                    }

                    newCate = new Category(categoryID, name,itemArrayList);
                    categoryArrayList.add(newCate);
                }
            }

            adapter.setCategoryArrayList(categoryArrayList);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}


