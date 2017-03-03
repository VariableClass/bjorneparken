package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetAllSpeciesTask extends AsyncTask<Void, Void, MainSpeciesListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;

    //endregion Properties

    //region Constructors

    public GetAllSpeciesTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
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

        MainSpeciesListResponse speciesResponse = new MainSpeciesListResponse();

        try {

            speciesResponse = mBuilder.build().species().all(mLanguage).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return speciesResponse;
    }

    @Override
    protected void onPostExecute(MainSpeciesListResponse speciesResponse) {

        mActivity.saveSpecies(speciesResponse);
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainSpeciesListResponse> Overridden Methods

    //endregion Methods
}