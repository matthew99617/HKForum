package com.example.hkforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostToForum extends AppCompatActivity implements View.OnClickListener{

    private EditText edTitle, edText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        edTitle = findViewById(R.id.editTitle);
        edText = findViewById(R.id.editText);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.post);

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
                        startActivity(new Intent(getApplicationContext(), ToForum.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.gps:
//                        startActivity(new Intent(getApplicationContext(),FindGps.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.post:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backToPost:
                startActivity(new Intent(getApplicationContext(),ToForum.class));
            case R.id.btnPost:
//                startActivity(new Intent(getApplicationContext(), ToForum.class));
        }
    }
}
