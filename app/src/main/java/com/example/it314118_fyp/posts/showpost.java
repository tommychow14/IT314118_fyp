package com.example.it314118_fyp.posts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it314118_fyp.R;
import com.example.it314118_fyp.multichat.ChatActivity;
import com.example.it314118_fyp.multichat.ChatGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class showpost extends AppCompatActivity {
    FloatingActionButton fab;
    private ArrayList<DataClass> postList;
    private String Uid;
    private ImageButton filterButton;
    private StorageReference mImageStorage;
    private Query useridtypeQuery;
    private RecyclerView mPostsList;
    private DatabaseReference mUsersDatabase;
    private PostsAdapter postsAdapter;
    private FloatingActionButton mCreateChat;

    private FirebaseAuth firebaseAuth;
    private final DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("Posts");
    private final StorageReference StorageRef= FirebaseStorage.getInstance().getReference().child("PostImage");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mPostsList = findViewById(R.id.post_RecyclerView);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        mPostsList.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();

        loadPosts();
    }

    private void loadPosts() {
        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    DataClass dataClass=ds.getValue(DataClass.class);

                    postList.add(dataClass);
                    postsAdapter = new PostsAdapter(showpost.this,postList);

                    mPostsList.setAdapter(postsAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(showpost.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
