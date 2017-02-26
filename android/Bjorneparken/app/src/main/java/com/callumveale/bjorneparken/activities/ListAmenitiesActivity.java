package com.callumveale.bjorneparken.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;

import java.io.IOException;

import com.callumveale.bjorneparken.R;
import com.google.api.client.json.jackson2.JacksonFactory;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainAmenityResponse;
import none.bjorneparkappen_api.model.MainAreaListResponse;

import static com.callumveale.bjorneparken.activities.HomeActivity.API_KEY;
import static com.callumveale.bjorneparken.activities.HomeActivity.ROOT_URL;
import static com.callumveale.bjorneparken.activities.HomeActivity.sDefSystemLanguage;

public class ListAmenitiesActivity extends AppCompatActivity {

    ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Build toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieve view to which items will be added
        layout = (ViewGroup) findViewById(R.id.view);

        GetAmenitiesTask task = new GetAmenitiesTask(this);
        task.execute();
    }

    private void display(MainAreaListResponse response){

        if (response.getAmenities() != null) {

            for (MainAmenityResponse amenity : response.getAmenities()) {

                TextView textView = new TextView(this);
                textView.setTextSize(18);
                textView.setText(amenity.getLabel() + "\n" + amenity.getDescription() + "\n\n");

                layout.addView(textView);
            }
        }
    }

    class GetAmenitiesTask extends AsyncTask<Void, Void, MainAreaListResponse> {

        ListAmenitiesActivity activity;

        GetAmenitiesTask(ListAmenitiesActivity activity){

            this.activity = activity;
        }

        @Override
        protected MainAreaListResponse doInBackground(Void... params) {

            BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

            builder.setRootUrl(ROOT_URL);

            MainAreaListResponse response = new MainAreaListResponse();

            try {

                response = builder.build().areas().amenities().all(sDefSystemLanguage).setKey(API_KEY).execute();

            } catch (IOException e) {

                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(MainAreaListResponse response){

            activity.display(response);
        }
    }
}
