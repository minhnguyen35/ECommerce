package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText pass, user;
    private Button register;
    private boolean remember = false;
    private static String remember_user, remember_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //checkRemember();
        setView();
        checkLogin();
        registerClick();

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
                boolean isValid = false;
                if(checkValid(u, p))
                    checkDb(u, p, db, remember);
            }
        });

    }
    private void registerClick()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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

    private void checkDb(final String user, final String pass, DatabaseReference db, final boolean isChecked)
    {
        //final boolean[] isValid = {false};
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(user).exists())
                {
                    String user_db = snapshot.child("Users").child(user).child("password").getValue().toString();

                    if(user_db.equals(pass))
                    {
//                        CharSequence x = "Login Successfully!";
//                        notification(x);
                        if(isChecked) {
                            SharedPreferences sharedPref = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("remember", "true");
                            editor.apply();
                        }
                        CharSequence x = "Login Successfully!";
                        Toast toast = Toast.makeText(LoginActivity.this, x, Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(LoginActivity.this, Dummy.class);
                        startActivity(intent);
                        //isValid[0] = true;
                    }
                    else{
                        CharSequence x = "Login Failed! Please Check Password Again!";
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
        //return  isValid[0];
    }
    private void setView() {
        user = (EditText)findViewById(R.id.input_user);
        pass = (EditText)findViewById(R.id.input_pass);
        register = findViewById(R.id.register_login);
    }

    public void rememberLogin(View view) {
        boolean checkRemember = ((CheckBox) view).isChecked();
        if(view.getId() == R.id.remember)
        {
            if(checkRemember)
            {
                remember = true;

            }

        }
    }
}