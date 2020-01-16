package com.example.moviecataloguefinalproject.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.moviecataloguefinalproject.R;
import com.example.moviecataloguefinalproject.activity.MainActivity;
import com.example.moviecataloguefinalproject.model.ListData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TYPE_DAILY_REMINDER = "dailyReminder";
    public static final String TYPE_RELEASE_REMINDER = "releaseReminder";

    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";

    private final static int ID_DAILY_REPEATING = 101;
    private final static int ID_RELEASE_REPEATING = 102;

    String title, message;
    int notifId;

    int idNotification = 0;

    private static final int MAX_NOTIFICATION = 5;

    private final static String GROUP_KEY_EMAILS = "group_key_emails";

    String release;

    ArrayList<ListData> arrayList = new ArrayList<>();

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);

        assert type != null;
        if (type.equalsIgnoreCase(TYPE_DAILY_REMINDER)) {
            title = "Daily Reminder";
            message = intent.getStringExtra(EXTRA_MESSAGE);
            notifId = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REPEATING : ID_RELEASE_REPEATING;
            showAlarmNotification(context, title, message, notifId);
        } else if (type.equalsIgnoreCase(TYPE_RELEASE_REMINDER)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            final String todayDate = simpleDateFormat.format(date);

            AsyncHttpClient client = new AsyncHttpClient();

            String API_KEY = "fb0e5ff27afe26ed13f939e837260424";
            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + todayDate + "&primary_release_date.lte=" + todayDate;

            Log.d("getDataFromAPI", "getDataFromApi");

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);

                    Log.d("data", result);

                    try {
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            ListData listData = new ListData();
                            listData.setTitle(list.getJSONObject(i).getString("title"));
                            listData.setReleaseDate(list.getJSONObject(i).getString("release_date"));

                            release = listData.getReleaseDate();
                            title = listData.getTitle();

                            arrayList.add(listData);

                            if (release.equals(todayDate)) {
                                String message = title + " has been release today";
                                sendNotif(context, title, message);
                                idNotification++;
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                }
            });
        }
    }

    private void showAlarmNotification(Context context, String title, String message,
                                       int notifId) {
        String CHANNEL_ID = "channel_01";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_search)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(notifId, notification);
        }
    }

    public void setDailyReminder(Context context, String type, String message) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REPEATING, intent, 0);
        if (alarmManager != null) {
            Log.d("setDailyReminder", "setDailyReminder");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelDailyReminder(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REPEATING : ID_RELEASE_REPEATING;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            Log.d("cancelSetDailyReminder", "cancelSetDailyReminder");
            alarmManager.cancel(pendingIntent);
        }
    }

    public void setReleaseDateTodayReminderAlarm(Context context, String type) {
        // Buat alarm manager object
        AlarmManager releaseTodayAlarmReminder = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent releaseTodayReminderIntent = new Intent(context, AlarmReceiver.class);
        releaseTodayReminderIntent.putExtra(EXTRA_TYPE, type);

        // Set time to 8AM
        Calendar releaseTodayReminderClock = Calendar.getInstance();
        releaseTodayReminderClock.set(Calendar.HOUR_OF_DAY, 8);
        releaseTodayReminderClock.set(Calendar.MINUTE, 0);
        releaseTodayReminderClock.set(Calendar.SECOND, 0);

        // Create pending intent dengan membawa object Intent yg disebut agar dapat trigger broadcast
        PendingIntent releaseTodayPendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REPEATING, releaseTodayReminderIntent, 0);

        if (releaseTodayAlarmReminder != null) {
            Log.d("setReleaseReminder", "setReleaseReminder");
            releaseTodayAlarmReminder.setInexactRepeating(AlarmManager.RTC_WAKEUP, releaseTodayReminderClock.getTimeInMillis(), AlarmManager.INTERVAL_DAY, releaseTodayPendingIntent);
        }

    }

    // Method ini berguna untuk cancel alarm yg ada di AlarmManager
    public void cancelReleaseDateTodayAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE_REMINDER) ? ID_RELEASE_REPEATING : ID_DAILY_REPEATING;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            Log.d("cancelReleaseReminder", "cancelReleaseReminder");
            alarmManager.cancel(pendingIntent);
        }
    }

    private void sendNotif(Context context, String title, String message) {
        String CHANNEL_NAME = "ReleaseAlarm channel";
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_search);
        NotificationCompat.Builder mBuilder;

        //Melakukan pengecekan jika idNotification lebih kecil dari Max Notif
        String CHANNEL_ID = "channel_01";
        if (idNotification < MAX_NOTIFICATION) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_search)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(title)
                    .addLine(message)
                    .setBigContentTitle(title)
                    .setSummaryText(message);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_search)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }
         /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(idNotification, notification);
        }
    }
}
