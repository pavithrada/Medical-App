package com.example.testing.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    TextView forgot;
    TextView useremail, userpass;
    Button userlogin;
    ProgressBar progressBar2;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        useremail = (TextView) findViewById(R.id.useremail);
        userpass = (TextView) findViewById(R.id.userpass);
        userlogin = (Button) findViewById(R.id.userlogin);
        userlogin.setOnClickListener(this);
        progressBar2 = (ProgressBar)findViewById(R.id.pg);
        forgot=(TextView)findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userlogin:
                userlogin();
                break;

            case R.id.forgot:
                startActivity(new Intent(this,user_forgot.class));
                break;
        }
    }

    private void userlogin() {

        String email3 = useremail.getText().toString().trim();
        String password3 = userpass.getText().toString().trim();

        if (email3.isEmpty()) {
            useremail.setError("Email is required!");
            useremail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email3).matches()) {
            useremail.setError("Please provide a valid email!");
            useremail.requestFocus();
            return;
        }
        if (password3.isEmpty()) {
            userpass.setError("Email is required!");
            userpass.requestFocus();
            return;
        }
        if (password3.length() < 6) {
            userpass.setError("Min password length must be 6");
            userpass.requestFocus();
            return;
        }
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email3, password3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String U = user.getUid();
                    if (user.isEmailVerified()) {
                        Intent i = new Intent(new Intent(Login_Activity.this, Dashboard.class));
                        i.putExtra("pid",U);
                        startActivity(i);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login_Activity.this, "Check your email to verify your account!",Toast.LENGTH_LONG).show();

                    }
                    progressBar2.setVisibility(View.GONE);


                } else {
                    Toast.makeText(Login_Activity.this, "Login failed! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }

            }
        });

    }
}
