package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by robin on 15/02/17.
 * Note to grader: doInBackground return 1 because.. It was unhappy returning nothing.
 */

public class PhotoTask extends AsyncTask<Object, Integer, Integer> {

    private final WeakReference<ImageView> imageViewRef;
    Context context;
    ImageManager IM;

    public PhotoTask(ImageView _imgview, ImageManager _im) {
        imageViewRef = new WeakReference<ImageView>(_imgview);
        IM = _im;
    }

    @Override
    protected Integer doInBackground(Object... obj) {
        IM.saveImageToMediaGallery();
        return 1;
    }

    @Override
    protected void onPostExecute(Integer i) {
        if(imageViewRef != null) {
            final ImageView imageView = imageViewRef.get();
            if(imageView != null) {
                imageView.setImageBitmap(IM.loadLastStoredImage());
                MainActivity.toggleFab(true);
                return;
            }
        }
    }

    public void showToast(String msg) {;
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
