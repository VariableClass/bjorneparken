package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Species;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */
public class UnstarSpeciesTask extends AsyncTask<Void, Void, Void> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private long mVisitorId;
    private Species mSpecies;

    //endregion Properties

    //region Constructors

    public UnstarSpeciesTask(BjorneparkappenApi.Builder builder, HomeActivity activity, long visitorId, Species species) {

        mBuilder = builder;
        mActivity = activity;
        mVisitorId = visitorId;
        mSpecies = species;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, Void> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected Void doInBackground(Void... params) {

        ProtorpcMessagesCombinedContainer request = new ProtorpcMessagesCombinedContainer();
        request.set(RequestsModule.VISITOR_ID, mVisitorId);
        request.set(RequestsModule.SPECIES_ID, mSpecies.getId());

        try {

            mBuilder.build().visitors().starredSpecies().remove(request).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void param) {

        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, Void> Overridden Methods

    //endregion Methods
}