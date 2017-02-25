package com.callumveale.bjorneparken.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

import com.callumveale.bjorneparken.R;
import none.bjorneparkappen.Bjorneparkappen;
import none.bjorneparkappen.model.MainAmenityResponse;
import none.bjorneparkappen.model.MainAreaListResponse;

import static com.callumveale.bjorneparken.activities.HomeActivity.API_KEY;
import static com.callumveale.bjorneparken.activities.HomeActivity.ROOT_URL;

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

            Bjorneparkappen.Builder builder = new Bjorneparkappen.Builder(
                    AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);

            builder.setRootUrl(ROOT_URL);

            MainAreaListResponse response = new MainAreaListResponse();

            try {

                response = builder.build().amenities().list("en").setKey(API_KEY).execute();

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
