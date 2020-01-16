package com.example.moviecataloguefinalproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.service.StackWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "com.example.moviecataloguefinalproject.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.moviecataloguefinalproject.EXTRA_ITEM";
    public static final String UPDATE_ITEM = "com.example.moviecataloguefinalproject.UPDATE_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteMovieWidget.class);
        toastIntent.setAction(FavoriteMovieWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                String viewIndex = intent.getStringExtra(EXTRA_ITEM);
                Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
            }

            if (intent.getAction().equals(UPDATE_ITEM)){
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] restart = appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoriteMovieWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(restart, R.id.stack_view);
            }
        }
        super.onReceive(context, intent);
    }
}

