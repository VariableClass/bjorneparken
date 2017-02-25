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
import none.bjorneparkappen.model.MainEventListResponse;
import none.bjorneparkappen.model.MainEventResponse;

import static com.callumveale.bjorneparken.activities.HomeActivity.API_KEY;
import static com.callumveale.bjorneparken.activities.HomeActivity.ROOT_URL;
import static com.callumveale.bjorneparken.activities.HomeActivity.sDefSystemLanguage;

public class ListEventsActivity extends AppCompatActivity {

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

        GetEventsTask task = new GetEventsTask(this);
        task.execute();
    }

    private void display(MainEventListResponse response){

        if (response.getEvents() != null) {

            for (MainEventResponse event : response.getEvents()) {

                TextView textView = new TextView(this);
                textView.setTextSize(18);
                textView.setText(event.getLabel() + "\n" + event.getDescription() + "\n" + event.getLocation().getLabel() + "\n" + event.getStartTime() + "\n\n");

                layout.addView(textView);
            }
        }
    }

    class GetEventsTask extends AsyncTask<Void, Void, MainEventListResponse> {

        ListEventsActivity activity;

        GetEventsTask(ListEventsActivity activity){

            this.activity = activity;
        }

        @Override
        protected MainEventListResponse doInBackground(Void... params) {

            Bjorneparkappen.Builder builder = new Bjorneparkappen.Builder(
                    AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);

            builder.setRootUrl(ROOT_URL);

            MainEventListResponse response = new MainEventListResponse();

            try {

                response = builder.build().events().list(sDefSystemLanguage).setKey(API_KEY).execute();

            } catch (IOException e) {

                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(MainEventListResponse response){

            activity.display(response);
        }
    }
}
