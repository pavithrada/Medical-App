package com.example.testing.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Patient_Dashboard extends AppCompatActivity {
    dataholder patientObj;

    TextView tvPGreet,id;
    Button btnApt, btnPPrescription, btnComments, btnPtLogOut,btnsymp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__dashboard);
        //final String mail = getIntent().getStringExtra("mail");
        tvPGreet = findViewById(R.id.tvPGreet);

        String pid = getIntent().getStringExtra("pid");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Patients Registered").child(pid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 patientObj = snapshot.getValue(dataholder.class);
                String name = patientObj.name1;
                tvPGreet.setText("Welcome "+ name + "!" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //id = findViewById(R.id.pid);
        //id.setText(pid);
        btnPPrescription = findViewById(R.id.btnPPrescription);
        btnComments = findViewById(R.id.btnComments);
        btnApt = findViewById(R.id.btnApt);
        btnsymp=(Button)findViewById(R.id.btnsymp);
        btnPtLogOut = findViewById(R.id.btnPtLogOut);

        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Patient_Dashboard.this, AllComments.class);
                i.putExtra("pid",pid);
                startActivity(i);
            }
        });

        btnApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Patient_Dashboard.this, AllSlots.class);
                i.putExtra("pid",pid);
                i.putExtra("pname",patientObj.name1);
                startActivity(i);
            }
        });

        btnsymp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Patient_Dashboard.this, AcceptData.class);
                i.putExtra("pid",pid);
                startActivity(i);
            }
        });

        btnPPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Patient_Dashboard.this, My_Prescriptions.class);
                i.putExtra("pid",pid);
                //i.putExtra("pname",pname);
                startActivity(i);
            }
        });

        btnPtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Patient_Dashboard.this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Patient_Dashboard.this, MainActivity.class));
                finish();
            }
        });

    }
}
