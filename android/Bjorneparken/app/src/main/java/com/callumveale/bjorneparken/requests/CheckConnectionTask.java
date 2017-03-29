package com.callumveale.bjorneparken.requests;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Species;

import java.io.IOException;
import java.net.HttpURLConnection;
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

        if (isOnline()) {

            try {

                URL root = new URL(mRootUrl);
                HttpURLConnection connection = (HttpURLConnection) root.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();

                int responseCode = connection.getResponseCode();

                return (responseCode > 200);

            } catch (Exception e) {

                return false;
            }

        } else {

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isAvailable) {

        mActivity.setServerAvailability(isAvailable);
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, Boolean> Overridden Methods

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) mActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //endregion Methods
}