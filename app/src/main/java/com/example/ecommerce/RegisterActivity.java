package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Button login_reg;
    private Button join;
    private EditText username, password, passwordAgain, phone;

    private ProgressBar create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUp();
    }

    private void setUp() {
        login_reg = (Button)findViewById(R.id.login_in_reg);
        login_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        username = (EditText)findViewById(R.id.user_reg);
        password = (EditText)findViewById(R.id.pass_reg);
        phone = (EditText)findViewById(R.id.user_number);
        passwordAgain = findViewById(R.id.pass_again);
        join = (Button)findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInform();
            }
        });
    }
    private void notify(CharSequence x)
    {
        Toast toast = Toast.makeText(this, x, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void checkInform()
    {
        final String u = username.getText().toString();
        final String p = password.getText().toString();
        final String pnum = phone.getText().toString();
        final String passAgain = passwordAgain.getText().toString();
        final String mail = "default@gmail.com";
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(System.currentTimeMillis());
        final String date = DateFormat.format("HH:mm dd-MM-yyyy", cal).toString();
        if(u.isEmpty())
        {
            CharSequence x = "Empty Username!";
            notify(x);
        }
        else if(p.isEmpty())
        {
            CharSequence x = "Empty Password";
            notify(x);
        }
        else if(pnum.isEmpty())
        {
            CharSequence x = "Empty Phone Number";
            notify(x);
        }
        else if(!p.equals(passAgain))
        {
            CharSequence x = "Password are not matched";
            notify(x);
        }
        else
        {
            final DatabaseReference db;
            //long ts = System.currentTimeMillis();
//            ArrayList<String> orderID = new ArrayList<>();
//            orderID.add("123345");
//            orderID.add("123389");
            String bankNumber = " ";
            String address = " ";
            String userImage = "https://firebasestorage.googleapis.com/v0/b/ecommerce-c3c8f.appspot.com/o/avt.png?alt=media&token=92057ded-2f01-416e-a15d-a26a36362c45";
            User_Info newUser = new User_Info(userImage, u, p, pnum, mail, bankNumber, address);
            final UserAccount user = new UserAccount(newUser, date);
            db = FirebaseDatabase.getInstance().getReference();
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isExists = checkExists(snapshot, u, pnum);
                    if(isExists == false)
                    {
                        db.child("Users").child(u).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    CharSequence x = "Register Successfully!";
                                    Toast toast = Toast.makeText(RegisterActivity.this, x, Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            db.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private boolean checkExists(DataSnapshot snapshot, String user, String pnum) {
        if(snapshot.child("Users").child(user).exists())
        {
            CharSequence y = snapshot.child("Users").child(user).child("userInfo").child("phone").toString();
            CharSequence x = "Username already exists!";
            notify(x + " " + y);
            return true;
        }
        else
        {
            for(DataSnapshot traverse: snapshot.child("Users").getChildren())
            {
                if(Objects.equals(traverse.child("phone").getValue(), pnum))
                {
                    CharSequence x = "Phone Number already registered";
                    notify(x);
                    return true;
                }
            }
        }
        return false;
    }


}