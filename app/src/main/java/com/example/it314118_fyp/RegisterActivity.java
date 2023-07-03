package com.example.it314118_fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.it314118_fyp.viewController.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout mDisplayName,mEmail,mPassword;
    private Button mCreateBtn;

    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegProgress=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mDisplayName=findViewById(R.id.reg_display_name);
        mEmail=findViewById(R.id.reg_email);
        mPassword=findViewById(R.id.reg_password);
        mCreateBtn=findViewById(R.id.reg_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();

                if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(password)&&TextUtils.isEmpty(display_name)){
                    Toast.makeText(RegisterActivity.this, "Please Enter Data",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Please Enter Email Address",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Please Enter the Password",Toast.LENGTH_SHORT).show();
                }else if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    Log.v("display_name",display_name);
                    if(display_name.isEmpty()){
                        display_name=email;
                    }
                    register_user(display_name,email,password);
                }
            }
        });
    }

    private void register_user(String display_name,String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();

                            mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",display_name);
                            userMap.put("status","Hi there I'm Here.");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgress.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            mRegProgress.hide();
                            Log.w("createAC","createUserWithEmail:failure",task.getException());
                            Toast.makeText(RegisterActivity.this,"create failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
