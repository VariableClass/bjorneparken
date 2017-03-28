package com.callumveale.bjorneparken.notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;

/**
 * Created by callum on 05/03/2017.
 */
public class NotificationIntentService extends IntentService {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String EVENT = "EVENT";
    public static final String START_TIME = "START_TIME";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context, int notificationId, Event event, long startTime) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        Bundle args = new Bundle();
        args.putInt(NOTIFICATION_ID, notificationId);
        args.putParcelable(EVENT, event);
        args.putLong(START_TIME, startTime);
        intent.putExtras(args);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        Bundle args = new Bundle();
        args.putInt(NOTIFICATION_ID, notificationId);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification((int) intent.getExtras().get(NOTIFICATION_ID), (Event) intent.getExtras().get(EVENT), intent.getExtras().getLong(START_TIME));
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {

        // Cancel any future notification showings
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent.getExtras() != null) {

            manager.cancel((int) intent.getExtras().get(NOTIFICATION_ID));
        }

    }

    private void processStartNotification(final int notificationId, Event event, long startTime) {

        final Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);

        // Calculate the number of minutes remaining
        long difference = (long)((float)(startTime - System.currentTimeMillis()) / 60000);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(event.getHeader())
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setColor(getApplication().getResources().getColor(R.color.primary))
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(icon)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.FLAG_SHOW_LIGHTS)
                .setLights(0xffc0cb, 1000, 1000)
                .setVibrate(new long[] { 0, 100, 30, 100, 200, 500 })
                .setContentText(String.format(getString(R.string.event_warning), difference));

        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                notificationId,
                new Intent(this, HomeActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this, notificationId));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(notificationId, builder.build());

        final NotificationCompat.Builder repeatBuilder = new NotificationCompat.Builder(this);
        final Event repeatEvent = event;
        final long startTimeMillis = startTime;

        // Start a background thread to update the time to event
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        // Whilst the event has not started
                        while (System.currentTimeMillis() < startTimeMillis){

                            // Calculate the number of minutes remaining
                            long difference = (long)((float)(startTimeMillis - System.currentTimeMillis()) / 60000);

                            if (difference > 0) {

                                // Update the text and renotify
                                repeatBuilder.setContentTitle(repeatEvent.getHeader())
                                        .setOngoing(true)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setAutoCancel(false)
                                        .setColor(getApplication().getResources().getColor(R.color.primary))
                                        .setSmallIcon(R.drawable.notification_icon)
                                        .setLargeIcon(icon)
                                        .setContentText(String.format(getString(R.string.event_warning), difference))
                                        .setContentIntent(pendingIntent)
                                        .setDeleteIntent(NotificationEventReceiver.getDeleteIntent(getApplicationContext(), notificationId));
                                manager.notify(notificationId, repeatBuilder.build());

                                try {
                                    // Wait 1 minute
                                    Thread.sleep(60000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        // Update the text and renotify
                        repeatBuilder.setContentTitle(repeatEvent.getHeader())
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setOngoing(false)
                                .setAutoCancel(true)
                                .setColor(getApplication().getResources().getColor(R.color.primary))
                                .setSmallIcon(R.drawable.notification_icon)
                                .setLargeIcon(icon)
                                .setContentText(getString(R.string.event_now));
                        manager.notify(notificationId, repeatBuilder.build());
                    }
                }
        ).start();
    }
}
