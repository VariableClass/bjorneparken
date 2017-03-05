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

                // Retrieve the event start time
                int eventStartHour = Integer.parseInt(event.getStartTime().substring(0, 2));
                int eventStartMinute = Integer.parseInt(event.getStartTime().substring(3, 5));

                // Get exact start date
                Calendar eventStartTime = getEventTime(visitStart, i, eventStartHour, eventStartMinute);

                // Retrieve the event end time
                int eventEndHour = Integer.parseInt(event.getEndTime().substring(0, 2));
                int eventEndMinute = Integer.parseInt(event.getEndTime().substring(3, 5));

                // Get exact end date
                Calendar eventEndTime = getEventTime(visitStart, i, eventEndHour, eventEndMinute);

                // If the event is not over
                if (eventEndTime.getTimeInMillis() > System.currentTimeMillis()) {

                    // Retrieve an intent for the event at it's start time
                    PendingIntent alarmIntent = getStartPendingIntent(context, event, eventStartTime);

                    // Set an alarm for the specified number of minutes prior to the event
                    eventStartTime.add(Calendar.MINUTE, -WARNING_TIME_MINUTES);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, eventStartTime.getTimeInMillis(), alarmIntent);
                }
            }
        }
    }

    private static Calendar getEventTime(Calendar visitStart, int daysDelta, int hour, int minute){

        // Create a new Calendar set to the visit start date
        Calendar eventTime = Calendar.getInstance();
        eventTime.setTime(visitStart.getTime());

        // Add 1 day until we reach the currently processing date
        eventTime.add(Calendar.HOUR, (24 * daysDelta));

        // Set the hour and minute to the event time
        eventTime.set(Calendar.HOUR_OF_DAY, hour);
        eventTime.set(Calendar.MINUTE, minute);

        return eventTime;
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