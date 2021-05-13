package com.example.hkforum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hkforum.model.District;
import com.example.hkforum.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private FirebaseUser user;
    private ImageView btnProfileBack;
    private District district;

    private String userID;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        builder = new AlertDialog.Builder(this);

        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnProfileBack.setOnClickListener(this);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        TextView editInfo = findViewById(R.id.editInfo);
        editInfo.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView firstNameTextView = findViewById(R.id.tvProfileFirstName);
        final TextView lastNameTextView = findViewById(R.id.tvProfileLastName);
        final TextView emailTextView = findViewById(R.id.tvProfileEmail);
        final TextView userNameTextView = findViewById(R.id.tvProfileUserName);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if (userProfile != null) {
                    String firstName = userProfile.firstName;
                    String lastName = userProfile.lastName;
                    String email = userProfile.email;
                    String userName = userProfile.userName;

                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(email);
                    userNameTextView.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), ChooseLocation.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), ToForum.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.gps:
                        startActivity(new Intent(getApplicationContext(),LocationGPS.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), PostToForum.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profile.this, Login.class));
        } else if (v.getId() == R.id.editInfo) {
            showEditProfileDialog();
        } else if (v.getId() == R.id.btnProfileBack){
            startActivity(new Intent(Profile.this, ToForum.class));
        }
    }

    private void showEditProfileDialog() {
        String[] options = {"Edit Username", "Change Password"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose Action");

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Edit Username
                changeUserNameDialog();
                builder.setMessage("Updating username..");
            } else if (which == 1) {
                // Change Password
                changePasswordDialog();
                builder.setMessage("Changing password..");
            }
        });

        builder.create().show();

    }

    private void changeUserNameDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_update_username, null);
        EditText edUsername = view.findViewById(R.id.edUpdateUserName);
        Button btnUpdateUsername = view.findViewById(R.id.btnUpdateUserName);

        builder = new AlertDialog.Builder(this);
        builder.setView(view);

        builder.create().show();

        btnUpdateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString().trim();
                builder.create().dismiss();
                if (username.isEmpty()) {
                    edUsername.setError("Username is required!");
                    edUsername.requestFocus();
                    return;
                }
                if (updateUserName(username)) {
                    Toast.makeText(Profile.this, "Success to Change Username!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                }
            }
        });
    }

    private boolean updateUserName(String username) {
        reference.child(userID).child("userName").setValue(username);
        return true;
    }

    private void changePasswordDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_update_password, null);
        EditText edOldPassword = view.findViewById(R.id.edOldPassword);
        EditText edNewPassword = view.findViewById(R.id.edNewPassword);
        EditText edConfirmNewPassword = view.findViewById(R.id.edConfirmNewPassword);

        Button btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);

        builder.setView(view);

        builder.create().show();

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edOldPassword.getText().toString().trim();
                String newPassword = edNewPassword.getText().toString().trim();
                String confirmNewPassword = edConfirmNewPassword.getText().toString().trim();

                if (oldPassword.isEmpty()){
                    edOldPassword.setError("Password is required!");
                    edOldPassword.requestFocus();
                    return;
                }

                if (newPassword.length() < 8){
                    edNewPassword.setError("Password length should be 8 characters!");
                    edNewPassword.requestFocus();
                    return;
                }

                if (newPassword.isEmpty()){
                    edNewPassword.setError("Confirm password cannot empty!");
                    edNewPassword.requestFocus();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)){
                    edConfirmNewPassword.setError("It is not matching with your password!");
                    edConfirmNewPassword.requestFocus();
                    return;
                }
                updatePassword(oldPassword, newPassword);
                builder.create().dismiss();
            }
        });
    }

    private void updatePassword(String oldPassword, final String newPassword) {
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassword)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Update Completed! Please Login Again",Toast.LENGTH_SHORT).show();
                                builder.create().dismiss();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                FirebaseAuth.getInstance().signOut();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
