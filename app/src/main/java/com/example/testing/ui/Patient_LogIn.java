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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Patient_LogIn extends AppCompatActivity implements View.OnClickListener {
    TextView reg,forgot;
    TextView useremail, userpass;
    Button userlogin;
    ProgressBar progressBar2;
    private FirebaseAuth mAuth;
    private DatabaseReference reff;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__log_in);
        reg = (TextView) findViewById(R.id.reg);
        reg.setOnClickListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        useremail = (TextView) findViewById(R.id.useremail);
        userpass = (TextView) findViewById(R.id.userpass);
        userlogin = (Button) findViewById(R.id.userlogin);

        userlogin.setOnClickListener(this);
        forgot=(TextView)findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg:
                startActivity(new Intent(this, Register_Activity.class));
                break;


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
                    if (user.isEmailVerified()) {
                        Intent i = new Intent(Patient_LogIn.this, Patient_Dashboard.class);
                        reff = FirebaseDatabase.getInstance().getReference("patients");
                        userID = user.getUid();
                        startActivity(i);
                        progressBar2.setVisibility(View.GONE);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Patient_LogIn.this, "Check your email to verify your account!",Toast.LENGTH_LONG).show();
                        progressBar2.setVisibility(View.GONE);

                    }


                } else {
                    Toast.makeText(Patient_LogIn.this, "Login failed! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }

            }
        });

    }
}