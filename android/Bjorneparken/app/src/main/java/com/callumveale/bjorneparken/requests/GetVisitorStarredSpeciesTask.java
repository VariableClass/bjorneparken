package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetVisitorStarredSpeciesTask extends AsyncTask<Void, Void, MainSpeciesListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;
    private long mVisitorId;

    //endregion Properties

    //region Constructors

    public GetVisitorStarredSpeciesTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language, long visitorId) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
        mVisitorId = visitorId;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, MainSpeciesListResponse> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected MainSpeciesListResponse doInBackground(Void... params) {

        MainSpeciesListResponse starredSpeciesResponse = null;

        try {

            starredSpeciesResponse = mBuilder.build().visitors().starredSpecies().get(mVisitorId, mLanguage).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return starredSpeciesResponse;
    }

    @Override
    protected void onPostExecute(MainSpeciesListResponse starredSpeciesResponse) {

        if (starredSpeciesResponse != null) {

            mActivity.saveStarredSpecies(starredSpeciesResponse);

        } else {

            mActivity.getNewId();
        }

        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainSpeciesListResponse> Overridden Methods

    //endregion Methods
}