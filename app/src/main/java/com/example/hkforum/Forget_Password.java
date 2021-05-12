package com.example.hkforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_Password extends AppCompatActivity implements View.OnClickListener{

    private ImageView btnBackToLogin;

    private EditText resetEmail;
    private Button btnReset;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mAuth = FirebaseAuth.getInstance();

        resetEmail = findViewById(R.id.inputResetEmail);

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        btnBackToLogin = findViewById(R.id.btnLoginBack);
        btnBackToLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginBack:
                startActivity(new Intent(getApplicationContext(), Login.class));
            case R.id.btnReset:
                resetPassword();
        }
    }

    private void resetPassword() {
        String email = resetEmail.getText().toString().trim();

        if (email.isEmpty()){
            resetEmail.setError("Email is required!");
            resetEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetEmail.setError("Please provide valid email!");
            resetEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Forget_Password.this,"Check your email to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Forget_Password.this,"Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}