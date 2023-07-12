package com.example.it314118_fyp.posts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it314118_fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    ImageView ivuICON,ivImage;
    TextView tvuName,tvpTime,tvpTitle,tvpDetail,pLike;
    Button likebtn;
    Toolbar mChatToolbar;
    FirebaseAuth mAuth;
    RecyclerView comment_recyclerView;

    ArrayList<comments> commentsList;
    CommentsAdapter commentsAdapter;

    //LinearLayout profileLayout;

    private final DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("Posts");
    EditText commentET;
    ImageButton sendBtn;
    ImageView uiCON2;
    String uid;

    String myuid,myName,myDetail
            ,pid,plike,hisDetail,hisName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mChatToolbar=findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Post Detail");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        pid= intent.getStringExtra("pid");

        ivuICON = findViewById(R.id.uIcon);
        ivImage = findViewById(R.id.pImage);

        tvuName = findViewById(R.id.uName);
        tvpTime = findViewById(R.id.pTimeTv);
        tvpTitle = findViewById(R.id.pTitle);
        tvpDetail = findViewById(R.id.pdetail);
        //profileLayout = findViewById(R.id.profile);
        commentET = findViewById(R.id.et_comment);
        sendBtn = findViewById(R.id.sendBtn);
        uiCON2 = findViewById(R.id.usericon);

        comment_recyclerView = findViewById(R.id.comment_RecyclerView);

        commentsList = new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        loadPostInfo();

        loadUserInfo(uid);
        
        loadComments();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        comment_recyclerView.setLayoutManager(layoutManager);

        DatabaseReference CommentRef = Dataref.child(pid).child("comments");
        CommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){

                    comments comments =ds.getValue(comments.class);
                    commentsList.add(comments);

                    commentsAdapter = new CommentsAdapter(getApplicationContext(),commentsList);
                    comment_recyclerView.setAdapter(commentsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void postComment() {
        String comment = commentET.getText().toString().trim();

        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this, "Comment is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference commentsRer=Dataref.child(pid).child("comments");
        String pushid=commentsRer.push().getKey();

        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("cid",pushid);
        hashMap.put("comment",comment);
        hashMap.put("timestamp",timeStamp);
        hashMap.put("uid",uid);

        commentsRer.child(pushid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                commentET.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadUserInfo(String uid) {
        DatabaseReference mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                //mName.setText(name);

                if (!image.equals("default")) {
                    try {
                        Picasso.get().load(image).placeholder(R.drawable.resource_default).into(uiCON2);
                    }catch (Exception e){
                        
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostInfo() {
        Query query = Dataref.orderByChild("pId").equalTo(pid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){

                    String pTitle = ""+ds.child("pTitle").getValue();
                    String pDetail = ""+ds.child("pDetail").getValue();
                    String pImage = ""+ds.child("pImage").getValue();
                    String pTimeStamp = ""+ds.child("pTime").getValue();
                    String puid = ""+ds.child("uid").getValue();

                    DatabaseReference mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(puid);
                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            String image = dataSnapshot.child("image").getValue().toString();
                            tvuName.setText(name);
                            try{
                                Picasso.get().load(image).placeholder(R.drawable.resource_default).into(ivuICON);
                            }catch (Exception e){}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

                    tvpTitle.setText(pTitle);
                    tvpTime.setText(pTime);
                    tvpDetail.setText(pDetail);
                    //tvuName.setText(hisName);


                    if (pImage.equals("noImage")){
                        ivImage.setVisibility(View.GONE);
                    }else{
                        try{
                            Picasso.get().load(pImage).into(ivImage);
                        }catch (Exception e){}
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
