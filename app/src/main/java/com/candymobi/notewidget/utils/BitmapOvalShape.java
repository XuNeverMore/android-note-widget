package com.candymobi.notewidget.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.shapes.OvalShape;

/**
 * @author xuchuanting
 * Create on 2020/9/28 15:11
 */
public class BitmapOvalShape extends OvalShape {

    private Bitmap mBitmap;

    public BitmapOvalShape(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        super.draw(canvas, paint);
    }
}
