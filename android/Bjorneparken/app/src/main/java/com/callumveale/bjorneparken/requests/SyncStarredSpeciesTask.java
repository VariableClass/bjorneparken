package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.file.ResponseConverter;
import com.callumveale.bjorneparken.models.Species;

import java.io.IOException;
import java.util.ArrayList;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */
public class SyncStarredSpeciesTask extends AsyncTask<Void, Void, MainSpeciesListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;
    private long mVisitorId;
    private ArrayList<Species> mStarredSpecies;

    //endregion Properties

    //region Constructors

    public SyncStarredSpeciesTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language, long visitorId, ArrayList<Species> starredSpecies) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
        mVisitorId = visitorId;
        mStarredSpecies = starredSpecies;
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

        ProtorpcMessagesCombinedContainer request = new ProtorpcMessagesCombinedContainer();
        request.set(RequestsModule.STARRED_SPECIES, ResponseConverter.convertLocalSpeciesList(mStarredSpecies));
        request.set(RequestsModule.VISITOR_ID, this.mVisitorId);
        request.set(RequestsModule.LANGUAGE_CODE, mLanguage);

        MainSpeciesListResponse starredSpeciesResponse = new MainSpeciesListResponse();

        try {

            starredSpeciesResponse = mBuilder.build().visitors().starredSpecies().sync(request).setKey(RequestsModule.API_KEY).execute();

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