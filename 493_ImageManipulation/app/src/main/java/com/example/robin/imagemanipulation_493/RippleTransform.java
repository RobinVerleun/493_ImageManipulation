package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by robin on 13/02/17.
 */

public class RippleTransform extends AbstractTransform {

    public RippleTransform(Context context, Bitmap original) {
        super(context, original);
    }

    public void runFilter() {
        script.invoke_processRipple(input, output);
    }
}
