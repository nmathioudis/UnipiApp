package com.example.nikos.unipiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText user, pass;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();


        user = (EditText) findViewById(R.id.eTusername);
        pass = (EditText) findViewById(R.id.eTpasswordLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
    }




    private void userLogin() {
        String username = user.getText().toString();
        String password = pass.getText().toString();

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                } else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
        }


    }
}
