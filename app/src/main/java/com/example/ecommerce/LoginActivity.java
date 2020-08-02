package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText pass, user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setView();
        checkLogin();
    }

    private void checkLogin() {
        final DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference();



        Button login = findViewById(R.id.button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView();
                final String u = user.getText().toString();
                final String p = pass.getText().toString();
                if(checkValid(u, p))
                    checkDb(u, p, db);
            }
        });

    }
    private void notification(CharSequence x)
    {
        Toast toast = Toast.makeText(this, x, Toast.LENGTH_SHORT);
        toast.show();
    }
    private boolean checkValid(String u, String p) {
        if(u.isEmpty())
        {
            CharSequence x = "Username is empty!";
            notification(x);
            return false;
        }
        if(p.isEmpty())
        {
            CharSequence x = "Password is empty";
            notification(x);
            return false;
        }
        return true;
    }

    private void checkDb(final String user, final String pass, DatabaseReference db)
    {

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(user).exists())
                {
                    String user_db = snapshot.child("Users").child(user).child("password").getValue().toString();
//                    CharSequence x = user_db;
//                    notification(x);
                    if(user_db.equals(pass))
                    {
                        CharSequence x = "Login Successfully!";
                        notification(x);
//                        Toast toast = Toast.makeText(LoginActivity.this, x, Toast.LENGTH_SHORT);
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
                    }
                    else{
                        CharSequence x = "Login Failed! Please Check Password Again!";
//                        Toast toast = Toast.makeText(LoginActivity.this, x, Toast.LENGTH_SHORT);
                        notification(x);
                    }
                }
                else{
                    CharSequence x = "Login Failed! Please Check Username Again!";
                    Toast toast = Toast.makeText(LoginActivity.this, x, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setView() {
        user = (EditText)findViewById(R.id.input_user);
        pass = (EditText)findViewById(R.id.input_pass);
    }
}