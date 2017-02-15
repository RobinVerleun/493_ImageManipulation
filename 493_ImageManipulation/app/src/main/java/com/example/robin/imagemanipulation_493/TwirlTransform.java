package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by robin on 14/02/17.
 */

public class TwirlTransform extends AbstractTransform {

    public TwirlTransform(Context context, Bitmap original) {
        super(context, original);
    }

    public void runFilter() {
        script.invoke_processTwirl(input, output);
    }

}
