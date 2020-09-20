package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private boolean allowBack = false;
    static final int GALLERY_REQUEST_CODE_SCAN = 12;
    private EditText name, quantity, price, description;
    private Spinner branch, category;
    private Button addImage, addItem;
    private Bitmap imageBitmap;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener getData;
    private List<String> listBranch = new ArrayList<>();
    private List<String> listCategory = new ArrayList<>();
    private String supID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setUp();
        getImage();
        Intent intent = getIntent();
        supID = intent.getStringExtra("supID");
        //Toast.makeText(this,supID,Toast.LENGTH_LONG).show();

    }
    private void getImage()
    {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageFromGallery();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot curBranch: snapshot.child("Branchs").getChildren())
                {
                    String getSupID = curBranch.child("supermarketID").getValue().toString();
                    if(getSupID.equals(supID)) {
                        String curAdd = curBranch.child("address").getValue().toString();
                        listBranch.add(curAdd);
                        //Toast.makeText(AdminActivity.this,curBranch.child("address").getValue().toString(),Toast.LENGTH_LONG).show();
                    }

                }

//                String str[] = new String[listBranch.size()];
//
//                // ArrayList to Array Conversion
//                for (int j = 0; j < listBranch.size(); j++) {
//                    // Assign each value to String array
//                    str[j] = listBranch.get(j);
//                }
                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter<String> aa = new ArrayAdapter<String>(AdminActivity.this,android.R.layout.simple_spinner_item, listBranch);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                branch.setAdapter(aa);  ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(getData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(getData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE_SCAN)
        {
            if(resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    private void addImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick Image"), GALLERY_REQUEST_CODE_SCAN);
    }
    private void setUp() {
        name = findViewById(R.id.name_product);
        quantity = findViewById(R.id.quantity_product);
        price = findViewById(R.id.price_product);
        description = findViewById(R.id.description_product);
        branch = (Spinner)findViewById(R.id.branch_selected);
        category = findViewById(R.id.category_selected);
        addImage = findViewById(R.id.add_image);
        addItem = findViewById(R.id.add_item);

    }

    public void onBackPressed() {
        if (allowBack) {
            super.onBackPressed();
        } else {

        }
    }
}