package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetVisitorStarredSpecies extends AsyncTask<Void, Void, MainSpeciesListResponse> {

    private HomeActivity activity;
    private long visitorId;

    public GetVisitorStarredSpecies(HomeActivity activity, long visitorId) {

        this.activity = activity;
        this.visitorId = visitorId;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected MainSpeciesListResponse doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        MainSpeciesListResponse starredSpeciesResponse = null;

        try {

            starredSpeciesResponse = builder.build().visitors().starredSpecies().get(visitorId, RequestsModule.LANGUAGE).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }


        return starredSpeciesResponse;
    }

    @Override
    protected void onPostExecute(MainSpeciesListResponse starredSpeciesResponse) {

        if (starredSpeciesResponse != null) {

            activity.saveStarredSpecies(starredSpeciesResponse);

        } else {

            activity.getNewId();
        }

        activity.updateProgress(true);
    }
}