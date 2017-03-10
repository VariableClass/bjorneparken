package com.callumveale.bjorneparken.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.callumveale.bjorneparken.config.Configuration;
import com.callumveale.bjorneparken.file.FileWriter;
import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by callum on 05/03/2017.
 */
public final class NotificationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieve itinerary from file
        FileWriter fileWriter = new FileWriter(context);
        ArrayList<Event> itinerary = fileWriter.getItineraryFromFile();
        Configuration config = fileWriter.getConfigFromFile();

        // TODO Retrieve visitor start and end date from file
        DateTime visitStart = new DateTime(new Date()); //fileWriter.getVisitStartFromFile();
        DateTime visitEnd = new DateTime(new Date()); //fileWriter.getVisitEndFromFile();

        // If notifications enabled
        if (Boolean.valueOf(config.getProperty(Configuration.NOTIFICATIONS_ENABLED))) {

            // Update times
            NotificationEventReceiver.setupAlarm(context, itinerary, visitStart, visitEnd);
        }
    }
}