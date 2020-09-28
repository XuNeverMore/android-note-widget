package com.candymobi.notewidget.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
    private static final float RADIUS = 50;

    public static Bitmap convertCircleBitmap(@NonNull Bitmap toTransform, BitmapPool pool) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Bitmap bitmap;
        if (pool != null) {
            bitmap = pool.get(width, height, Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0x00ff0000);
        Paint paint = new Paint();
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        canvas.drawPaint(paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        int size = Math.min(width, height);
//        Path path = new Path();
//        path.addCircle(width / 2f, height / 2f, size / 2f, Path.Direction.CCW);
//        canvas.clipPath(path);
        canvas.drawRoundRect(0, 0, width, height, RADIUS, RADIUS, paint);
        return bitmap;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return convertCircleBitmap(toTransform, pool);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
