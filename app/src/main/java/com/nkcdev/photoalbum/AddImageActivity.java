package com.nkcdev.photoalbum;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.material.appbar.MaterialToolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {

    MaterialToolbar toolbarAdd;

    private ImageView imageViewAddImage;
    private EditText editTextAddTitle, editTextAddDescription;
    private Button buttonSave;

    private Bitmap selectedImage;
    private Bitmap scaledImage;

    //create an object from Activity Result Launcher
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_image);

        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        toolbarAdd = findViewById(R.id.toolbarAdd);
        toolbarAdd.setNavigationOnClickListener(v -> {
            finish();
        });

        imageViewAddImage = findViewById(R.id.imageViewAddImage);
        editTextAddTitle = findViewById(R.id.editTextAddTitle);
        editTextAddDescription = findViewById(R.id.editTextAddDescription);
        buttonSave = findViewById(R.id.buttonSave);

        //register the activity for result, this is mandatory
        registerActivityForSelectImage();

        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String permission;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permission = android.Manifest.permission.READ_MEDIA_IMAGES;
                } else {
                    permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
                }

                if (ContextCompat.checkSelfPermission(AddImageActivity.this
                        , permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddImageActivity.this
                            , new String[]{permission}, 1);
                } else {
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    //launch
                    activityResultLauncherForSelectImage.launch(imageIntent);

                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedImage == null) {
                    Toast.makeText(AddImageActivity.this, "Please select an image!", Toast.LENGTH_SHORT).show();
                } else {
                    String title = editTextAddTitle.getText().toString();
                    String description = editTextAddDescription.getText().toString();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scaledImage = makeSmall(selectedImage, 300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                    byte[] image = outputStream.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    intent.putExtra("image", image);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //launch
            activityResultLauncherForSelectImage.launch(imageIntent);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void registerActivityForSelectImage() {

        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent imageData = result.getData();

                        if (resultCode == RESULT_OK && imageData != null) {

                            Uri imageUri = imageData.getData();

                            try {

                                if (Build.VERSION.SDK_INT >= 28) {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                                    selectedImage = ImageDecoder.decodeBitmap(source);
                                } else {
                                    selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                }

                                imageViewAddImage.setImageBitmap(selectedImage);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });

    }

    public Bitmap makeSmall(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;

        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);

        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}