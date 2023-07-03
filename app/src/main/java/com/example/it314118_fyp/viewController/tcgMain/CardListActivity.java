package com.example.it314118_fyp.viewController.tcgMain;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.it314118_fyp.R;

public class CardListActivity extends AppCompatActivity {

    Button btnOCRscan;
    EditText etResult;

    private static final String TAG = "CARDLIST_TAG";
    private String[] cameraPermissions;
    private Uri imageUri = null;
    private String[] storagePermissions;
    private static final int CAMRA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        getPermissions();
        setupUI();
        setupPopMenu();

    }

    //get Permissions
    private void getPermissions() {
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    //UI init
    private void setupUI() {
        btnOCRscan = findViewById(R.id.btn_Scan);
        etResult = findViewById(R.id.et_result);
    }

    //scan btn popup menu
    private void setupPopMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnOCRscan);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.item_openalbum) {
                    Log.d(TAG, "onMenuItemClick: Album");
                    onClickAlbum();
                } else if (id == R.id.item_opencamera) {
                    Log.d(TAG, "onMenuItemClick: Camera");
                    onClickCamera();
                }
                return false;
            }
        });

        btnOCRscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    private void onClickCamera() {
        if (checkCameraPermission()) {
            pickImageCamera();
        } else {
            requsetCameraPermission();
        }
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                    } else {
                        Toast.makeText(CardListActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void onClickAlbum() {
        if (checkStoragePermission()) {
            pickImageGallery();
        } else {
            requestStoragePermission();
        }
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();

                    } else {
                        Toast.makeText(CardListActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return cameraResult && storageResult;
    }

    private void requsetCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMRA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMRA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(this, "permissions is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickImageGallery();
                    } else {
                        Toast.makeText(this, "permissions is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }
}