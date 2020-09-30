package com.candymobi.notewidget;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 1;
    private SharedPreferences mSharedPreferences;
    private ImageView mIvBg;
    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mWidgetId = intent.getIntExtra(Const.WIDGET_ID, -1);
        if (mWidgetId == -1) {
            return;
        }
        Log.i(NewAppWidget.class.getSimpleName(), "onCreate: " + mWidgetId);
        initView();
    }

    private void initView() {
        EditText edtContent = findViewById(R.id.edit_note);
        Button button = findViewById(R.id.btn_save);
        NoteManager noteManager = NoteManager.getInstance();
        final String content = (String) noteManager.getNoteContent(mWidgetId);
        if (!TextUtils.isEmpty(content)) {
            edtContent.setText(content);
        }

        button.setOnClickListener(view -> {
            Editable text = edtContent.getText();
            String string = text.toString();
            noteManager.saveNote(mWidgetId, string);
            NewAppWidget.updateAppWidget(this, mWidgetId);
        });

        Button btnSetBg = findViewById(R.id.button_set_bg);
        btnSetBg.setOnClickListener(v -> {
            if (hasExternalStoragePermission()) {
                selectPhoto();
            } else {
                requestExternalStoragePermission();
            }
        });

        mIvBg = findViewById(R.id.iv_bg);
        String bgPath = noteManager.getBgPath(mWidgetId);
        if (!TextUtils.isEmpty(bgPath)) {
            Glide.with(this).load(bgPath).into(mIvBg);
        } else {
            mIvBg.setImageDrawable(new ColorDrawable(0xffeeeeee));
        }

    }

    private void requestExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private boolean hasExternalStoragePermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            return;
        }
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果document类型是U日，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是普通类型 用普通方法处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果file类型位uri直街获取图片路径即可
            imagePath = uri.getPath();
        }

        NoteManager.getInstance().saveBg(mWidgetId, imagePath);
        Glide.with(this).load(imagePath).into(mIvBg);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasExternalStoragePermission()) {
            selectPhoto();
        }
    }
}