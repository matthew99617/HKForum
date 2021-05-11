package com.example.hkforum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hkforum.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText edFirstName, edLastName, edEmail, edUserName, edPassword, edConfirmPassword;
    private Button btnClear, btnSubmit;
    private ImageView regViewBack;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root = database.getReference("Users");

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edFirstName = (EditText) findViewById(R.id.edFirstName);
        edLastName = (EditText) findViewById(R.id.edLastName);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edUserName = (EditText) findViewById(R.id.edUserName);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edConfirmPassword = (EditText) findViewById(R.id.edConfirmPassword);

        regViewBack = (ImageView) findViewById(R.id.regViewBack);
        regViewBack.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClear:
                edFirstName.setText("");
                edLastName.setText("");
                edEmail.setText("");
                edUserName.setText("");
                edPassword.setText("");
                edConfirmPassword.setText("");
                break;
            case R.id.btnSubmit:
                registerUser();
                break;
            case R.id.regViewBack:
                startActivity(new Intent(this, Login.class));
        }
    }

    private void registerUser(){
        String firstName = edFirstName.getText().toString().trim();
        String lastName = edLastName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String userName = edUserName.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confirmPassword = edConfirmPassword.getText().toString().trim();

        if (firstName.isEmpty()){
            edFirstName.setError("First name is required!");
            edFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()){
            edLastName.setError("Last name is required!");
            edLastName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            edEmail.setError("Email is required!");
            edEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Please provide valid email!");
            edEmail.requestFocus();
            return;
        }

        if (userName.isEmpty()){
            edUserName.setError("Username is required!");
            edUserName.requestFocus();
            return;
        }

        if (password.isEmpty()){
            edPassword.setError("Password is required!");
            edPassword.requestFocus();
            return;
        }

        if (password.length() < 8){
            edPassword.setError("Password length should be 8 characters!");
            edPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()){
            edConfirmPassword.setError("Confirm password cannot empty!");
            edConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)){
            edConfirmPassword.setError("It is not matching with your password!");
            edConfirmPassword.requestFocus();
            return;
        }

//        HashMap<String, String> userMap = new HashMap<>();
//
//        userMap.put("FirstName",firstName);
//        userMap.put("LastName",lastName);
//        userMap.put("UserName",userName);
//        userMap.put("Email",email);
//        userMap.put("Password",password);
//
//        root.setValue(firstName);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        closeKeyboard();

                        if (task.isSuccessful()){
                            User user = new User(firstName, lastName, userName, email, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        startActivity(new Intent(Register.this, Login.class));
                                        Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(Register.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }

                                }

                            });
                        } else {
                            Toast.makeText(Register.this,"Failed to register!", Toast.LENGTH_LONG).show();
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
