package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.fragments.ListFragment;
import com.callumveale.bjorneparken.models.Species;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetAllSpecies extends AsyncTask<Void, Void, MainSpeciesListResponse> {

    private HomeActivity activity;

    public GetAllSpecies(HomeActivity activity) {

        this.activity = activity;
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

        MainSpeciesListResponse speciesListResponse = new MainSpeciesListResponse();

        try {

            speciesListResponse = builder.build().species().all(RequestsModule.LANGUAGE).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return speciesListResponse;
    }

    @Override
    protected void onPostExecute(MainSpeciesListResponse response) {

        activity.saveSpecies(response);
        activity.updateProgress(true);
    }
}