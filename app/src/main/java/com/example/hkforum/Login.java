package com.example.hkforum;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hkforum.model.DistrictSingleton;
import com.example.hkforum.model.UsernameSingleton;
import com.example.hkforum.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity implements LocationListener {

    private TextView registerUser, forgetPassword;
    private EditText edEmail, edPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private DistrictSingleton districtSingleton;

    LocationManager locationManager;
    UsernameSingleton usernameSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        districtSingleton = DistrictSingleton.getInstance();

        usernameSingleton = UsernameSingleton.getInstance();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        forgetPassword = (TextView) findViewById(R.id.tvForgotPassword);
        edEmail = (EditText) findViewById(R.id.inputLoginEmail);
        edPassword = (EditText) findViewById(R.id.inputPassword);
        registerUser = (TextView) findViewById(R.id.tvGoToRegister);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
//                startActivity(new Intent(Login.this, ToForum.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));

            }
        });
        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        getLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Login.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(Login.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String state = addresses.get(0).getAdminArea();

            districtSingleton.setStrDistrict(state);
            System.out.println(districtSingleton.getStrDistrict());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void userLogin() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edEmail.setError("Email is required!");
            edEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Please enter a valid email!");
            edEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edPassword.setError("Password is required!");
            edPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            edPassword.setError("Password should be 8 characters!");
            edPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                closeKeyboard();
                if (task.isSuccessful()) {

                    startActivity(new Intent(Login.this, Profile.class));
                } else {
                    Toast.makeText(Login.this, "Failed to login! Please check your email or password!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}