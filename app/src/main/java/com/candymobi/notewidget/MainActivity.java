package com.candymobi.notewidget;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!NewAppWidget.hasWidget(this)) {
            NewAppWidget.requestPlace(this);
        }

        initView();
    }

    private void initView() {
        EditText edtContent = findViewById(R.id.edit_note);
        Button button = findViewById(R.id.btn_save);
        final String content = NoteManager.getInstance().getNoteContent();
        if (!TextUtils.isEmpty(content)) {
            edtContent.setText(content);
        }

        button.setOnClickListener(view -> {

            Editable text = edtContent.getText();
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String string = text.toString();
            NoteManager.getInstance().saveNote(string);
            NewAppWidget.update(this);
        });
    }


}