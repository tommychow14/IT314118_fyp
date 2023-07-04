package com.example.it314118_fyp.viewController.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.it314118_fyp.ChatFindActivity;
import com.example.it314118_fyp.ChatRBuildActivity;
import com.example.it314118_fyp.viewController.login.LoginActivity;
import com.example.it314118_fyp.R;
import com.example.it314118_fyp.UserProfileActivity;
import com.example.it314118_fyp.viewController.home.CardListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityBackup extends AppCompatActivity {
    private TextView mtxtuid;
    private Button mLogoutbtn, mProfilebtn, mChatfindbtn, mGroupChatbtn, btnGoCardList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_backup);

        mtxtuid = findViewById(R.id.txtuid);
        mLogoutbtn = findViewById(R.id.Logoutbtn);
        mProfilebtn = findViewById(R.id.Profilebtn);
        mChatfindbtn = findViewById(R.id.message);
        mGroupChatbtn = findViewById(R.id.groupmessage);
        btnGoCardList = findViewById(R.id.btn_goCardList);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    mtxtuid.setText("name: " + name + "\nuid :" + uid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            signInAnonymously();
        }

        mChatfindbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityBackup.this, ChatFindActivity.class);
                startActivity(intent);
            }
        });

        mGroupChatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityBackup.this, ChatRBuildActivity.class);
                startActivity(intent);
            }
        });

        mProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityBackup.this, UserProfileActivity.class);
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

        btnGoCardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityBackup.this, CardListActivity.class);
                startActivity(intent);
            }
        });
    }


    private void signInAnonymously() {
        finish();
        Intent i = new Intent(MainActivityBackup.this, LoginActivity.class);
        startActivity(i);
    }
}