package com.example.it314118_fyp.viewController.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.it314118_fyp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;

import java.lang.reflect.Array;

public class CardListActivity extends AppCompatActivity {

    Button btnOCRscan;
    Button btnAddImg;
    EditText etResult;
    ImageView imgScanimg;
    Uri imgUri;
    private TextRecognizer textRecognizer;
    private static final String TAG = "CARDLIST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        setupUI();
        setUpListener();

    }

    //UI init
    private void setupUI() {

        btnOCRscan = findViewById(R.id.btn_Scan);
        etResult = findViewById(R.id.et_result);
        imgScanimg = findViewById(R.id.img_Scanimg);
        btnAddImg = findViewById(R.id.btn_addimg);

        textRecognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());

    }

    private void setUpListener() {
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        btnOCRscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizeText();
            }
        });
    }

    private void recognizeText() {
        if (imgUri == null) {
            Toast.makeText(this, "Pick image first!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "recognizing...", Toast.LENGTH_SHORT).show();
            try {
                InputImage inputImage = InputImage.fromFilePath(this, imgUri);

                Task<Text> textTaskResult = textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                String recognizedTest = text.getText();
                                Log.d(TAG, "onSuccess: " + recognizedTest);
                                String[] ary = recognizedTest.split("\n");
                                for (String a : ary){
                                    Log.d(TAG, "onSuccess: "+a);
                                }
//                                etResult.setText(recognizedTest);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CardListActivity.this, "Fail recognize text:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(this, "Fail input image:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
            Uri uri = data.getData();

            imgUri = uri;

            // Use Uri object instead of File to avoid storage permissions
            imgScanimg.setImageURI(uri);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}