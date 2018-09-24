package com.example.nikos.unipiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends DropDownMenu {

    private static final int PIC = 400;
    ImageView profilePic;
    EditText nickname;
    Uri uriProfilePicture;
    ProgressBar progressBar;
    String downloadUrlProfilePic;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = (ImageView) findViewById(R.id.imgUserPictureProfile);
        nickname = (EditText) findViewById(R.id.eTusernameProfile);
        progressBar = (ProgressBar) findViewById(R.id.pgbProfilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionImages();
            }
        });
        mAuth = FirebaseAuth.getInstance();


        loadUserInfo();

        findViewById(R.id.btnSaveProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();

            }
        });

        findViewById(R.id.btnLogoutProfileActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutProfileActivity();
            }
        });

    }

    private void logoutProfileActivity() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profilePic);
            }

            if (user.getDisplayName() != null) {
                nickname.setText(user.getDisplayName());
            }

        }

    }

    private void saveUserInformation() {
        String displayName = nickname.getText().toString();
        if (displayName.isEmpty()) {
            nickname.setError("Field required");
            nickname.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && downloadUrlProfilePic != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(downloadUrlProfilePic))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfilePicture = data.getData();
            try {
                Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfilePicture);
                profilePic.setImageBitmap(bit);

                uploadImagetoFireCloud();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImagetoFireCloud() {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + "jpg");
        if (uriProfilePicture != null) {
            profilePicRef.putFile((uriProfilePicture)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);

                    downloadUrlProfilePic = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //create an image chooser from your photos
    private void showSelectionImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PIC);
    }
}
