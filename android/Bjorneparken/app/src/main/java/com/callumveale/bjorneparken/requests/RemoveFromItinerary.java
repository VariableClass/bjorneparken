package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */


public class RemoveFromItinerary extends AsyncTask<Void, Void, Void> {

    private HomeActivity activity;
    private long visitorId;
    private Event event;

    public RemoveFromItinerary(HomeActivity activity, long visitorId, Event event) {

        this.activity = activity;
        this.visitorId = visitorId;
        this.event = event;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected Void doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        ProtorpcMessagesCombinedContainer request = new ProtorpcMessagesCombinedContainer();
        request.set("visitor_id", this.visitorId);
        request.set("event_id", this.event.getId());
        request.set("location_id", this.event.getLocation().getId());

        try {

            builder.build().visitors().itinerary().remove(request).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void param) {


        activity.updateProgress(true);
    }
}