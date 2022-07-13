package com.candymobi.notewidget.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

/**
 * @author xuchuanting
 * Create on 2020/9/27 11:45
 */
public class Util {


    public static int getScreenWidth(@NonNull Context context) {
//        return context.getDisplay().getWidth();
        return 0;
    }

    public static int dp2px(Context context,int dp){
        return (int) (context.getResources().getDisplayMetrics().density*dp+0.5);
    }


    public static String getPathFrom(@NonNull Context context, @NonNull Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果document类型是U日，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是普通类型 用普通方法处理
            path = getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果file类型位uri直街获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    @SuppressLint("Range")
    private static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
