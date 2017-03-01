package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Amenity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainAreaListResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetAllAmenities extends AsyncTask<Void, Void, MainAreaListResponse> {

    private HomeActivity activity;

    public GetAllAmenities(HomeActivity activity) {

        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected MainAreaListResponse doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        MainAreaListResponse amenitiesResponse = new MainAreaListResponse();

        try {

            amenitiesResponse = builder.build().areas().amenities().all(RequestsModule.LANGUAGE).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return amenitiesResponse;
    }

    @Override
    protected void onPostExecute(MainAreaListResponse amenitiesResponse) {

        activity.saveAmenities(amenitiesResponse);
        activity.updateProgress(true);
    }
}