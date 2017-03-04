package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Species;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainEventListResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetAllFeedingsTask extends AsyncTask<Void, Void, MainEventListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;

    //endregion Properties

    //region Constructors

    public GetAllFeedingsTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, MainEventListResponse> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected MainEventListResponse doInBackground(Void... params) {

        MainEventListResponse feedingsResponse = null;

        try {

            feedingsResponse = mBuilder.build().events().feedings().all(mLanguage).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return feedingsResponse;
    }

    @Override
    protected void onPostExecute(MainEventListResponse feedingsResponse) {

        mActivity.saveFeedings(feedingsResponse);

        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainEventListResponse> Overridden Methods

    //endregion Methods
}