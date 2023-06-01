package com.example.it314118_fyp.chat;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it314118_fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");;
    private List<Messages>mMessageList;
    private DatabaseReference mChatUser= FirebaseDatabase.getInstance().getReference().child("Users");

    public MessageAdapter(List<Messages>mMessageList){
        this.mMessageList=mMessageList;
    }
    public MessageViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout,parent,false);
        return new MessageViewHolder(v);
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText,mChatUser_Name;
        public CircleImageView profileImageOtherUser;
        public TextView messageTextOther,messageOtherUser_Name;
        public LinearLayout messageLayout,messageLayoutOther;
        public ImageView messageImage,messageImageOther;

        public MessageViewHolder(View view){
            super(view);

            messageText=view.findViewById(R.id.message_text_layout);
            messageTextOther=view.findViewById(R.id.message_othertext_layout);

            messageImage=view.findViewById(R.id.message_image_layout);
            messageImageOther=view.findViewById(R.id.message_otherimage_layout);

            profileImageOtherUser=view.findViewById(R.id.image_gchat_profile_other);
            messageOtherUser_Name=view.findViewById(R.id.message_other_userName);

            messageLayout=view.findViewById(R.id.layout_gchat_container_me);
            messageLayoutOther=view.findViewById(R.id.layout_gchat_container_other);

            mChatUser_Name=view.findViewById(R.id.message_other_userName);
        }
    }
    public void onBindViewHolder(MessageViewHolder viewHolder,int i){
        String current_user_id=mAuth.getCurrentUser().getUid();
        Messages c=mMessageList.get(i);
        String from_user=c.getFrom();
        String message_type=c.getType();
        mUsersDatabase=mUsersDatabase.child(current_user_id);
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(from_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();

                String message;
                if(from_user.equals(current_user_id)){
                    Log.v("message_type",c.getType());
                    if(message_type.equals("text")){

                        try {
                            message=decryption(c.getMessage(), c.getSecretKey());
                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                            message= String.valueOf(e);
                        }
                        viewHolder.messageText.setText(message);
                        viewHolder.messageText.setVisibility(View.VISIBLE);
                        viewHolder.messageLayout.setVisibility(View.VISIBLE);
                        viewHolder.messageImage.setVisibility(View.GONE);
                        //viewHolder.messageText.setTextDirection(View.TEXT_DIRECTION_RTL);
                    }else if(message_type.equals("image")){
                        viewHolder.messageLayout.setVisibility(View.VISIBLE);
                        viewHolder.messageText.setVisibility(View.GONE);
                        viewHolder.messageImage.setVisibility(View.VISIBLE);
                        Picasso.get().load(c.getMessage()).placeholder(R.drawable.resource_default).into(viewHolder.messageImage);
                    }
                }else {
                    viewHolder.mChatUser_Name.setText(name);
                    Picasso.get().load(image).placeholder(R.drawable.resource_default).into(viewHolder.profileImageOtherUser);
                    if(message_type.equals("text")) {
                        try {
                            message=decryption(c.getMessage(), c.getSecretKey());
                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                            message= String.valueOf(e);
                        }
                        viewHolder.messageTextOther.setText(message);
                        viewHolder.messageImageOther.setVisibility(View.GONE);
                        viewHolder.messageLayoutOther.setVisibility(View.VISIBLE);
                    }else if(message_type.equals("image")){
                        viewHolder.messageLayoutOther.setVisibility(View.VISIBLE);
                        viewHolder.messageTextOther.setVisibility(View.GONE);
                        viewHolder.messageImageOther.setVisibility(View.VISIBLE);
                        Picasso.get().load(c.getMessage()).placeholder(R.drawable.resource_default).into(viewHolder.messageImageOther);
                    }
                }
                //viewHolder.messageOtherUser_Name.setText(from_user_name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount():", String.valueOf(mMessageList.size()));
        return mMessageList.size();
    }

    public static String decryption(String input, String stringkey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        // Do Decryption
        // output.setText(x); // Show the result here
        byte[] decodedkey = Base64.decode(stringkey,Base64.DEFAULT);
        SecretKey key = new SecretKeySpec(decodedkey,"AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedData = Base64.decode(input, Base64.DEFAULT);
        byte[] original = cipher.doFinal(decodedData);

        String oriVal=new String(original, "utf-8");
        return oriVal;

    }

}


