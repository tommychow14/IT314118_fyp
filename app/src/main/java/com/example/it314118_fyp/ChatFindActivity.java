package com.example.it314118_fyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it314118_fyp.chat.ChatActivity;
import com.example.it314118_fyp.chat.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFindActivity extends AppCompatActivity {
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> mUserRVAdapter;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatfind);

        Uid = FirebaseAuth.getInstance().getUid();

        //mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Chat").child(Uid);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList = findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


        Query mUsersDatabaseQuery = mUsersDatabase.orderByKey();
        FirebaseRecyclerOptions UsersDatabase = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUsersDatabaseQuery, Users.class).build();
        mUserRVAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(UsersDatabase) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                String user_id = getRef(position).getKey();
                FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String user_Name = snapshot.child("name").getValue().toString();
                        holder.setName(user_Name);
                        String status = snapshot.child("status").getValue().toString();
                        holder.setStatus(status);
                        String image = snapshot.child("image").getValue().toString();
                        holder.setUserImage(image, ChatFindActivity.this);

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ChatFindActivity.this, ChatActivity.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("user_Name", user_Name);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_single_layout
                                , parent, false);
                return new UsersViewHolder(view);
            }
        };
        mUsersList.setAdapter(mUserRVAdapter);
    }

    public void onStart() {
        super.onStart();
        mUserRVAdapter.startListening();
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setStatus(String Status) {
            TextView userStatusView = mView.findViewById(R.id.user_single_status);
            userStatusView.setText(Status);
        }

        public void setUserImage(String image, Context context) {
            CircleImageView userImageView = mView.findViewById(R.id.user_signle_image);
            Picasso.get().load(image).placeholder(R.drawable.resource_default).into(userImageView);
        }

    }
}