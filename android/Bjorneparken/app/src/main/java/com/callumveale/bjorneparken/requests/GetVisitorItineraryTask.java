package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainEventListResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetVisitorItineraryTask extends AsyncTask<Void, Void, MainEventListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;
    private long mVisitorId;

    //endregion Properties

    //region Constructors

    public GetVisitorItineraryTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language, long visitorId) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
        mVisitorId = visitorId;
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

        MainEventListResponse itineraryResponse = null;

        try {

            itineraryResponse = mBuilder.build().visitors().itinerary().get(mVisitorId, mLanguage).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return itineraryResponse;
    }

    @Override
    protected void onPostExecute(MainEventListResponse itineraryResponse) {

        if (itineraryResponse != null) {

            mActivity.saveItinerary(itineraryResponse);

        } else {

            mActivity.getNewId();
        }

        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainEventListResponse> Overridden Methods

    //endregion Methods
}