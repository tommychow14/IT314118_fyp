package com.example.it314118_fyp.posts;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it314118_fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentAdapter>{
    Context context;
    ArrayList<comments> comments;

    public CommentsAdapter(Context context, ArrayList<comments>comments) {
        this.comments= comments;
        this.context=context;
    }

    @NonNull
    @Override
    public CommentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_single_layout,parent,false);
        return new CommentAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter holder, int position) {
        String uid = comments.get(position).getUid();

        DatabaseReference mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                holder.tvuName.setText(name);
                try{
                    Picasso.get().load(image).placeholder(R.drawable.resource_default).into(holder.ivUserICONs);
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String cid = comments.get(position).getCid();
        String cTimestamp = comments.get(position).getTimestamp();
        String comment = comments.get(position).getComment();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(cTimestamp));
        String cTime = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        Log.d("cTime",cTime);
        //holder.ivuName.setText(name);
        holder.tvcTime.setText(cTime);
        holder.tvcomment.setText(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentAdapter extends RecyclerView.ViewHolder{
        ImageView ivUserICONs;
        TextView tvuName, tvcomment,tvcTime;
        //ImageButton btnmore;

        public CommentAdapter(View view){
            super(view);

            ivUserICONs = view.findViewById(R.id.usericons);
            tvuName = view.findViewById(R.id.tvname);
            tvcomment = view.findViewById(R.id.tvcomment);
            tvcTime = view.findViewById(R.id.tvtime);
    }


}
}


