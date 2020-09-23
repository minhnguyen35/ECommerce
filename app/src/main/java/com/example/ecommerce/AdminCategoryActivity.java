package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private CategoryAdapter adapter;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Integer> categoryIDList;
    private String branchID;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private Button addItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        addNewItem();
        catchIntent();
        createRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addNewItem()
    {
        addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddItemActivity.class);
                intent.putExtra("branchID", branchID);
                startActivity(intent);
            }
        });
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
        adapter=new CategoryAdapter(this,categoryArrayList,true);
        recyclerView.setAdapter(adapter);

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