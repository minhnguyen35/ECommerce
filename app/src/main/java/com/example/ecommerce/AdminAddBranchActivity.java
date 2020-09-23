package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminAddBranchActivity extends AppCompatActivity {
    private Button save;
    private EditText Address, Latitude, Longitude;
    private String add;
    private Double lat;
    private Double lng;
    private String supermarketID;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener getSup;
    private Supermarket curSup;
    private String newBranchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_branch);
        catchIntent();
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSup = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curSup = snapshot.child("Supermarkets").child(supermarketID).getValue(Supermarket.class);
                newBranchID = snapshot.child("ID").child("BranchID").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(getSup);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(getSup);
    }

    private void catchIntent()
    {
        Intent intent = getIntent();
        supermarketID = intent.getStringExtra("supID");
    }
    private void initView() {
        save = findViewById(R.id.save_branch);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int branchID = Integer.parseInt(newBranchID) + 1;
                String newID = String.valueOf(branchID);
                db.child("ID").child("BranchID").setValue(newID);
                add = Address.getText().toString();
                lat = Double.valueOf(Latitude.getText().toString());
                lng = Double.valueOf(Longitude.getText().toString());
                List<Double> location = new ArrayList<>();
                location.add(lat);
                location.add(lng);
                final ProgressDialog loadingBar = new ProgressDialog(AdminAddBranchActivity.this);
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                HashMap<String, Object> tmp = new HashMap<>();
                tmp.put("address", add);
                tmp.put("branchID", newID);
                tmp.put("latLng", location);
                tmp.put("supermarketID", supermarketID);
                db.child("Branchs").child(newID).updateChildren(tmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingBar.dismiss();
                        finish();
                    }
                });

            }
        });
        Address = findViewById(R.id.branch_add);
        Latitude = findViewById(R.id.latitude);
        Longitude = findViewById(R.id.longitude);
    }
}