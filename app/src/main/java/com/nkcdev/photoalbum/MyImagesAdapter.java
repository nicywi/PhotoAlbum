package com.nkcdev.photoalbum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.MyImagesHolder>{

    List<MyImages> imagesList = new ArrayList<>();

    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);

        return new MyImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImagesHolder holder, int position) {

        MyImages currentImage = imagesList.get(position);
        holder.editTextAddTitle.setText(currentImage.getImage_title());
        holder.editTextAddDescription.setText(currentImage.getImage_description());

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyImagesHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        EditText editTextAddTitle, editTextAddDescription;


        public MyImagesHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewAddImage);
            editTextAddTitle  = itemView.findViewById(R.id.editTextAddTitle);
            editTextAddDescription  = itemView.findViewById(R.id.editTextAddDescription);

        }
    }
}
