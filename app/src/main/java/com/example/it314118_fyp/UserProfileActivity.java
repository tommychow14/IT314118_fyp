package com.example.it314118_fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.it314118_fyp.viewController.login.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private ImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private Button mStatusbtn,mImagebtn,mLogoutbtn;
    private ProgressDialog mProgressDialog;

    private static final int GALLERY_PICK=1;
    private StorageReference mImageStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        mDisplayImage=findViewById(R.id.settings_image);
        mName=findViewById(R.id.settings_name);
        mStatus=findViewById(R.id.settings_status);

        mStatusbtn=findViewById(R.id.settings_nameNstatus_btn);
        mImagebtn=findViewById(R.id.settings_image_btn);
        mLogoutbtn=findViewById(R.id.settings_logout_btn);

        mImageStorage= FirebaseStorage.getInstance().getReference();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();

        String current_uid=mCurrentUser.getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                mName.setText(name);
                mStatus.setText(status);

                if (!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.resource_default).into(mDisplayImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mStatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_value = mName.getText().toString();
                String status_value = mStatus.getText().toString();
                Intent intent =
                        new Intent(UserProfileActivity.this,
                                StatusActivity.class);
                intent.putExtra("status", status_value);
                intent.putExtra("name", name_value);
                startActivity(intent);
            }
        });

        mImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        mLogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                signInAnonymously();
            }
        });
    }


    private void signInAnonymously() {
        finish();
        Intent i= new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK){

            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setTitle("uploading Image");
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            // user selects some Image
            try {
                Uri imageURL = data.getData();

                String current_User_id=mCurrentUser.getUid();
                StorageReference filepath=mImageStorage.child("profile_image").child(current_User_id+".jpg");
                UploadTask uploadTask = filepath.putFile(imageURL);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String downloadURL = downloadUri.toString();
                            mUserDatabase.child("image").setValue(downloadURL);
                            mProgressDialog.dismiss();
                            Toast.makeText(UserProfileActivity.this,"Upload ok.",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this,"Error in upload.",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } catch (Exception e) {
                mProgressDialog.dismiss();
                e.printStackTrace();
            }
        }
    }
}