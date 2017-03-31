package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;

import java.io.IOException;
import java.util.List;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainAmenityResponse;
import none.bjorneparkappen_api.model.MainAreaListResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetAllAreasTask extends AsyncTask<Void, Void, MainAreaListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;

    //endregion Properties

    //region Constructors

    public GetAllAreasTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language) {

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

        MainAreaListResponse areasResponse = new MainAreaListResponse();

        try {

            areasResponse = mBuilder.build().areas().all(mLanguage).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return areasResponse;
    }

    @Override
    protected void onPostExecute(MainAreaListResponse areasResponse) {

        if (areasResponse != null) {

            mActivity.saveAreas(areasResponse);
        }
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainAreaListResponse> Overridden Methods

    //endregion Methods
}