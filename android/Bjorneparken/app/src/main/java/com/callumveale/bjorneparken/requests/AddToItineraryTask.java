package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;

import java.io.IOException;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.ProtorpcMessagesCombinedContainer;

/**
 * Created by callum on 27/02/2017.
 */
public class AddToItineraryTask extends AsyncTask<Void, Void, MainEventListResponse> {

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;
    private long mVisitorId;
    private Event mEvent;

    //endregion Properties

    //region Constructors

    public AddToItineraryTask(BjorneparkappenApi.Builder builder, HomeActivity activity, String language, long visitorId, Event event) {

        mBuilder = builder;
        mActivity = activity;
        mLanguage = language;
        mVisitorId = visitorId;
        mEvent = event;
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

        ProtorpcMessagesCombinedContainer request = new ProtorpcMessagesCombinedContainer();
        request.set(RequestsModule.VISITOR_ID, this.mVisitorId);
        request.set(RequestsModule.EVENT_ID, this.mEvent.getId());
        request.set(RequestsModule.LOCATION_ID, this.mEvent.getLocation().getId());
        request.set(RequestsModule.LANGUAGE_CODE, mLanguage);

        MainEventListResponse itineraryResponse = new MainEventListResponse();

        try {

            itineraryResponse = mBuilder.build().visitors().itinerary().add(request).setKey(RequestsModule.API_KEY).execute();

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