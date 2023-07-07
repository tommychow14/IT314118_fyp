package com.example.it314118_fyp.posts;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.it314118_fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class uploadpost extends AppCompatActivity {
    private EditText inputImageAbout;
    private ProgressBar progressBar;
    private TextView textViewProgress;
    boolean isImageAdded=false;
    private Uri imageUri;
    private Button btnUpload;
    private Button btn_back;
    private Handler handler;
    private RelativeLayout rlContent;
    private Animator animator;
    private String topLabel;




    private final DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("Items");
    private final StorageReference StorageRef= FirebaseStorage.getInstance().getReference().child("ItemsImage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadposts);

        //ImageView im_upload = findViewById(R.id.img_upload);
        //btnUpload = findViewById(R.id.btn_Upload);
        //inputImageAbout = findViewById(R.id.edit_About);
        //progressBar = findViewById(R.id.progressBar);
        //textViewProgress = findViewById(R.id.textViewProgress);
        //rlContent=findViewById(R.id.rl_content);
        //btn_back=findViewById(R.id.btn_post_back);

        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        rlContent.getBackground().setAlpha(0);
        handler=new Handler();
    }

    private void uploadImage(final String uid, final String imageAbout) {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key = Dataref.push().getKey();
        final DatabaseReference database = Dataref.child(key);

        StorageRef.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Userid",uid);
                        hashMap.put("About",imageAbout);
                        hashMap.put("ImageUrl",uri.toString());
                        hashMap.put("ptype",uid+"-"+topLabel);


                        database.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(uploadpost.this, "Successfully Uploaded!", Toast.LENGTH_LONG).show();
                            }
                        });
                        database.setValue(hashMap).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(uploadpost.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int)progress);
                textViewProgress.setText(progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadpost.this, "Uploading", Toast.LENGTH_LONG).show();
            }
        });
    }
}

