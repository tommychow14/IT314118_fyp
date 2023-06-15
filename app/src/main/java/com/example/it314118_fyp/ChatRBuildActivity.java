package com.example.it314118_fyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRBuildActivity extends AppCompatActivity {
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerAdapter<ChatGroup, UsersViewHolder> mUserRVAdapter;
    private FloatingActionButton mCreateChat;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatadd);

        Uid = FirebaseAuth.getInstance().getUid();

        //mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Chat").child(Uid);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("ChatGroup");

        mCreateChat = findViewById(R.id.createChatGroup);
        mUsersList = findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


        mCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatRBuildActivity.this,ChatRActivity.class);
                startActivity(intent);
            }
        });

        Query mChatGroupsDatabaseQuery = mUsersDatabase.orderByKey();
        FirebaseRecyclerOptions UsersDatabase = new FirebaseRecyclerOptions.Builder<ChatGroup>()
                .setQuery(mChatGroupsDatabaseQuery, ChatGroup.class).build();
        mUserRVAdapter = new FirebaseRecyclerAdapter<ChatGroup, UsersViewHolder>(UsersDatabase) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull ChatGroup model) {
                String chatGroup_id = getRef(position).getKey();
                FirebaseDatabase.getInstance().getReference().child("ChatGroup").child(chatGroup_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String chatGroup_Name = snapshot.child("name").getValue().toString();
                        holder.setName(chatGroup_Name);
                        String chatGroup_status = snapshot.child("status").getValue().toString();
                        holder.setStatus(chatGroup_status);
                        String chatGroup_image = snapshot.child("image").getValue().toString();
                        holder.setUserImage(chatGroup_image, ChatRBuildActivity.this);

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ChatRBuildActivity.this, ChatActivity.class);
                                intent.putExtra("chatGroup_id", chatGroup_id);
                                intent.putExtra("chatGroup_Name", chatGroup_Name);
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