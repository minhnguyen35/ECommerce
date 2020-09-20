package com.example.ecommerce;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public interface FirebaseInsertBehaviour {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    void insert(final Item item);
}
