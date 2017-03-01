package com.callumveale.bjorneparken.requests;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainVisitorRequest;
import none.bjorneparkappen_api.model.MainVisitorResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetVisitorItinerary extends AsyncTask<Void, Void, MainEventListResponse> {

    private HomeActivity activity;
    private long visitorId;

    public GetVisitorItinerary(HomeActivity activity, long visitorId) {

        this.activity = activity;
        this.visitorId = visitorId;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected MainEventListResponse doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        MainEventListResponse itineraryResponse = null;

        try {

            itineraryResponse = builder.build().visitors().itinerary().get(visitorId, RequestsModule.LANGUAGE).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }


        return itineraryResponse;
    }

    @Override
    protected void onPostExecute(MainEventListResponse itineraryResponse) {

        if (itineraryResponse != null) {

            activity.saveItinerary(itineraryResponse);

        } else {

            activity.getNewId();
        }

        activity.updateProgress(true);
    }
}