package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainVisitorRequest;
import none.bjorneparkappen_api.model.MainVisitorResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetVisitorId extends AsyncTask<Void, Void, MainVisitorResponse> {

    private HomeActivity activity;
    private DateTime visitStart;
    private DateTime visitEnd;

    public GetVisitorId(HomeActivity activity, DateTime visitStart, DateTime visitEnd) {

        this.activity = activity;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
    }

    @Override
    protected MainVisitorResponse doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        MainVisitorRequest visitorRequest = new MainVisitorRequest().setVisitStart(visitStart).setVisitEnd(visitEnd);
        MainVisitorResponse visitorIdResponse = new MainVisitorResponse();

        try {

            visitorIdResponse = builder.build().visitors().create(visitorRequest).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return visitorIdResponse;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected void onPostExecute(MainVisitorResponse response) {

        activity.updateProgress(true);

        activity.setId(response.getId());
    }
}