package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainVersionResponse;
import none.bjorneparkappen_api.model.MainVisitorRequest;
import none.bjorneparkappen_api.model.MainVisitorResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetVersion extends AsyncTask<Void, Void, MainVersionResponse> {

    private HomeActivity activity;

    public GetVersion(HomeActivity activity) {

        this.activity = activity;
    }

    @Override
    protected MainVersionResponse doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        MainVersionResponse versionResponse = new MainVersionResponse();

        try {

            versionResponse = builder.build().version().get().setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return versionResponse;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected void onPostExecute(MainVersionResponse response) {

        activity.checkVersion(response.getVersion());
    }
}