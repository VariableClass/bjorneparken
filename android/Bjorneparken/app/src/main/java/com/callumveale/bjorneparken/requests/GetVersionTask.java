package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainVersionResponse;

/**
 * Created by callum on 27/02/2017.
 */
public class GetVersionTask extends AsyncTask<Void, Void, MainVersionResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;

    //endregion Properties

    //region Constructors

    public GetVersionTask(BjorneparkappenApi.Builder builder, HomeActivity activity) {

        mBuilder = builder;
        mActivity = activity;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, MainVersionResponse> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected MainVersionResponse doInBackground(Void... params) {

        MainVersionResponse versionResponse = new MainVersionResponse();

        try {

            versionResponse = mBuilder.build().version().get().setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return versionResponse;
    }

    @Override
    protected void onPostExecute(MainVersionResponse versionResponse) {

        mActivity.compareVersions(versionResponse.getVersion());
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainVersionResponse> Overridden Methods

    //endregion Methods
}