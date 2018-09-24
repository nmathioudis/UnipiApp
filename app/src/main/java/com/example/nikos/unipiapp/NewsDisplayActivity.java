package com.example.nikos.unipiapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsDisplayActivity extends DropDownMenu {


    TextView titleView;
    TextView descriptionView;
    TextView nameView;
    ImageView imageView;
    Context context;
    ImageView favorite, favoritetrue;
    List<String> newslist;

    String title;
    String content;
    String name;
    String urlImage;
    FirebaseAuth mAuth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);


        mAuth = FirebaseAuth.getInstance();



        title = this.getIntent().getExtras().getString("TITLE");
        content = this.getIntent().getExtras().getString("CONTENT");
        name = this.getIntent().getExtras().getString("NAME");
        urlImage = this.getIntent().getExtras().getString("URLIMAGE");




        // Use Content
        Log.d("MessegeTitle", "Set" + title);
        titleView = findViewById(R.id.txtTitleNewsDetailsActivity);
        titleView.setText(title);

        Log.d("MessegeContent", "Set" + content);
        descriptionView = findViewById(R.id.txtContentNewsDetailsActivity);
        descriptionView.setText(content);

        Log.d("MessegeName", "Set" + name);
        nameView = findViewById(R.id.txtSourceNewsDetailsActivity);
        nameView.setText(name);

        Log.d("MessegeUrlImage", "Set" + urlImage);
        imageView = findViewById(R.id.imgNewsDetailsActivity);
        Picasso.with(context).load(urlImage).into(imageView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Favorites/").child(mAuth.getUid());

        favorite = (ImageView) findViewById(R.id.imgFavoriteNot);
        favoritetrue = (ImageView) findViewById(R.id.imgFavorite);

        // Add To Favorite //
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFavorite(title, content, name, urlImage);

                favorite.setVisibility(View.GONE);
                favoritetrue.setVisibility(View.VISIBLE);
            }
        });

        // Remove to Favorite //
        favoritetrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeFavorite(title);

                favorite.setVisibility(View.VISIBLE);
                favoritetrue.setVisibility(View.GONE);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            android.content.Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot post = dataSnapshot;
                FavoriteNewsInformation dataretrieval = post.getValue(FavoriteNewsInformation.class);

                // Disable Stared if Article is Favorite //
                String favTitleRetrieval = dataretrieval.getTitle().toString();
                Log.d("FavoriteNews", dataretrieval.getTitle().toString());
                if (favTitleRetrieval.contains(title)) {
                    favorite.setVisibility(View.GONE);
                    favoritetrue.setVisibility(View.VISIBLE);
                }

                // -- Dokimes Eksagogis se lista -- //
                long countFav = post.getChildrenCount();
                String favPushRetrieval = post.getKey();
                String favPushTitle = favTitleRetrieval + "," + favPushRetrieval;
                List<String> favTitleRetrievalList = new ArrayList<String>(Arrays.asList(favPushTitle.split(",")));
                Log.d("FavoriteNews", favTitleRetrieval + " " + favPushTitle + " " + favTitleRetrievalList);
                Log.d("FavoriteIDs", favPushRetrieval);
                // -- Telos Dokimes Eksagogis se lista -- //
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void addFavorite(String title, String content, String name, String urlImage) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("title", title);
        taskMap.put("content", content);
        taskMap.put("source", name);
        taskMap.put("urlImage", urlImage);
        myRef.push().setValue(taskMap);
    }

    public void removeFavorite(String title) {
        myRef.child(title)
                .child("title").setValue(title);
    }

}


