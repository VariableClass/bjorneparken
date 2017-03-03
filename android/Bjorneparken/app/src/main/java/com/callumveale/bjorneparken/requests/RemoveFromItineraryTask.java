package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */
public class RemoveFromItineraryTask extends AsyncTask<Void, Void, Void> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private long mVisitorId;
    private Event mEvent;

    //endregion Properties

    //region Constructors

    public RemoveFromItineraryTask(BjorneparkappenApi.Builder builder, HomeActivity activity, long visitorId, Event event) {

        mBuilder = builder;
        mActivity = activity;
        mVisitorId = visitorId;
        mEvent = event;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, Void> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected Void doInBackground(Void... params) {

        ProtorpcMessagesCombinedContainer request = new ProtorpcMessagesCombinedContainer();
        request.set(RequestsModule.VISITOR_ID, mVisitorId);
        request.set(RequestsModule.EVENT_ID, mEvent.getId());
        request.set(RequestsModule.LOCATION_ID, mEvent.getLocation().getId());

        try {

            mBuilder.build().visitors().itinerary().remove(request).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void param) {

        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, Void> Overridden Methods

    //endregion Methods
}