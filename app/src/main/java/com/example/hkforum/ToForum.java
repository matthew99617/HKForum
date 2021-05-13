package com.example.hkforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hkforum.model.District;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ToForum extends AppCompatActivity implements View.OnClickListener{

    private ImageView ic_add;
    private TextView tvForumDistrict;
    private District district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_forum);

        district = District.getInstance();
        tvForumDistrict = findViewById(R.id.tvForumDistrict);
        tvForumDistrict.setText(district.getStrDistrict()+" Forum");

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
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), ChooseLocation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.gps:
                        startActivity(new Intent(getApplicationContext(),LocationGPS.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(),PostToForum.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_add){
            startActivity(new Intent(getApplicationContext(),PostToForum.class));
        }
    }
}
