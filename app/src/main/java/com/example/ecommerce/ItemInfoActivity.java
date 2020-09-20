package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.ecommerce.MainScreenActivity.categoryArrayList;

public class ItemInfoActivity extends AppCompatActivity {
    Item item;
    Category category;
    RecyclerView recyclerViewArrayImage, recyclerViewArraySuggestion;
    TextView textViewName, textViewPrice, textViewQuantity,textViewID,textViewCateID, textViewDescr;
    Button btnAdd;

    ItemAdapter suggestionAdapter;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        mapping();
        catchIntent();
        init();
    }

    private void catchIntent() {
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        category = categoryArrayList.get((Integer.valueOf(item.getCategoryID()) - 1));
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
        imageAdapter = new ImageAdapter(this,item.getImageArray());
        recyclerViewArrayImage.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

        recyclerViewArraySuggestion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewArraySuggestion.setHasFixedSize(true);
        //suggestionAdapter = new ItemAdapter(this, category.getItemArrayList());
        recyclerViewArraySuggestion.setAdapter(suggestionAdapter);
        suggestionAdapter.notifyDataSetChanged();


        textViewName.setText(item.getName());
        textViewID.setText(item.getID());
        textViewCateID.setText(String.valueOf(item.getCategoryID()));
        textViewPrice.setText(String.valueOf(item.getPrice()));
        textViewQuantity.setText(String.valueOf(item.getQuantity()));
        textViewDescr.setText(item.getDescription());

        btnAdd.setOnClickListener(add);
    }

    Button.OnClickListener add = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(ItemInfoActivity.this,"Add to Cart",Toast.LENGTH_LONG).show();
        }
    };


}