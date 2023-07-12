package com.example.it314118_fyp.posts;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostAdapter>{
    Context context;
    ArrayList<DataClass> post;

    public PostsAdapter(Context context,ArrayList<DataClass>post) {
        this.post=post;
        this.context=context;
    }

    @NonNull
    @Override
    public PostAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_single_layout,parent,false);
        return new PostAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter holder, int position) {
        String uid = post.get(position).getUid();

        DatabaseReference mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                holder.ivuName.setText(name);
                try{
                    Picasso.get().load(image).placeholder(R.drawable.resource_default).into(holder.ivuICON);
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String pid = post.get(position).getpId();
        String pTitle = post.get(position).getpTitle();
        String pDetail = post.get(position).getpDetail();
        String pImage = post.get(position).getpImage();
        String pTimeStamp = post.get(position).getpTime();
        Log.d("pTitle",pTitle);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        //holder.ivuName.setText(name);
        holder.pTime.setText(pTime);
        holder.pTitle.setText(pTitle);
        holder.pDetail.setText(pDetail);



        if (pImage.equals("noImage")){
            holder.ivpImage.setVisibility(View.GONE);
        }else{
            try{
                Picasso.get().load(pImage).into(holder.ivpImage);
            }catch (Exception e){}
        }

        holder.btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PostDetailActivity.class);
                intent.putExtra("pid",pid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    public class PostAdapter extends RecyclerView.ViewHolder{
        ImageView ivuICON, ivpImage;
        TextView ivuName, pTitle, pDetail,pLike,pTime;
        //ImageButton btnmore;
        Button btnlike,btncomment,btnshare;

        public PostAdapter(View view){
            super(view);

            ivuICON = view.findViewById(R.id.uIcon);
            ivpImage = view.findViewById(R.id.pImage);
            ivuName = view.findViewById(R.id.uName);
            pTitle = view.findViewById(R.id.pTitle);
            pDetail = view.findViewById(R.id.pdetail);
            btncomment = view.findViewById(R.id.btnComment);
            pTime = view.findViewById(R.id.pTimeTv);



    }


}
}


