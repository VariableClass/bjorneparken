package com.callumveale.bjorneparken.requests;

import android.os.AsyncTask;
import android.os.Parcelable;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Amenity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainAreaListResponse;

/**
 * Created by callum on 27/02/2017.
 */


public class GetAllAttractions extends AsyncTask<Void, Void, MainAreaListResponse> {

    private HomeActivity activity;

    public GetAllAttractions(HomeActivity activity) {

        this.activity = activity;
    }

    @Override
    protected MainAreaListResponse doInBackground(Void... params) {

        BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

        builder.setRootUrl(RequestsModule.ROOT_URL);

        MainAreaListResponse areaListResponse = new MainAreaListResponse();

        try {

            areaListResponse = builder.build().areas().amenities().type("ATTRACTION", RequestsModule.LANGUAGE).setKey(RequestsModule.API_KEY).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return areaListResponse;
    }

    @Override
    protected void onPreExecute() {

        activity.updateProgress(false);
    }

    @Override
    protected void onPostExecute(MainAreaListResponse response) {

        activity.updateProgress(true);

        ArrayList<Parcelable> list = RequestsModule.convertListResponseToList(response);
        activity.createListFragment(list, Amenity.class);
    }
}