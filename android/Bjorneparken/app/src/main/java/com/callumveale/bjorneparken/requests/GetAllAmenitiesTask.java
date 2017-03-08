package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainAreaListResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetAllAmenitiesTask extends AsyncTask<Void, Void, MainAreaListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;

    //endregion Properties

    //region Constructors

    public GetAllAmenitiesTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, MainAreaListResponse> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected MainAreaListResponse doInBackground(Void... params) {

        MainAreaListResponse amenitiesResponse = new MainAreaListResponse();

        try {

            amenitiesResponse = mBuilder.build().areas().amenities().all(mLanguage).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return amenitiesResponse;
    }

    @Override
    protected void onPostExecute(MainAreaListResponse amenitiesResponse) {

        if (amenitiesResponse != null) {

            mActivity.saveAmenities(amenitiesResponse);
        }
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainAreaListResponse> Overridden Methods

    //endregion Methods
}