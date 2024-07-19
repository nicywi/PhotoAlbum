package com.nkcdev.photoalbum;

import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.android.material.appbar.MaterialToolbar;

public class UpdateImageActivity extends AppCompatActivity {

    MaterialToolbar toolbarAdd;

    private ImageView imageViewUpdateImage;
    private EditText editTextUpdateTitle, editTextUpdateDescription;
    private Button buttonUpdate;

    private String title, description;
    private byte[] image;
    private int id;

    private Bitmap selectedImage;
    private Bitmap scaledImage;

    //create an object from Activity Result Launcher
    ActivityResultLauncher<Intent> activityResultLauncherUpdateSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_image);
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        toolbarAdd = findViewById(R.id.toolbarUpdate);
        toolbarAdd.setNavigationOnClickListener(v -> {
            finish();
        });

        imageViewUpdateImage = findViewById(R.id.imageViewUpdateImage);
        editTextUpdateTitle = findViewById(R.id.editTextUpdateTitle);
        editTextUpdateDescription = findViewById(R.id.editTextUpdateDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        //register the activity for result, this is mandatory
        registerActivityForUpdateSelectImage();

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        image = getIntent().getByteArrayExtra("image");

        editTextUpdateTitle.setText(title);
        editTextUpdateDescription.setText(description);
        imageViewUpdateImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        imageViewUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //launch
                activityResultLauncherUpdateSelectImage.launch(imageIntent);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

    }

    public void updateData() {
        if (id == -1) {
            Toast.makeText(UpdateImageActivity.this, "There ia a problem!", Toast.LENGTH_SHORT).show();
        } else {
            String updateTitle = editTextUpdateTitle.getText().toString();
            String updateDescription = editTextUpdateDescription.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("updateTitle", updateTitle);
            intent.putExtra("updateDescription", updateDescription);

            if (selectedImage == null) {
                intent.putExtra("image", image);
            } else {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImage = makeSmall(selectedImage, 300);
                scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] image = outputStream.toByteArray();
                intent.putExtra("image", image);
            }

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void registerActivityForUpdateSelectImage() {

        activityResultLauncherUpdateSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data != null) {

                            Uri imageUri = data.getData();

                            try {

                                if (Build.VERSION.SDK_INT >= 28) {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                                    selectedImage = ImageDecoder.decodeBitmap(source);
                                } else {
                                    selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                }

                                imageViewUpdateImage.setImageBitmap(selectedImage);


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