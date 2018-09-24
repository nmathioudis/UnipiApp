package com.example.nikos.unipiapp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import static android.support.v4.content.ContextCompat.startActivity;

public class DropDownMenu extends AppCompatActivity{
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drop_down_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.main){
            startActivity(new Intent(this,MainActivity.class));
        }
        else if(item.getItemId() == R.id.profile){
            startActivity(new Intent(this,ProfileActivity.class));
        }
        else if(item.getItemId() == R.id.favorites){
            startActivity(new Intent(this,Favorites.class));
        }
        else if(item.getItemId() == R.id.signout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent logoutIntentDropDown = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(logoutIntentDropDown);
    }
}
