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
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String ACTION_REQUEST_PLACE = "action_request_place";

    private static final String TAG = "NewAppWidget";

    private static boolean PA = false;

    public static void updateAppWidget(Context context,
                                       int appWidgetId) {
        Log.i(TAG, "updateAppWidget: " + appWidgetId);

        AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);
        final NoteManager noteManager = NoteManager.getInstance();
        CharSequence widgetText = noteManager.getNoteContent(appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        final String bgPath = noteManager.getBgPath(appWidgetId);


        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent it = new Intent(context, NewAppWidget.class);
        it.setAction(MyApp.getInstance().getWidgetAction());
        it.putExtra(Const.WIDGET_ID, appWidgetId);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, it, 0);


//        if (PA) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Const.WIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.rl_root, pendingIntent);
//        } else {
//            Intent intent = new Intent(MyApp.getInstance().getWidgetAction());
//            intent.putExtra(Const.WIDGET_ID, appWidgetId);
//            PendingIntent pi = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//            views.setOnClickPendingIntent(R.id.rl_root, pi);
//        }

        if (TextUtils.isEmpty(bgPath)) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            return;
        }

        Glide.with(MyApp.getInstance())
                .asBitmap()
                .load(bgPath)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            views.setImageViewIcon(Icon.);
//                        } else {
                        views.setImageViewBitmap(R.id.iv_bg, resource);
//                        }
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                        return true;
                    }
                })
//                .transform(new CircleTransformation())
                .preload();


        // Instruct the widget manager to update the widget
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


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetId);
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

