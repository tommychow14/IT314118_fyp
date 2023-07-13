package com.example.it314118_fyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it314118_fyp.R;
import com.example.it314118_fyp.StatusActivity;
import com.example.it314118_fyp.UserProfileActivity;
import com.example.it314118_fyp.multichat.ChatGroup;
import com.example.it314118_fyp.viewController.chat.ChatActivity;
import com.example.it314118_fyp.viewController.chat.Users;
import com.example.it314118_fyp.viewController.login.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFindActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFindActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> mUserRVAdapter;
    private FloatingActionButton mCreateChat;
    private String Uid;

    public ChatFindActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFindActivity newInstance(String param1, String param2) {
        ChatFindActivity fragment = new ChatFindActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_chatadd, container, false);


        Uid = FirebaseAuth.getInstance().getUid();

        //mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Chat").child(Uid);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList = view.findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(getContext()));


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
                        holder.setUserImage(image, getContext());

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), ChatActivity.class);
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
        return view;
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