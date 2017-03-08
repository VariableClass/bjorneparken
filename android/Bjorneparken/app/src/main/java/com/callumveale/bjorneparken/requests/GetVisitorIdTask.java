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
public class GetVisitorIdTask extends AsyncTask<Void, Void, MainVisitorResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private DateTime mVisitStart;
    private DateTime mVisitEnd;

    //endregion Properties

    //region Constructors

    public GetVisitorIdTask(BjorneparkappenApi.Builder builder, HomeActivity activity, DateTime visitStart, DateTime visitEnd) {

        mBuilder = builder;
        mActivity = activity;
        mVisitStart = visitStart;
        mVisitEnd = visitEnd;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, MainVisitorResponse>

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected MainVisitorResponse doInBackground(Void... params) {

        MainVisitorRequest visitorRequest = new MainVisitorRequest().setVisitStart(mVisitStart).setVisitEnd(mVisitEnd);
        MainVisitorResponse visitorIdResponse = new MainVisitorResponse();

        try {

            visitorIdResponse = mBuilder.build().visitors().create(visitorRequest).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return visitorIdResponse;
    }

    @Override
    protected void onPostExecute(MainVisitorResponse response) {

        if (response.getId() != null) {

            mActivity.setId(response.getId());
        }
        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainVisitorResponse>

    //endregion Methods
}