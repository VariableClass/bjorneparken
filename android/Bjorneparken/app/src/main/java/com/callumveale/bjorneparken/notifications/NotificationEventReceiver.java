package com.callumveale.bjorneparken.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.callumveale.bjorneparken.models.Event;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by callum on 05/03/2017.
 */
public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";
    private static final String EVENT = "EVENT";
    public static final int WARNING_TIME_MINUTES = 15;

    private static ArrayList<Event> mEvents;

    public static void setupAlarm(Context context, ArrayList<Event> events) {

        mEvents = events;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (Event event : events){

            int eventStartHour = Integer.parseInt(event.getStartTime().substring(0, 2));
            int eventStartMinute = Integer.parseInt(event.getStartTime().substring(3, 5));

            Calendar eventTime = Calendar.getInstance();
            eventTime.set(eventTime.get(Calendar.YEAR), eventTime.get(Calendar.MONTH), eventTime.get(Calendar.DATE), eventStartHour, eventStartMinute);
            eventTime.add(Calendar.MINUTE, -WARNING_TIME_MINUTES);

            PendingIntent alarmIntent = getStartPendingIntent(context, event);

            alarmManager.set(AlarmManager.RTC_WAKEUP, eventTime.getTimeInMillis(), alarmIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context, (Event) intent.getExtras().get(EVENT));
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    private static PendingIntent getStartPendingIntent(Context context, Event event) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        Bundle args = new Bundle();
        args.putParcelable(EVENT, event);
        intent.putExtras(args);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}