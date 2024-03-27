package com.nkcdev.photoalbum;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyImagesDao myImagesDao;

    private LiveData<List<MyImages>> imagesList;

    ExecutorService executors = Executors.newSingleThreadExecutor();

    public MyImagesRepository(Application application) {

        MyImagesDatabase database = MyImagesDatabase.getInstance(application);
        myImagesDao = database.myImagesDao();
        imagesList = myImagesDao.getAllImages();
    }

    public void insert(MyImages myImages) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.insert(myImages);
            }
        });

    }

    public void update(MyImages myImages) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.update(myImages);
            }
        });
    }

    public void delete(MyImages myImages) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.delete(myImages);
            }
        });
    }

    public LiveData<List<MyImages>> getAllImages() {
        return imagesList;
    }
}
