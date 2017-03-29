package com.callumveale.bjorneparken.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.callumveale.bjorneparken.file.FileWriter;
import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created by callum on 05/03/2017.
 */
public class NotificationEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";
    public static final int WARNING_TIME_MINUTES = 15;

    public static void setupAlarm(Context context, ArrayList<Event> events, DateTime visitStartDate, DateTime visitEndDate) {

        FileWriter fileWriter = new FileWriter(context);
        ArrayList<Notification> notifications = fileWriter.getNotificationsFromFile();

        // Retrieve system alarm service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Convert passed dates to calendars
        Calendar visitStart = Calendar.getInstance();
        visitStart.setTimeInMillis(visitStartDate.getValue());

        Calendar visitEnd = Calendar.getInstance();
        visitEnd.setTimeInMillis(visitEndDate.getValue());

        // Calculate the days between the two calendars
        long daysBetween = TimeUnit.MILLISECONDS.toDays(Math.abs(visitEnd.getTimeInMillis() - visitStart.getTimeInMillis()));

        int eventCount = 1;

        // For each day in between (runs once if 0 days difference [same day])
        for (int i = 0; i == daysBetween; i++){

            // For each event
            for (Event event : events){

                // Retrieve exact start time
                Calendar startTime = event.getEventStartCalendar(visitStart, i);

                // If the event has not started/is up to 5 minutes in
                if (System.currentTimeMillis() < startTime.getTimeInMillis() + 300000) {

                    // If event currently has notification
                    for (Notification notification : notifications){

                        if ((notification.getAreaId() == event.getLocation().getId()) && (notification.getEventId() == event.getId())) {

                            // Retrieve an intent for the event at it's start time
                            PendingIntent alarmIntent = getStartPendingIntent(context, notification.getNotificationId(), notification.getAreaId(), notification.getEventId(), notification.getStartTime());

                            // Cancel the alarm
                            alarmManager.cancel(alarmIntent);

                            // Remove notification from list
                            notifications.remove(notification);
                            break;
                        }
                    }

                    // Create new notification and add it to the list of notifications
                    Notification notification = new Notification(eventCount, event.getLocation().getId(), event.getId(), startTime.getTimeInMillis());
                    notifications.add(notification);

                    // Retrieve an intent for the event at it's start time
                    PendingIntent alarmIntent = getStartPendingIntent(context, notification.getNotificationId(), notification.getAreaId(), notification.getEventId(), notification.getStartTime());

                    // Set an alarm for the specified number of minutes prior to the event
                    startTime.add(Calendar.MINUTE, -WARNING_TIME_MINUTES);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), alarmIntent);
                }

                eventCount++;
            }
        }

        fileWriter.writeNotificationsToFile(notifications);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {

            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context, (int) intent.getExtras().get(NotificationIntentService.NOTIFICATION_ID), (long) intent.getExtras().get(NotificationIntentService.AREA_ID), (long) intent.getExtras().get(NotificationIntentService.EVENT_ID), intent.getExtras().getLong(NotificationIntentService.START_TIME));
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {

            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context, (int) intent.getExtras().get(NotificationIntentService.NOTIFICATION_ID));
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    private static PendingIntent getStartPendingIntent(Context context, int notificationId, long areaId, long eventId, long eventStartTime) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        Bundle args = new Bundle();
        args.putInt(NotificationIntentService.NOTIFICATION_ID, notificationId);
        args.putLong(NotificationIntentService.AREA_ID, areaId);
        args.putLong(NotificationIntentService.EVENT_ID, eventId);
        args.putLong(NotificationIntentService.START_TIME, eventStartTime);
        intent.putExtras(args);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        intent.putExtra(NotificationIntentService.NOTIFICATION_ID, notificationId);
        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

