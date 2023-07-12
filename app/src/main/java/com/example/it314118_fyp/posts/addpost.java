package com.example.it314118_fyp.posts;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.it314118_fyp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class addpost extends AppCompatActivity {
    private EditText mETTitle,mETDetail;
    private ImageView mIVPost;
    private Button btnUPload;
    private FirebaseAuth mAuth;
    private String uid;
    private Uri imgUri = null;
    private ProgressDialog pd;
    private Toolbar mChatToolbar;

    private final DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("Posts");
    private final StorageReference StorageRef= FirebaseStorage.getInstance().getReference().child("PostImage");

    //private String username,uid,it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadposts);

        mAuth= FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        mChatToolbar=findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Upload Post");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        pd = new ProgressDialog(this);

        mETTitle = findViewById(R.id.etTitle);
        mETDetail = findViewById(R.id.etdetail);

        mIVPost = findViewById(R.id.ivPost);

        btnUPload = findViewById(R.id.btnuploadpost);

        mIVPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        btnUPload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String title = mETTitle.getText().toString().trim();
                String detail = mETDetail.getText().toString().trim();

                if (TextUtils.isEmpty(title)){
                    Toast.makeText(addpost.this,"Enter title...",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(detail)){
                    detail = " ";
                }
                if(imgUri == null){
                    uploadData(title,detail,null);
                }else{
                    uploadData(title,detail,String.valueOf(imgUri));
                }
            }
        });

    }

    private void uploadData(String title, String detail, String imageurl) {
        pd.setMessage("Uploading post...");
        pd.show();

        String timeStamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference user_message_push=Dataref.push();
        String push_id=user_message_push.getKey();
        if (imageurl!=null){
            StorageReference StorageRef_image = StorageRef.child("post_"+timeStamp);
            StorageRef_image.putFile(Uri.parse(imageurl))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            String downloadUrl = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){

                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("pId",push_id);
                                hashMap.put("pTitle",title);
                                hashMap.put("pDetail",detail);
                                hashMap.put("pImage",downloadUrl);
                                hashMap.put("pTime",timeStamp);

                                Dataref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pd.dismiss();
                                                Toast.makeText(addpost.this,"Post uploaded",Toast.LENGTH_SHORT).show();

                                                mETTitle.setText("");
                                                mETDetail.setText("");
                                                mIVPost.setImageURI(null);
                                                imgUri = null;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(addpost.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                        }
                    });
        }else{
            HashMap<Object, String> hashMap = new HashMap<>();

            hashMap.put("uid",uid);
            hashMap.put("pId",push_id);
            hashMap.put("pTitle",title);
            hashMap.put("pDetail",detail);
            hashMap.put("pImage","noImage");
            hashMap.put("pTime",timeStamp);

            Dataref.child(push_id).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(addpost.this,"Post uploaded",Toast.LENGTH_SHORT).show();

                            mETTitle.setText("");
                            mETDetail.setText("");
                            mIVPost.setImageURI(null);
                            imgUri = null;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(addpost.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void imagePicker() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //Image Uri will not be null for RESULT_OK
            imgUri = data.getData();

            // Use Uri object instead of File to avoid storage permissions
            mIVPost.setImageURI(imgUri);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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

