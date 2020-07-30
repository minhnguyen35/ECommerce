package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class RegisterActivity extends AppCompatActivity {
    private Button login_reg;
    private Button join;
    private EditText username, password, phone;
    private DatabaseReference db;
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

        join = (Button)findViewById(R.id.join);
        username = (EditText)findViewById(R.id.input_user);
        password = (EditText)findViewById(R.id.pass_reg);
        phone = (EditText)findViewById(R.id.user_number);

        join = (Button)findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRegisterButton();
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
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String phone_num = phone.getText().toString();
        if(user == null)
        {
            CharSequence x = "Empty Username!";
            notify(x);
        }
        else if(pass == null)
        {

        }
    }
    private void setRegisterButton() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String phone_num = phone.getText().toString();
    }

}