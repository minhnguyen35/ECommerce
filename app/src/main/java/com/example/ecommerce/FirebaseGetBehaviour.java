package com.example.ecommerce;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public interface FirebaseGetBehaviour {
    //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    void getData(final String id);
    void getList();
}
