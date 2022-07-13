package com.candymobi.notewidget;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author xuchuanting
 * Create on 2020/9/27 10:46
 */
public class NoteManager {

    private static NoteManager sInstance;
    private static final String NOTE_CONTENT = "note_content";
    private final SharedPreferences mSharedPreferences;

    private NoteManager() {
        mSharedPreferences = MyApp.getInstance().getSharedPreferences("note_list", Context.MODE_PRIVATE);
    }

    public static NoteManager getInstance() {
        if (sInstance == null) {
            sInstance = new NoteManager();
        }
        return sInstance;
    }

    public void saveNote(int widgetId, String string) {
        mSharedPreferences.edit()
                .putString(NOTE_CONTENT + "_" + widgetId, string)
                .apply();
    }


    public String getNoteContent(int widgetId) {
        return mSharedPreferences.getString(NOTE_CONTENT + "_" + widgetId, null);
    }

    public void saveBg(int widgetId, String imagePath) {
        mSharedPreferences.edit().putString("imagePath" + "_" + widgetId, imagePath)
                .apply();
    }

    public String getBgPath(int widgetId) {
        return mSharedPreferences.getString("imagePath" + "_" + widgetId, null);
    }
}
