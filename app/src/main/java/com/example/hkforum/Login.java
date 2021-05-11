package com.example.hkforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser,forgetPassword;
    private EditText edEmail, edPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        forgetPassword = (TextView) findViewById(R.id.tvForgotPassword);
        edEmail = (EditText) findViewById(R.id.inputEmail);
        edPassword = (EditText) findViewById(R.id.inputPassword);
        registerUser = (TextView) findViewById(R.id.tvGoToRegister);
        registerUser.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                startActivity(new Intent(this,Forum.class));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue("Hello, World");
                break;
            case R.id.tvGoToRegister:
                startActivity(new Intent(this,Register.class));
        }
    }
}