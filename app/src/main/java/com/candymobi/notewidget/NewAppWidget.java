package com.candymobi.notewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.candymobi.notewidget.utils.CircleTransformation;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String ACTION_REQUEST_PLACE = "action_request_place";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        final NoteManager noteManager = NoteManager.getInstance();
        CharSequence widgetText = (CharSequence) noteManager.getNoteContent();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        final String bgPath = noteManager.getBgPath();
        Glide.with(MyApp.getMyApp())
                .asBitmap()
                .transform(new CircleTransformation())
                .load(bgPath)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        views.setImageViewBitmap(R.id.iv_bg, resource);
                        return false;
                    }
                }).preload();

        views.setTextViewText(R.id.appwidget_text, widgetText);
        PendingIntent pi = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.rl_root, pi);

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

