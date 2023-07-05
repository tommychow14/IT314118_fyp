package com.example.it314118_fyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatRActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private TextInputLayout group_name,group_status;
    private String group_nameTXT,group_statusTXT;
    private String userid;
    private Button createbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatcreate);

        mAuth = FirebaseAuth.getInstance();
        mRootRef= FirebaseDatabase.getInstance().getReference();
        userid = mAuth.getCurrentUser().getUid();
        group_name= findViewById(R.id.displayName_input);
        group_status= findViewById(R.id.status_input);
        createbtn=findViewById(R.id.Create_btn);

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group_nameTXT=group_name.getEditText().getText().toString();
                group_statusTXT=group_status.getEditText().getText().toString();
                sendMessage(group_nameTXT,group_statusTXT,userid);
            }
        });
    }

    private void sendMessage(String name,String status,String uid) {
        Log.d("click",name);

        if(!TextUtils.isEmpty(name)){
            DatabaseReference user_message_push=mRootRef
                    .child("ChatGroup").push();

            String push_id=user_message_push.getKey();

            Map messageMap=new HashMap();


            messageMap.put("image","default");
            messageMap.put("userid", uid);
            messageMap.put("name", name);
            messageMap.put("status",status);


            Map messageUserMap=new HashMap();
            messageUserMap.put("ChatGroup/"+push_id,messageMap);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error!=null){
                        Log.d("CHAT_LOG",error.getMessage().toString());
                    }
                }
            });
        }
    }

}