package com.example.hkforum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
//                userLogin();
                startActivity(new Intent(this, ToForum.class));

                break;
            case R.id.tvGoToRegister:
                startActivity(new Intent(this,Register.class));
        }
    }

    private void userLogin() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if(email.isEmpty()){
            edEmail.setError("Email is required!");
            edEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Please enter a valid email!");
            edEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            edPassword.setError("Password is required!");
            edPassword.requestFocus();
            return;
        }
        if(password.length() < 8){
            edPassword.setError("Password should be 8 characters!");
            edPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                closeKeyboard();

                if (task.isSuccessful()){
                    //redirect to user profile
                    startActivity(new Intent(Login.this, ChooseLocation.class));
                } else {
                    Toast.makeText(Login.this, "Failed to login! Please check your email or password!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}