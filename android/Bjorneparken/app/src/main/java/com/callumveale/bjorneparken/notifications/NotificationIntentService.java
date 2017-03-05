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
import android.util.Log;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;

/**
 * Created by callum on 05/03/2017.
 */
public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    private static final String EVENT = "EVENT";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context, Event event) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        Bundle args = new Bundle();
        args.putParcelable(EVENT, event);
        intent.putExtras(args);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification((Event) intent.getExtras().get(EVENT));
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification(Event event) {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(event.getHeader())
                .setAutoCancel(true)
                .setColor(getApplication().getResources().getColor(R.color.primary))
                .setContentText(String.format(getString(R.string.event_warning), NotificationEventReceiver.WARNING_TIME_MINUTES))
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(icon);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, HomeActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
