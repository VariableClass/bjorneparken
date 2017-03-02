package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Species;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */


public class UnstarSpecies extends AsyncTask<Void, Void, Void> {

    private HomeActivity activity;
    private long visitorId;
    private Species species;

    public UnstarSpecies(HomeActivity activity, long visitorId, Species species) {

        this.activity = activity;
        this.visitorId = visitorId;
        this.species = species;
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
        request.set("species_id", this.species.getId());

        try {

            builder.build().visitors().starredSpecies().remove(request).execute();

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