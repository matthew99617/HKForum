package com.example.hkforum;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hkforum.model.DistrictSingleton;
import com.example.hkforum.model.PostsImage;
import com.example.hkforum.model.UsernameSingleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class PostToForum extends AppCompatActivity implements View.OnClickListener {

    private EditText edTitle, edText;

    private ImageView backToForum, ic_add_photo, postImage;
    private DistrictSingleton districtSingleton;
    private UsernameSingleton usernameSingleton;
    private String username;
    private String tempLocation, title, content;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private TextView tvRemindUser, btnPost;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int PERMISSION_CODE = 1000;
    public static final int IMAGE_CAPTURE_CODE = 1001;
    public static final int PICK_IMAGE = 1;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        usernameSingleton = UsernameSingleton.getInstance();
        username = usernameSingleton.getStrUsername();

        districtSingleton = DistrictSingleton.getInstance();
        tempLocation = districtSingleton.getStrDistrict();

        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        storageReference = FirebaseStorage.getInstance().getReference("Posts");

        ic_add_photo = findViewById(R.id.img_add_photo);
        ic_add_photo.setOnClickListener(this);
        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);
        postImage = findViewById(R.id.postImage);
        edTitle = findViewById(R.id.editTitle);
        edText = findViewById(R.id.editText);
        tvRemindUser = findViewById(R.id.tvRemindUser);
        tvRemindUser.setText("You are going to post in " + tempLocation + " forum.");

        backToForum = findViewById(R.id.backToPost);
        backToForum.setOnClickListener(this);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.post);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), ToForum.class));
                        overridePendingTransition(0, 0);
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
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backToPost) {
            startActivity(new Intent(getApplicationContext(), ToForum.class));
        } else if (v.getId() == R.id.btnPost) {
            title = edTitle.getText().toString().trim();
            content = edText.getText().toString().trim();

            if (title.isEmpty()) {
                edTitle.setError("Title is required!");
                edTitle.requestFocus();
                return;
            }
            if (content.isEmpty()) {
                edText.setError("Text is required!");
                edText.requestFocus();
                return;
            }
            if (imageUri == null) {
                onPostWithoutImage();
            } else {
                onPostWithImage(imageUri);
            }
//                startActivity(new Intent(getApplicationContext(), ToForum.class));
        } else if (v.getId() == R.id.img_add_photo) {
            showChooseDialog();
        }
    }

    private void showChooseDialog() {
        String[] options = {"Take Photo", "Choose a Picture From Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Picture");

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                cameraPermission();
            } else if (which == 1) {
                // Change Password
                choosePhoto();
            }
        });

        builder.create().show();

    }

    private void cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager
                    .PERMISSION_DENIED || checkSelfPermission(Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            postImage.setImageURI(imageUri);
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                postImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void choosePhoto() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }

    private void onPostWithImage(Uri uri) {

        StorageReference storageReference2 = storageReference
                .child(System.currentTimeMillis() + "." + GetFileExtension(uri));
        storageReference2.putFile(uri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getApplicationContext(),"Image Uploaded Successfully",Toast.LENGTH_LONG).show();

                        PostsImage postsImage = new PostsImage(tempLocation,
                                username, title, content, uri.toString());

                        databaseReference.push().setValue(postsImage);
                    }
                });
                // Reset all input
                edText.setText("");
                edTitle.setText("");
                imageUri = null;
                postImage.invalidate();
                postImage.setImageBitmap(null);
                closeKeyboard();
            }
        });

    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void onPostWithoutImage() {
        String title = edTitle.getText().toString().trim();
        String content = edText.getText().toString().trim();
        if (title.isEmpty()) {
            edTitle.setError("Title is required!");
            edTitle.requestFocus();
            return;
        }
        if (content.isEmpty()) {
            edText.setError("Text is required!");
            edText.requestFocus();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("Title", title);
        map.put("Text", content);
        map.put("Username", username);
        map.put("District", tempLocation);
        map.put("imageUrl", null);

        databaseReference.push().setValue(map);
        edText.setText("");
        edTitle.setText("");
        closeKeyboard();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
