package com.candymobi.notewidget;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author xuchuanting
 * Create on 2020/9/27 10:46
 */
public class NoteManager {

    private static NoteManager sInstance;
    private final String NOTE_CONTENT = "note_content";
    private final SharedPreferences mSharedPreferences;

    private NoteManager() {
        mSharedPreferences = MyApp.getMyApp().getSharedPreferences("note_list", Context.MODE_PRIVATE);
    }

    public static NoteManager getInstance() {
        if (sInstance == null) {
            sInstance = new NoteManager();
        }
        return sInstance;
    }

    public void saveNote(String string) {
        mSharedPreferences.edit()
                .putString(NOTE_CONTENT, string)
                .apply();
    }


    public String getNoteContent() {
        return mSharedPreferences.getString(NOTE_CONTENT, null);
    }

    public void saveBg(String imagePath) {
        mSharedPreferences.edit().putString("imagePath", imagePath)
                .apply();
    }

    public String getBgPath() {
        return mSharedPreferences.getString("imagePath", null);
    }
}
