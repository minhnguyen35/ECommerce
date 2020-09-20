package com.example.ecommerce;


import android.content.Context;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ecommerce.FirebaseInsertBehaviour;
import com.example.ecommerce.Item;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class InsertItem implements FirebaseInsertBehaviour {
    private Context context;
    public InsertItem( Context context) {

        this.context = context;
    }

    public void onUpdateItem(final Item itemEdited) {
        DatabaseReference itemRef = db.child("Items").child(itemEdited.getID());

        itemRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Item p = mutableData.getValue(Item.class);
                // Set value and report transaction success
                Toast.makeText(context,p.getDescription(), Toast.LENGTH_LONG).show();
                mutableData.setValue(itemEdited);
                Toast.makeText(context,"Success edited", Toast.LENGTH_LONG).show();
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {

                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    public void insert(final Item item)
    {

        final String idItem = item.getID();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(isExist(snapshot, idItem) == false)
                {
                    db.child("Items").child(idItem).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                CharSequence x = "Insert Item Successfully!";
                                Toast toast = Toast.makeText(context, x, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean isExist(DataSnapshot snapshot, String IDitem)
    {

        if(snapshot.child("Items").child(IDitem).exists())
        {
            CharSequence y = snapshot.child("Items").child(IDitem).toString();
            CharSequence x = "Item already exists!";
            notify(x + " " + y);
            return true;
        }
        return false;
    }

    private void notify(CharSequence s) {
        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        toast.show();
    }
}
