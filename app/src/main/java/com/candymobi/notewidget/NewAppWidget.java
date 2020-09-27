package com.candymobi.notewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String ACTION_REQUEST_PLACE = "action_request_place";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = NoteManager.getInstance().getNoteContent();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void requestPlace(Context context) {
        AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                PendingIntent pi = PendingIntent.getBroadcast(context, 100, new Intent(ACTION_REQUEST_PLACE), PendingIntent.FLAG_UPDATE_CURRENT);
                Handler handler = new Handler(context.getMainLooper());
                handler.postDelayed(() -> {
                            try {
//                                UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
//                                userManager.isUserUnlockingOrUnlocked(0);
                                ComponentName componentName = new ComponentName(context, NewAppWidget.class);
                                appWidgetManager.requestPinAppWidget(componentName, null, pi);
                            } catch (IllegalStateException e) {
                            }
                        },
                        300);
            }
        }
    }

    public static boolean hasWidget(Context context) {
        AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);
        ComponentName componentName = new ComponentName(context, NewAppWidget.class);
        return appWidgetManager.getAppWidgetIds(componentName).length > 0;
    }

    public static void update(Context context) {
        AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);
        ComponentName componentName = new ComponentName(context, NewAppWidget.class);

        final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

