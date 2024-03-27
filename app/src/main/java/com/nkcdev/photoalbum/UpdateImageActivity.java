package com.nkcdev.photoalbum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateImageActivity extends AppCompatActivity {

    private ImageView imageViewUpdateImage;
    private EditText editTextUpdateTitle;
    private EditText editTextUpdateDescription;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);

        getSupportActionBar().setTitle("Add Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_image);

        imageViewUpdateImage = findViewById(R.id.imageViewUpdateImage);
        editTextUpdateTitle  = findViewById(R.id.editTextUpdateTitle);
        editTextUpdateDescription  = findViewById(R.id.editTextUpdateDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        imageViewUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}