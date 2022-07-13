package com.candymobi.notewidget;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.candymobi.notewidget.utils.Util;

public class EditWidgetActivity extends AppCompatActivity {

    private ImageView mIvBg;
    private int mWidgetId;

    ActivityResultLauncher<String> photoLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
                String path = Util.getPathFrom(this, result);
                NoteManager.getInstance().saveBg(mWidgetId, path);
                Glide.with(this).load(result).into(mIvBg);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_edit_widget);

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

        //save
        button.setOnClickListener(view -> {
            Editable text = edtContent.getText();
            String string = text.toString();
            noteManager.saveNote(mWidgetId, string);
            NewAppWidget.updateAppWidget(this, mWidgetId);
            finish();
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
        photoLauncher.launch("image/*");
    }

    private boolean hasExternalStoragePermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasExternalStoragePermission()) {
            selectPhoto();
        }
    }
}