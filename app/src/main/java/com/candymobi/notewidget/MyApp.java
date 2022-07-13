package com.candymobi.notewidget;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author xuchuanting
 * Create on 2020/9/27 10:46
 */
public class MyApp extends Application {

    private static MyApp sMyApp;

    public static MyApp getInstance() {
        return sMyApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApp = this;
        IntentFilter filter = new IntentFilter(getWidgetAction());
        registerReceiver(new EditBR(), filter);
    }

    public String getWidgetAction(){
        return EditWidgetActivity.class.getName();
    }

    private static class EditBR extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra(Const.WIDGET_ID, -1);
            Intent intent1 = new Intent(context, EditWidgetActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra(Const.WIDGET_ID, intExtra);
            context.startActivity(intent1);
        }
    }
}
