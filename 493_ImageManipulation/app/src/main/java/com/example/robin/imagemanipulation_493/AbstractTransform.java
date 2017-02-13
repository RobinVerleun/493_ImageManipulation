package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.*;

/**
 * Created by Robin on 2017-02-11.
 */

public abstract class AbstractTransform {



    protected Context context;
    protected Bitmap copy;
    protected RenderScript mRS;
    protected ScriptC_transforms script;
    protected Allocation input, output;


    public AbstractTransform(Context _context, Bitmap original) {

        context = _context;

        copy = original.copy(original.getConfig(), true);
        mRS = RenderScript.create(context);
        script = new ScriptC_transforms(mRS);
        input = Allocation.createFromBitmap(
                mRS,
                copy);
        output = Allocation.createTyped(
                mRS, input.getType(),
                Allocation.USAGE_SCRIPT);
        script.bind_input(input);

    }
//    public AbstractTransform(Context _context, Bitmap original) {
//
//        context = _context;
//
//        copy = original.copy(original.getConfig(), true);
//        mRS = RenderScript.create(context);
//        script = new ScriptC_transforms(mRS);
//
//        input = Allocation.createFromBitmap(mRS, copy);
//        output = Allocation.createTyped(mRS, input.getType());
//
//        script.set_height(original.getHeight());
//        script.set_width(original.getWidth());
//
//    }

    public Bitmap getResult() {
        output.copyTo(copy);
        return copy;
    }

    public abstract void runFilter();
}
