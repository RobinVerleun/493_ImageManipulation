package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by robin on 15/02/17.
 */

public class TransformTask extends AsyncTask<Object, Integer, Bitmap> {

        private final WeakReference<ImageView> imageViewRef;
        Context context;

        public TransformTask(ImageView _imgview, Context parentContext) {
            imageViewRef = new WeakReference<ImageView>(_imgview);
            context = parentContext;
        }

        @Override
        protected Bitmap doInBackground(Object... obj) {
            AbstractTransform mTransform = (AbstractTransform) obj[0];

            mTransform.runFilter();
            return mTransform.getResult();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(imageViewRef != null && result != null) {

                final ImageView imageView = imageViewRef.get();

                if(imageView != null) {
                    imageView.setImageBitmap(result);
                    showToast("Done!");
                    MainActivity.toggleFab(true);
                    return;
                }
            }
            showToast("Error applying filter.");
        }

        public void showToast(String msg) {;
            CharSequence text = msg;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
}
