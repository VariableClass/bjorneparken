package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Species;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */
public class CheckConnectionTask extends AsyncTask<Void, Void, Boolean> {

    //region Properties

    private String mRootUrl;
    private HomeActivity mActivity;

    //endregion Properties

    //region Constructors

    public CheckConnectionTask(String rootUrl, HomeActivity activity) {

        mRootUrl = rootUrl;
        mActivity = activity;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, Boolean> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try{

            URL root = new URL(mRootUrl);
            URLConnection connection = root.openConnection();
            connection.setConnectTimeout(30000);
            connection.connect();
            return true;

        } catch (Exception e) {

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isAvailable) {

        mActivity.setServerAvailability(isAvailable);
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, Boolean> Overridden Methods

    //endregion Methods
}