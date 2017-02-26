package com.callumveale.bjorneparken.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;

import java.io.IOException;

import com.callumveale.bjorneparken.R;
import com.google.api.client.json.jackson2.JacksonFactory;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.MainSpeciesResponse;

import static com.callumveale.bjorneparken.activities.HomeActivity.API_KEY;
import static com.callumveale.bjorneparken.activities.HomeActivity.ROOT_URL;
import static com.callumveale.bjorneparken.activities.HomeActivity.sDefSystemLanguage;

public class ListSpeciesActivity extends AppCompatActivity {

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

        GetSpeciesTask task = new GetSpeciesTask(this);
        task.execute();
    }

    private void display(MainSpeciesListResponse response){

        if (response.getSpecies() != null) {

            for (MainSpeciesResponse speciesInstance : response.getSpecies()) {

                TextView textView = new TextView(this);
                textView.setTextSize(18);
                textView.setText(speciesInstance.getCommonName() + "\n" + speciesInstance.getLatin() + "\n" + speciesInstance.getDescription() + "\n\n");

                layout.addView(textView);
            }
        }
    }

    class GetSpeciesTask extends AsyncTask<Void, Void, MainSpeciesListResponse> {

        ListSpeciesActivity activity;

        GetSpeciesTask(ListSpeciesActivity activity){

            this.activity = activity;
        }

        @Override
        protected MainSpeciesListResponse doInBackground(Void... params) {

            BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

            builder.setRootUrl(ROOT_URL);

            MainSpeciesListResponse response = new MainSpeciesListResponse();

            try {

                response = builder.build().species().all(sDefSystemLanguage).setKey(API_KEY).execute();

            } catch (IOException e) {

                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(MainSpeciesListResponse response){

            activity.display(response);
        }
    }
}
