package com.candymobi.notewidget.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @author xuchuanting
 * Create on 2020/9/28 13:56
 */
public class CircleTransformation extends BitmapTransformation {
    private float radius = 30;

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        final Bitmap bitmap = pool.get(outWidth, outHeight, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0,0,outWidth,outHeight,radius,radius,paint);
        return toTransform;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
