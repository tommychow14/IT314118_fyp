package com.example.it314118_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mtxtuid;
    private Button mLogoutbtn,mProfilebtn;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtxtuid = findViewById(R.id.txtuid);
        mLogoutbtn = findViewById(R.id.Logoutbtn);
        mProfilebtn = findViewById(R.id.Profilebtn);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    mtxtuid.setText("name: "+name+"\nuid :"+uid);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            signInAnonymously();
        }

        mProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        mLogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                signInAnonymously();
            }
        });
    }


    private void signInAnonymously() {
        finish();
        Intent i= new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}