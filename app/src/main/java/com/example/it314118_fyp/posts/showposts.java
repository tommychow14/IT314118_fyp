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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class showposts extends AppCompatActivity {
    FloatingActionButton fab;
    private GridView gridView;
    private ArrayList<DataClass> dataList;
    Grid_ImageAdapter grid_imageAdapter;
    private String Uid;
    private ImageButton filterButton;
    final private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("Items");
    private StorageReference mImageStorage;
    private Query useridtypeQuery;

    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerAdapter<ChatGroup, showposts.UsersViewHolder> mUserRVAdapter;
    private FloatingActionButton mCreateChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        fab = findViewById(R.id.fab);
        Uid = FirebaseAuth.getInstance().getUid();
        gridView = findViewById(R.id.gridview);
        dataList = new ArrayList<>();
        grid_imageAdapter = new Grid_ImageAdapter(dataList,this);
        gridView.setAdapter(grid_imageAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url =dataList.get(position).getImageUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Query mChatGroupsDatabaseQuery = mUsersDatabase.orderByKey();
        FirebaseRecyclerOptions UsersDatabase = new FirebaseRecyclerOptions.Builder<ChatGroup>()
                .setQuery(mChatGroupsDatabaseQuery, ChatGroup.class).build();
        mUserRVAdapter = new FirebaseRecyclerAdapter<ChatGroup, showposts.UsersViewHolder>(UsersDatabase) {
            @Override
            protected void onBindViewHolder(@NonNull showposts.UsersViewHolder holder, int position, @NonNull ChatGroup model) {
                String chatGroup_id = getRef(position).getKey();
                FirebaseDatabase.getInstance().getReference().child("ChatGroup").child(chatGroup_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String chatGroup_Name = snapshot.child("name").getValue().toString();
                        holder.setName(chatGroup_Name);
                        String chatGroup_status = snapshot.child("status").getValue().toString();
                        holder.setStatus(chatGroup_status);
                        String chatGroup_image = snapshot.child("image").getValue().toString();
                        holder.setUserImage(chatGroup_image, showposts.this);

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(showposts.this, ChatActivity.class);
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
            public showposts.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_single_layout
                                , parent, false);
                return new showposts.UsersViewHolder(view);
            }
        };
        mUsersList.setAdapter(mUserRVAdapter);

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
