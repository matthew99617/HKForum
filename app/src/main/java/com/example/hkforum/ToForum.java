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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hkforum.adapter.PostAdapter;
import com.example.hkforum.model.DistrictSingleton;
import com.example.hkforum.model.PostsImage;
import com.example.hkforum.model.UsernameSingleton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToForum extends AppCompatActivity implements View.OnClickListener {


    private ImageView ic_add;
    private TextView tvForumDistrict;
    private DistrictSingleton district;
    private ArrayList<PostsImage> postsImages;

    private DatabaseReference referencePosts;
    private String tempLocation;
    String username;

    private String strDistrict = "District";
    private UsernameSingleton usernameSingleton;

    RecyclerView recyclerView;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_forum);

        postsImages = new ArrayList<>();

        district = DistrictSingleton.getInstance();
        tempLocation = district.getStrDistrict();

        tvForumDistrict = findViewById(R.id.tvForumDistrict);
        tvForumDistrict.setText(district.getStrDistrict() + " Forum");

        referencePosts = FirebaseDatabase.getInstance().getReference("Posts");

        usernameSingleton = UsernameSingleton.getInstance();

        ic_add = findViewById(R.id.ic_add);
        ic_add.setOnClickListener(this);

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

        recyclerView = findViewById(R.id.recViewPost);

        referencePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child(strDistrict).getValue().toString().equals(tempLocation)) {
                        PostsImage postsImage = dataSnapshot.getValue(PostsImage.class);
                        postsImages.add(postsImage);
                    }
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                username = usernameSingleton.getStrUsername();
                postAdapter = new PostAdapter(username, postsImages, getApplicationContext());
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
