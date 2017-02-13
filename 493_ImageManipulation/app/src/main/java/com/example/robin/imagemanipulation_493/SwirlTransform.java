package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

/**
 * Created by Robin on 2017-02-11.
 */

public class SwirlTransform extends AbstractTransform {

    public SwirlTransform(Context context, Bitmap original) {
        super(context, original);
    }

    public void runFilter() {
        script.invoke_processSwirl(input, output);
    }

    public void showToast(String str) {
        Toast.makeText(context,
                str, Toast.LENGTH_SHORT).show();
    }
}
