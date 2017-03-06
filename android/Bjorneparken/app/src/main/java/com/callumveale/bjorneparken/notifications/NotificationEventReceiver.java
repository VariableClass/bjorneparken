package com.callumveale.bjorneparken.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by callum on 05/03/2017.
 */
public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";
    private static final String EVENT = "EVENT";
    private static final String START_TIME = "START_TIME";
    public static final int WARNING_TIME_MINUTES = 15;

    public static void setupAlarm(Context context, ArrayList<Event> events, DateTime visitStartDate, DateTime visitEndDate) {

        // Retrieve system alarm service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Convert passed dates to calendars
        Calendar visitStart = Calendar.getInstance();
        visitStart.setTimeInMillis(visitStartDate.getValue());

        Calendar visitEnd = Calendar.getInstance();
        visitEnd.setTimeInMillis(visitEndDate.getValue());

        // Calculate the days between the two calendars
        long daysBetween = TimeUnit.MILLISECONDS.toDays(Math.abs(visitEnd.getTimeInMillis() - visitStart.getTimeInMillis()));

        // For each day in between (runs once if 0 days difference [same day])
        for (int i = 0; i == daysBetween; i++){

            // For each event
            for (Event event : events){

                // Retrieve event end time
                Calendar endTime = event.getEventEndCalendar(visitEnd, i);

                // If the event is not over
                if (endTime.getTimeInMillis() > System.currentTimeMillis()) {

                    // Retrieve exact start time
                    Calendar startTime = event.getEventStartCalendar(visitStart, i);

                    // Retrieve an intent for the event at it's start time
                    PendingIntent alarmIntent = getStartPendingIntent(context, event, startTime);

                    // Set an alarm for the specified number of minutes prior to the event
                    startTime.add(Calendar.MINUTE, -WARNING_TIME_MINUTES);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), alarmIntent);
                }
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {

            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context, (Event) intent.getExtras().get(EVENT), intent.getExtras().getLong(START_TIME));
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {

            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    private static PendingIntent getStartPendingIntent(Context context, Event event, Calendar eventStartTime) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        Bundle args = new Bundle();
        args.putParcelable(EVENT, event);
        args.putLong(START_TIME, eventStartTime.getTimeInMillis());
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