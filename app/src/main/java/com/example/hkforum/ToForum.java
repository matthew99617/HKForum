package com.example.hkforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hkforum.model.DistrictSingleton;
import com.example.hkforum.model.Posts;
import com.example.hkforum.model.UsernameSingleton;
import com.example.hkforum.model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ToForum extends AppCompatActivity implements View.OnClickListener {

    private String userID;
    private ImageView ic_add;
    private TextView tvForumDistrict;
    private DistrictSingleton district;
    private Posts posts;

    private DatabaseReference referenceUsername, referencePosts;
    private FirebaseUser user;

    private UsernameSingleton usernameSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_forum);

        posts = new Posts();

        district = DistrictSingleton.getInstance();
        String temp = getIntent().getStringExtra("Location");

        tvForumDistrict = findViewById(R.id.tvForumDistrict);
        tvForumDistrict.setText(district.getStrDistrict() + " Forum");

        referencePosts = FirebaseDatabase.getInstance().getReference("Posts");

        usernameSingleton = UsernameSingleton.getInstance();

        ic_add = findViewById(R.id.ic_add);
        ic_add.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsername = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        referenceUsername.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if (userProfile != null) {
                    String userName = userProfile.userName;

                    usernameSingleton.setStrUsername(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ToForum.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

//        referencePosts.child(posts.getId());


        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.gps:
                        startActivity(new Intent(getApplicationContext(), LocationGPS.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
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
        if (v.getId() == R.id.ic_add) {
            startActivity(new Intent(getApplicationContext(), PostToForum.class));
        }
    }
}
