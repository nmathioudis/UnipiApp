package com.example.nikos.unipiapp;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText user, pass, passVerification;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        user = findViewById(R.id.eTAddEmail);
        pass = findViewById((R.id.eTAddPassword));
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        passVerification = findViewById(R.id.eTAddPasswordVerification);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String username = user.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String passVer = passVerification.getText().toString().trim();
        if (username.isEmpty()) {
            user.setError("Email is required");
            user.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            user.setError("Please enter a valid email");
            user.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            pass.setError("Password must have 6 characters");
            pass.requestFocus();
            return;
        }
        if (passVer.isEmpty()) {
            passVerification.setError("Password Verification is required");
            passVerification.requestFocus();
            return;
        }

        if(!password.equals(passVer)){
            passVerification.setError("Passwords do not much");
            passVerification.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if(password.equals(passVer)) {
            mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });
        }

    }

}
