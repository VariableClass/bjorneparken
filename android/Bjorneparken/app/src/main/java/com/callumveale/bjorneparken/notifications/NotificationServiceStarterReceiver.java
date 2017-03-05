package com.callumveale.bjorneparken.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.callumveale.bjorneparken.file.FileWriter;
import com.callumveale.bjorneparken.models.Event;

import java.util.ArrayList;

/**
 * Created by callum on 05/03/2017.
 */
public final class NotificationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieve itinerary from file
        FileWriter fileWriter = new FileWriter(context);
        ArrayList<Event> itinerary = fileWriter.getItineraryFromFile();

        // Update times
        NotificationEventReceiver.setupAlarm(context, itinerary);
    }
}